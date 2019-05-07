package ru.inforion.lab403.kopycat.cores.arm.instructions.cpu.multiply

import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.arm.UInt
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.hardware.flags.FlagProcessor
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMVariable
import ru.inforion.lab403.kopycat.cores.base.enums.Datatype
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

/**
 * Created by r.valitov on 22.01.18
 */

class UMLAL(cpu: AARMCore,
            opcode: Long,
            cond: Condition,
            val flags: Boolean,
            val rdHi: ARMRegister,
            val rdLo: ARMRegister,
            val rm: ARMRegister,
            val rn: ARMRegister):
        AARMInstruction(cpu, Type.VOID, cond, opcode, rdHi, rdLo, rm, rn) {
    override val mnem = "UMLAL$mcnd"

    val result = ARMVariable(Datatype.QWORD)
    override fun execute() {
        val acc = UInt(rdHi.value(core), 32).shl(32) + UInt(rdLo.value(core), 32)
        result.value(core, UInt(rn.value(core), 32) * UInt(rm.value(core), 32) + acc)
        rdHi.value(core, result.value(core)[63..32])
        rdLo.value(core, result.value(core)[31..0])
        if (flags)
            FlagProcessor.processMulFlag(core, result)
    }
}