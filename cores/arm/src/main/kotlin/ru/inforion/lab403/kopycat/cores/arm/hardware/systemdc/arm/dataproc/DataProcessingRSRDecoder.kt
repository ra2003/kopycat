package ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.arm.dataproc

import ru.inforion.lab403.common.extensions.asInt
import ru.inforion.lab403.common.extensions.find
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.arm.DecodeRegShift
import ru.inforion.lab403.kopycat.cores.arm.SRType
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.decoders.ADecoder
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

/**
 * Created by the bat on 17.01.18.
 */

class DataProcessingRSRDecoder(
        cpu: AARMCore,
        val constructor: (
                cpu: AARMCore,
                opcode: Long,
                cond: Condition,
                setFlags: Boolean,
                rd: ARMRegister,
                rn: ARMRegister,
                rm: ARMRegister,
                rs: ARMRegister,
                shiftT: SRType) -> AARMInstruction) : ADecoder<AARMInstruction>(cpu) {
    override fun decode(data: Long): AARMInstruction {
        val cond = find<Condition> { it.opcode == data[31..28].asInt }?: Condition.AL
        val rd = ARMRegister.gpr(data[15..12].asInt)
        val rn = ARMRegister.gpr(data[19..16].asInt)
        val rm = ARMRegister.gpr(data[3..0].asInt)
        val rs = ARMRegister.gpr(data[11..8].asInt)
        val setFlags = data[20] == 1L
        val type = data[6..5]
        val shiftT = DecodeRegShift(type)
        return constructor(core, data, cond, setFlags, rd, rn, rm, rs, shiftT)
    }
}