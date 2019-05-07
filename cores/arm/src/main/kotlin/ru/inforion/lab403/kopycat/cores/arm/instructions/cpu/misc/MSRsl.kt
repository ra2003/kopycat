package ru.inforion.lab403.kopycat.cores.arm.instructions.cpu.misc

import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMVariable
import ru.inforion.lab403.kopycat.cores.base.enums.Datatype
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

/**
 * Created by r.valitov on 25.01.18
 */

class MSRsl(cpu: AARMCore,
            opcode: Long,
            cond: Condition,
            val rd: ARMRegister,
            val mask: Long,
            val writeSPSR: Boolean):
        AARMInstruction(cpu, Type.VOID, cond, opcode, rd) {
    override val mnem = "MSR$mcnd"

    val result = ARMVariable(Datatype.WORD)
    override fun execute() {

    }
}