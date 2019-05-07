package ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.thumb16

import ru.inforion.lab403.common.extensions.asInt
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.decoders.ADecoder
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMImmediate
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.cores.base.operands.Immediate
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

class ThumbAdrDecoder(cpu: AARMCore,
                      private val constructor: (
                              cpu: AARMCore,
                              opcode: Long,
                              cond: Condition,
                              add: Boolean,
                              rd: ARMRegister,
                              imm: Immediate<AARMCore>,
                              size: Int) -> AARMInstruction) : ADecoder<AARMInstruction>(cpu) {
    override fun decode(data: Long): AARMInstruction {
        val rd = ARMRegister.gpr(data[10..8].asInt)
        val imm = ARMImmediate(data[7..0] shl 2, true)
        return constructor(core, data, Condition.AL, true, rd, imm, 2)
    }
}