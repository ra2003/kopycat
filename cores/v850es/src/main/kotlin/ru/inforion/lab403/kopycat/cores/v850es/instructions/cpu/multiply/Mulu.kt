package ru.inforion.lab403.kopycat.cores.v850es.instructions.cpu.multiply

import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.base.exceptions.GeneralException
import ru.inforion.lab403.kopycat.cores.base.operands.AOperand
import ru.inforion.lab403.kopycat.cores.v850es.instructions.AV850ESInstruction
import ru.inforion.lab403.kopycat.modules.cores.v850ESCore

/**
 * Created by user on 29.05.17.
 */

class Mulu(core: v850ESCore, size: Int, vararg operands: AOperand<v850ESCore>):
        AV850ESInstruction(core, Type.VOID, size, *operands) {
    override val mnem = "mulu"

    // Format XI - reg1, reg2, reg3
    // Format XII - imm, reg2, reg3
    override fun execute() {
        val res = op2.value(core) * op1.value(core)
        op3.value(core, res[63..32])
        op2.value(core, res[31..0])
        throw GeneralException("Check Mulu insn")
    }
}