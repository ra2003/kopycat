package ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.arm.extraloadstore

import ru.inforion.lab403.common.extensions.asInt
import ru.inforion.lab403.common.extensions.find
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.exceptions.ARMHardwareException.Unpredictable
import ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.decoders.ADecoder
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMImmediate
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.cores.base.operands.Immediate
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

/**
 * Created by r.valitov on 16.01.18
 */

class LoadStoreHalfwordImmDecoder(
        cpu: AARMCore,
        private val isStore: Boolean,
        val constructor: (
                cpu: AARMCore,
                opcode: Long,
                cond: Condition,
                index: Boolean,
                add: Boolean,
                wback: Boolean,
                rn: ARMRegister,
                rt: ARMRegister,
                imm32: Immediate<AARMCore>,
                size: Int) -> AARMInstruction) : ADecoder<AARMInstruction>(cpu) {
    override fun decode(data: Long): AARMInstruction {
        val cond = find<Condition> { it.opcode == data[31..28].asInt } ?: Condition.AL
        val rn = ARMRegister.gpr(data[19..16].asInt)
        val rt = ARMRegister.gpr(data[15..12].asInt)
        val imm32 = ARMImmediate(data[11..8].shl(4) + data[3..0], true)
        val index = data[24] == 1L
        val add = data[23] == 1L
        val wback = data[24] == 0L || data[21] == 1L

        if (rt.reg == 15) throw Unpredictable
        if (wback && ((rn.reg == 15 && isStore) || rn.reg == rt.reg)) throw Unpredictable
        return constructor(core, data, cond, index, add, wback, rn, rt, imm32, 4)
    }
}