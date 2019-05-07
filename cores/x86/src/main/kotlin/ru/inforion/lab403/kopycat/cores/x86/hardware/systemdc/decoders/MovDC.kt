package ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.decoders

import ru.inforion.lab403.kopycat.cores.base.exceptions.GeneralException
import ru.inforion.lab403.kopycat.cores.x86.enums.Regtype
import ru.inforion.lab403.kopycat.cores.x86.enums.x86GPR
import ru.inforion.lab403.kopycat.cores.x86.hardware.processors.x86CPU
import ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.Prefixes
import ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.RMDC
import ru.inforion.lab403.kopycat.cores.x86.hardware.x86OperandStream
import ru.inforion.lab403.kopycat.cores.x86.instructions.AX86Instruction
import ru.inforion.lab403.kopycat.cores.x86.instructions.cpu.memory.Mov
import ru.inforion.lab403.kopycat.cores.x86.operands.x86Register
import ru.inforion.lab403.kopycat.cores.x86.operands.x86Register.GPRBL.al
import ru.inforion.lab403.kopycat.modules.cores.x86Core

/**
 * Created by davydov_vn on 22.09.16.
 */

class MovDC(core: x86Core) : ADecoder<AX86Instruction>(core) {
    override fun decode(s: x86OperandStream, prefs: Prefixes): AX86Instruction {
        val opcode = s.last
        val rm = RMDC(s, prefs)
        val ops = when (opcode) {
            0x88 -> arrayOf(rm.m8, rm.r8)
            0x89 -> arrayOf(rm.mpref, rm.rpref)
            0x8A -> arrayOf(rm.r8, rm.m8)
            0x8B -> arrayOf(rm.rpref, rm.mpref)
            0x8C -> arrayOf(rm.m16, rm.r(rtyp = Regtype.SSR))
            0x8E -> {
                if (core.cpu.mode == x86CPU.Mode.R32)
                    prefs.operandOverride = true;
                arrayOf(rm.r(rtyp = Regtype.SSR), rm.mpref)
            }

            0xA0 -> arrayOf(al, s.mem8(prefs))
            0xA1 -> arrayOf(x86Register.gpr(prefs.opsize, x86GPR.EAX.id), s.mem(prefs))
            0xA2 -> arrayOf(s.mem8(prefs), al)
            0xA3 -> arrayOf(s.mem(prefs), x86Register.gpr(prefs.opsize, x86GPR.EAX.id))

            0xB0, 0xB1, 0xB2, 0xB3, 0xB4, 0xB5, 0xB6, 0xB7 ->
                arrayOf(x86Register.gpr8(opcode % 0xB0), s.imm8)

            0xB8, 0xB9, 0xBA, 0xBB, 0xBC, 0xBD, 0xBE, 0xBF ->
                arrayOf(x86Register.gpr(prefs.opsize, opcode % 0xB8), s.imm(prefs))

            0xC6 -> arrayOf(rm.m8, s.imm8)
            0xC7 -> arrayOf(rm.mpref, s.imm(prefs))
            else -> throw GeneralException("Incorrect opcode in decoder")
        }
        return Mov(core, s.data, prefs, *ops)
    }
}