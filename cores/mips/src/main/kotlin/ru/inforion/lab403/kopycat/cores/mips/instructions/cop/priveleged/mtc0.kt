package ru.inforion.lab403.kopycat.cores.mips.instructions.cop.priveleged

import ru.inforion.lab403.kopycat.cores.mips.instructions.RtRdSelInsn
import ru.inforion.lab403.kopycat.cores.mips.operands.GPR
import ru.inforion.lab403.kopycat.cores.mips.operands.MipsRegister
import ru.inforion.lab403.kopycat.modules.cores.MipsCore


/**
 * Created by batman on 03/06/16.
 *
 * MTC0 rt, rd, sel
 */
class mtc0(core: MipsCore,
           data: Long,
           rt: GPR,
           rd: MipsRegister<*>) : RtRdSelInsn(core, data, Type.VOID, rt, rd) {

    override val mnem = "mtc0"

    override fun execute() {
        vrd = vrt
    }
}

