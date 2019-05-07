package ru.inforion.lab403.kopycat.cores.mips.instructions.cpu.trap

import ru.inforion.lab403.kopycat.cores.mips.exceptions.MipsHardwareException
import ru.inforion.lab403.kopycat.cores.mips.instructions.RsImmInsn
import ru.inforion.lab403.kopycat.cores.mips.operands.GPR
import ru.inforion.lab403.kopycat.cores.mips.operands.MipsImmediate
import ru.inforion.lab403.kopycat.modules.cores.MipsCore

/**
 * Created by batman on 03/06/16.
 *
 * TGEIU rs, immediate
 *
 * To compare a GPR to a constant and do a conditional trap
 *
 * Compare the contents of GPR rs and the 16-bit sign-extended immediate as unsigned integers; if GPR rs is
 * greater than or equal to immediate, then take a Trap exception. Because the 16-bit immediate is sign-extended
 * before comparison, the instruction can represent the smallest or largest unsigned numbers.
 * The representable values are at the minimum [0, 32767] or maximum [max_unsigned-32767, max_unsigned]
 * end of the unsigned range.
 */
class tgeiu(core: MipsCore,
            data: Long,
            rs: GPR,
            imm: MipsImmediate) : RsImmInsn(core, data, Type.VOID, rs, imm) {

//    override val isSigned: Boolean = false
//    override val construct = ::tgeiu
    override val mnem = "tgeiu"

    override fun execute() {
        // Compare as unsigned integers
        // TODO: Refactor legacy operand class usage
        if (vrs >= imm.usext) throw MipsHardwareException.TR(core.pc)
    }

}