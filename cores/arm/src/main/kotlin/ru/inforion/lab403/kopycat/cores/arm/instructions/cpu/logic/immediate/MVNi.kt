package ru.inforion.lab403.kopycat.cores.arm.instructions.cpu.logic.immediate

import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.exceptions.ARMHardwareException.Unpredictable
import ru.inforion.lab403.kopycat.cores.arm.hardware.flags.FlagProcessor
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMVariable
import ru.inforion.lab403.kopycat.cores.base.enums.Datatype
import ru.inforion.lab403.kopycat.cores.base.operands.Immediate
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

/**
 * Created by the bat on 17.01.18.
 */

class MVNi(cpu: AARMCore,
           opcode: Long,
           cond: Condition,
           val setFlags: Boolean,
           val rd: ARMRegister,
           val rn: ARMRegister,
           val imm32: Immediate<AARMCore>,
           val carry: Boolean):
        AARMInstruction(cpu, Type.VOID, cond, opcode, rd, imm32) {

    override val mnem = "MVN${if(setFlags) "S" else ""}$mcnd"

    private var result = ARMVariable(Datatype.DWORD)

    override fun execute() {
        result.value(core, imm32.inv(core))
        if(rd.reg == 15) {
            if(setFlags) throw Unpredictable
            core.cpu.ALUWritePC(result.value(core))
        } else {
            rd.value(core, result)
            if (setFlags)
                FlagProcessor.processLogicFlag(core, result, carry)
        }
    }
}