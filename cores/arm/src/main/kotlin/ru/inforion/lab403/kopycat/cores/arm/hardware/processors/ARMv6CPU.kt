package ru.inforion.lab403.kopycat.cores.arm.hardware.processors

import gnu.trove.map.hash.THashMap
import ru.inforion.lab403.common.extensions.clr
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.common.extensions.hex8
import ru.inforion.lab403.common.extensions.insert
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.kopycat.cores.arm.enums.GPR
import ru.inforion.lab403.kopycat.cores.arm.enums.Mode
import ru.inforion.lab403.kopycat.cores.arm.exceptions.ARMHardwareException.Unpredictable
import ru.inforion.lab403.kopycat.cores.arm.hardware.registers.EPSRBank
import ru.inforion.lab403.kopycat.cores.arm.hardware.registers.IPSRBank
import ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.decoders.Thumb16Decoder
import ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.decoders.Thumb32Decoder
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.base.GenericSerializer
import ru.inforion.lab403.kopycat.cores.base.exceptions.GeneralException
import ru.inforion.lab403.kopycat.modules.cores.AARMCore.InstructionSet
import ru.inforion.lab403.kopycat.modules.cores.ARMv6Core
import java.util.logging.Level
import kotlin.collections.Map
import kotlin.collections.mapOf
import kotlin.collections.set

/**
 * Created by the bat on 13.01.18.
 */

class ARMv6CPU(core: ARMv6Core, name: String) : AARMCPU(core, name) {
    companion object {
        val log = logger(Level.FINER)
    }

    override fun CurrentMode(): Mode = CurrentMode

    override fun CurrentModeIsPrivileged(): Boolean = (CurrentMode == Mode.Handler || !spr.npriv)

    override fun StackPointerSelect(): Int =
            if(arm.cpu.spr.spsel){
                when(CurrentMode){
                    Mode.Thread -> GPR.SPProcess.id
                    Mode.Handler -> GPR.SPMain.id
                }
            } else GPR.SPMain.id

    override fun ALUWritePC(address: Long) = BranchWritePC(address)

    override fun LoadWritePC(address: Long) = BXWritePC(address)

    override fun BranchWritePC(address: Long) {
        BranchTo(address clr 0)
    }

    override fun BXWritePC(address: Long) {
        if(CurrentMode == Mode.Handler && address[31..28] == 0xFL) {
            ExceptionReturn(address)
        } else {
            epsr.t = address[0] == 1L
            BranchTo(address clr 0)
        }
    }

    fun BLXWritePC(address: Long) {
        epsr.t = address[0] == 1L
        pc = address clr 0
    }

    override fun CurrentInstrSet(): InstructionSet = InstructionSet.THUMB

    override fun SelectInstrSet(target: InstructionSet) {}

    var CurrentMode: Mode = Mode.Thread

    fun ExceptionReturn(excReturn: Long) {
        if(CurrentMode != Mode.Handler) throw GeneralException("ExceptionReturn() function call in Thread mode")
        if(excReturn[27..4] != 0xFF_FFFFL) throw Unpredictable

        val framePtr: Long

        when(excReturn[3..0]){
            0b0001L -> {
                framePtr = regs.spMain
                CurrentMode = Mode.Handler
                spr.spsel = false
            }
            0b1001L -> {
                framePtr = regs.spMain
                CurrentMode = Mode.Thread
                spr.spsel = false
            }
            0b1101L -> {
                framePtr = regs.spProcess
                CurrentMode = Mode.Thread
                spr.spsel = true
            }
            else -> throw Unpredictable
        }

        PopStack(framePtr, excReturn)

        if(CurrentMode == Mode.Handler){ if(ipsr.exceptionNumber == 0L) throw Unpredictable }
        else {if(ipsr.exceptionNumber != 0L) throw Unpredictable}
    }

    fun PopStack(framePtr: Long, excReturn: Long) {
        arm.cpu.regs.r0  = core.inl(framePtr)
        arm.cpu.regs.r1  = core.inl(framePtr + 0x4)
        arm.cpu.regs.r2  = core.inl(framePtr + 0x8)
        arm.cpu.regs.r3  = core.inl(framePtr + 0xC)
        arm.cpu.regs.r12 = core.inl(framePtr + 0x10)
        arm.cpu.regs.lr  = core.inl(framePtr + 0x14)
        val pc  = core.inl(framePtr + 0x18) clr 0
        val psr = core.inl(framePtr + 0x1C)

//        if(pc[0] == 1L) throw Unpredictable
        BranchTo(pc)

        when(excReturn[3..0]){
            0b0001L, 0b1001L -> regs.spMain = (regs.spMain + 0x20).insert(psr[9], 2)
            0b1101L -> regs.spProcess = (regs.spProcess + 0x20).insert(psr[9], 2)
            else -> throw Unpredictable
        }

        arm.cpu.sregs.apsr = insert(psr[31..28], 31..28)

        val forceThread = (CurrentMode == Mode.Thread && spr.npriv)
        ipsr.exceptionNumber = if(forceThread) 0 else psr[5..0]
        epsr.t = psr[24] == 1L
    }

    private val epsr = EPSRBank(core)
    private val ipsr = IPSRBank(core)

    val VTOR = 0x800_0000

    private val thumb16 = Thumb16Decoder(core)
    private val thumb32 = Thumb32Decoder(core)

    override fun reset() {
        super.reset()
        regs.reset()
        flags.reset()
        status.reset()
        epsr.reset()
        ipsr.reset()

        CurrentMode = Mode.Thread

        val address = core.inl(0x0800_0004)

        log.fine { "Set entry point address to ${address.hex8}" }

        BXWritePC(address)

        pipelineRefillRequired = false
    }

    private fun fetch(where: Long): Long = core.inl(where)

    private fun swapByte(data: Long): Long {
        val high = data and 0xFFFF_0000
        val low = data and 0xFFFF
        return (high shr 16) or (low shl 16)
    }

    private val cache = THashMap<Long, Pair<AARMInstruction, Int>>(1024*1024)

    override fun execute(): Int {
        val offset: Int

        var data = fetch(pc clr 0)  // we must allays call fetch to check breakpoints 
        val e = cache[pc]
        if (e == null) {
            val type = data[15..11]

            if (type != 0b11101L && type != 0b11110L && type != 0b11111L) {
                data = data[15..0]
                insn = thumb16.decode(data)
                offset = 2
            } else {
                data = swapByte(data)
                insn = thumb32.decode(data)
                offset = 0
            }

            insn.ea = pc

            cache[pc] = Pair(insn, offset)
        } else {
            insn = e.first
            offset = e.second
        }

//        log.finer { ("[${pc.hex8}] ${insn.opcode.hex8} $insn") }

        pc += insn.size + offset

        try {
            insn.execute()
        } catch (error: Throwable) {
            pc = insn.ea
            throw error
        }

        // PC points at the address for fetched instruction when executing restore normal code flow if no jump occurred
        if (!pipelineRefillRequired) pc -= offset
        // PC has been changed during instruction execution-> nothing to fix up
        else pipelineRefillRequired = false

        return 1  // TODO: get from insn.execute()
    }

    override fun serialize(ctxt: GenericSerializer): Map<String, Any> {
        return mapOf("regs" to regs.serialize(ctxt), "pc" to pc.hex8)
    }

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(ctxt: GenericSerializer, snapshot: Map<String, Any>) {
        regs.deserialize(ctxt, snapshot["regs"] as Map<String, String>)
    }
}