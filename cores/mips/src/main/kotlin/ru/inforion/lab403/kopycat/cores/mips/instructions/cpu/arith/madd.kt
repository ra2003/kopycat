package ru.inforion.lab403.kopycat.cores.mips.instructions.cpu.arith

import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.mips.instructions.RdRsRtInsn
import ru.inforion.lab403.kopycat.cores.mips.operands.GPR
import ru.inforion.lab403.kopycat.modules.cores.MipsCore


/**
 * Created by batman on 03/06/16.
 */
class madd(
        core: MipsCore,
        data: Long,
        rd: GPR,
        rs: GPR,
        rt: GPR) : RdRsRtInsn(core, data, Type.VOID, rd, rs, rt)  {

    override val mnem = "madd"

    override fun execute() {
        val v1 = (hi shl 32) or lo
        val v2 = v1 + vrs.toInt().toLong() * vrt.toInt().toLong()
        hi = v2[63..32]
        lo = v2[31..0]
    }

}