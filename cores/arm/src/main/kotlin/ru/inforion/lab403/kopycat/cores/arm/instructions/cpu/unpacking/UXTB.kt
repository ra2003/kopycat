package ru.inforion.lab403.kopycat.cores.arm.instructions.cpu.unpacking

import ru.inforion.lab403.common.extensions.asInt
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.arm.ROR
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.cores.base.operands.Immediate
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

/**
 * Created by r.valitov on 30.01.18
 */

class UXTB(cpu: AARMCore,
           opcode: Long,
           cond: Condition,
           val rd: ARMRegister,
           val rm: ARMRegister,
           private val rotate: Immediate<AARMCore>,
           size: Int):
        AARMInstruction(cpu, Type.VOID, cond, opcode, rd, rm, rotate, size = size) {
    override val mnem = "UXTB$mcnd"

    override fun execute() {
        val rotated = ROR(rm.value(core), 32, rotate.value(core).asInt)
        rd.value(core, rotated[7..0])
    }
}