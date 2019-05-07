package ru.inforion.lab403.kopycat.cores.x86.instructions.cpu.bitwise

import ru.inforion.lab403.common.extensions.asULong
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.base.operands.AOperand
import ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.Prefixes
import ru.inforion.lab403.kopycat.cores.x86.instructions.AX86Instruction
import ru.inforion.lab403.kopycat.modules.cores.x86Core

/**
 * Created by the bat on 15.07.17.
 */
class Bsr(core: x86Core, opcode: ByteArray, prefs: Prefixes, vararg operands: AOperand<x86Core>):
        AX86Instruction(core, Type.VOID, opcode, prefs, *operands) {
    override val mnem = "bsr"

    override fun execute() {
        val src = op2.value(core)
        if (src == 0L) {
            core.cpu.flags.zf = true
            op1.value(core, 0L)
        } else {
            core.cpu.flags.zf = false
            var temp = prefs.opsize.bits - 1
            while (src[temp] == 0L) temp -= 1
            op1.value(core, temp.asULong)
        }
    }
}