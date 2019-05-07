package ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.arm.packing

import ru.inforion.lab403.common.extensions.asInt
import ru.inforion.lab403.common.extensions.find
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.exceptions.ARMHardwareException.Unpredictable
import ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.decoders.ADecoder
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

class ByteSelDecoder(cpu: AARMCore,
                     val constructor: (
                             cpu: AARMCore,
                             opcode: Long,
                             cond: Condition,
                             rn: ARMRegister,
                             rd: ARMRegister,
                             rm: ARMRegister) -> AARMInstruction) : ADecoder<AARMInstruction>(cpu) {
    override fun decode(data: Long): AARMInstruction {
        val cond = find<Condition> { it.opcode == data[31..28].asInt } ?: Condition.AL
        val rn = ARMRegister.gpr(data[19..16].asInt)
        val rd = ARMRegister.gpr(data[15..12].asInt)
        val rm = ARMRegister.gpr(data[3..0].asInt)

        if(rd.reg == 15 || rm.reg == 15 || rn.reg == 15) throw Unpredictable

        return constructor(core, data, cond, rn, rd, rm)
    }
}