package ru.inforion.lab403.kopycat.cores.x86.instructions.fpu

import ru.inforion.lab403.kopycat.cores.base.operands.AOperand
import ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.Prefixes
import ru.inforion.lab403.kopycat.cores.x86.instructions.AX86Instruction
import ru.inforion.lab403.kopycat.modules.cores.x86Core

/**
 * Created by davydov_vn on 08.09.16.
 */

class Fldcw(core: x86Core, opcode: ByteArray, prefs: Prefixes, operand: AOperand<x86Core>):
        AX86Instruction(core, Type.VOID, opcode, prefs, operand) {
    override val mnem = "fldcw"

    override fun execute() {
        val data = op1.value(core)
        core.fpu.fwr.FPUControlWord = data
    }
}