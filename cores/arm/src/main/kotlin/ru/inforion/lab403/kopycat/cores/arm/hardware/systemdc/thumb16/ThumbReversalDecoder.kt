package ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.thumb16

import ru.inforion.lab403.common.extensions.asInt
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.decoders.ADecoder
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

class ThumbReversalDecoder(cpu: AARMCore,
                           private val constructor: (
                                   cpu: AARMCore,
                                   opcode: Long,
                                   cond: Condition,
                                   rd: ARMRegister,
                                   rm: ARMRegister,
                                   size: Int) -> AARMInstruction) : ADecoder<AARMInstruction>(cpu) {
    override fun decode(data: Long): AARMInstruction {
        val rd = ARMRegister.gpr(data[2..0].asInt)
        val rm = ARMRegister.gpr(data[5..3].asInt)
        return constructor(core, data, Condition.AL, rd, rm, 2)
    }
}