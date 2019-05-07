package ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.arm.packing

import ru.inforion.lab403.common.extensions.asInt
import ru.inforion.lab403.common.extensions.find
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.exceptions.ARMHardwareException.Unpredictable
import ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.decoders.ADecoder
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.cores.base.operands.Immediate
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

class xXTxDecoder(cpu: AARMCore,
                  val constructor: (
                          cpu: AARMCore,
                          opcode: Long,
                          cond: Condition,
                          rd: ARMRegister,
                          rm: ARMRegister,
                          rotate: Immediate<AARMCore>,
                          size: Int) -> AARMInstruction) : ADecoder<AARMInstruction>(cpu) {
    override fun decode(data: Long): AARMInstruction {
        val cond = find<Condition> { it.opcode == data[31..28].asInt } ?: Condition.AL
        val rd = ARMRegister.gpr(data[15..12].asInt)
        val rm = ARMRegister.gpr(data[3..0].asInt)

        val rotate = when (data[11..10].asInt) {
            0b01 -> Immediate(8, false)
            0b10 -> Immediate(16, false)
            0b11 -> Immediate(24, false)
            else -> Immediate<AARMCore>(0, false)
        }

        if(rd.reg == 15 || rm.reg == 15) throw Unpredictable

        return constructor(core, data, cond, rd, rm, rotate, 4)
    }
}