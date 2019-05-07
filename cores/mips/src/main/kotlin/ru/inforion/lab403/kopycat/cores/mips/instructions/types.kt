package ru.inforion.lab403.kopycat.cores.mips.instructions

import ru.inforion.lab403.common.extensions.toInt
import ru.inforion.lab403.kopycat.cores.base.abstracts.AInstruction.Type.VOID
import ru.inforion.lab403.kopycat.cores.mips.DoubleRegister
import ru.inforion.lab403.kopycat.cores.mips.enums.COND
import ru.inforion.lab403.kopycat.cores.mips.hardware.processors.ACOP0
import ru.inforion.lab403.kopycat.cores.mips.operands.*
import ru.inforion.lab403.kopycat.modules.cores.MipsCore


abstract class CcFsFtInsn(core: MipsCore, data: Long, type: Type, val fs: FPR, val ft: FPR, val cc: MipsImmediate) : AMipsInstruction(core, data, type, fs, ft, cc) {
    inline var vfs: Long
        get() = fs.value(core)
        set(value) = fs.value(core, value)
    inline var vft: Long
        get() = ft.value(core)
        set(value) = ft.value(core, value)

    var dfs : Long by DoubleRegister(1)
    var dft : Long by DoubleRegister(2)

    //  FCC0 bit is 23
    inline var vcc: Boolean
        get() = FCR.fccr.bit(core, 23) == 1
        set(value) = FCR.fccr.bit(core, 23, value.toInt())

    val cond by lazy { COND.values().find { it.ordinal == (op3 as MipsImmediate).value.toInt() }!! }
}

abstract class CcOffsetInsn(core: MipsCore, data: Long, type: Type, val cc: MipsImmediate, val off: MipsNear) : AMipsInstruction(core, data, type, cc, off) {

    inline val address: Long get() = off.offset + (core.cpu.pc + size)
    inline val eaAfterBranch: Long get() = core.cpu.pc + 8

    //  FCC0 bit is 23
    inline var vcc: Boolean
        get() = FCR.fccr.bit(core, 23) == 1
        set(value) = FCR.fccr.bit(core, 23, value.toInt())
}

abstract class Code19bitInsn(core: MipsCore, data: Long, type: Type, val imm: MipsImmediate) :
        AMipsInstruction(core, data, type, imm) {

    inline val cop0: ACOP0 get() = core.cop

    inline var index: Long
        get() = cop0.regs.Index
        set(value) {
            cop0.regs.Index = value
        }

    inline val random: Long get() = cop0.regs.Random

    inline var pageMask: Long
        get() = cop0.regs.PageMask
        set(value) {
            cop0.regs.PageMask = value
        }
    inline var entryHi: Long
        get() = cop0.regs.EntryHi
        set(value) {
            cop0.regs.EntryHi = value
        }
    inline var entryLo0: Long
        get() = cop0.regs.EntryLo0
        set(value) {
            cop0.regs.EntryLo0 = value
        }
    inline var entryLo1: Long
        get() = cop0.regs.EntryLo1
        set(value) {
            cop0.regs.EntryLo1 = value
        }
}

abstract class Code20bitInsn(core: MipsCore, data: Long, type: Type, val code: MipsImmediate) : AMipsInstruction(core, data, type, code)
abstract class Cofun25BitInsn(core: MipsCore, data: Long, type: Type, val cofun: MipsImmediate) : AMipsInstruction(core, data, type, cofun)

abstract class FdFsInsn(core: MipsCore, data: Long, type: Type, val fd: FPR, val fs: FPR) : AMipsInstruction(core, data, type, fd, fs) {
    inline var vfs: Long
        get() = fs.value(core)
        set(value) = fs.value(core, value)
    inline var vfd: Long
        get() = fd.value(core)
        set(value) = fd.value(core, value)
    var dfd : Long by DoubleRegister(1)
    var dfs : Long by DoubleRegister(2)
}

abstract class FdFsFtInsn(core: MipsCore, data: Long, type: Type, val fd: FPR, val fs: FPR, val ft: FPR) : AMipsInstruction(core, data, type, fd, fs, ft) {
    inline var vfd: Long
        get() = fd.value(core)
        set(value) = fd.value(core, value)
    inline var vfs: Long
        get() = fs.value(core)
        set(value) = fs.value(core, value)
    inline var vft: Long
        get() = ft.value(core)
        set(value) = ft.value(core, value)

    var dfd : Long by DoubleRegister(1)
    var dfs : Long by DoubleRegister(2)
    var dft : Long by DoubleRegister(3)
}

abstract class IndexInsn(core: MipsCore, data: Long, type: Type, val index: MipsNear) : AMipsInstruction(core, data, type, index) {
    val address: Long get() = index.offset + (core.cpu.pc and 0xF0000000)

    inline var vra: Long
        get() = GPR.ra.value(core)
        set(value) = GPR.ra.value(core, value)

    val eaAfterBranch: Long get() = core.cpu.pc + 8
}

abstract class OpOffsetBaseInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val imm: MipsImmediate,
        val off: MipsDisplacement
) : AMipsInstruction(core, data, type, imm, off)

abstract class RdInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rd: GPR
) : AMipsInstruction(core, data, type, rd) {
    inline var vrd: Long
        get() = rd.value(core)
        set(value) = rd.value(core, value)
}

abstract class RdRsHintInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rd: GPR,
        val rs: GPR,
        val hint: MipsImmediate
) : AMipsInstruction(core, data, type, rd, rs, hint) {
    inline var vrd: Long
        get() = rd.value(core)
        set(value) = rd.value(core, value)
    inline var vrs: Long
        get() = rs.value(core)
        set(value) = rs.value(core, value)
    val eaAfterBranch: Long get() = core.cpu.pc + 8
}

abstract class RdRsCcInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rd: GPR,
        val rs: GPR,
        val cc: MipsImmediate
) : AMipsInstruction(core, data, type, rd, rs, cc)

abstract class RdRsRtInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rd: GPR,
        val rs: GPR,
        val rt: GPR
) : AMipsInstruction(core, data, type, rd, rs, rt) {
    inline var vrd: Long
        get() = rd.value(core)
        set(value) = rd.value(core, value)
    inline val vrs: Long get() = rs.value(core)
    inline val vrt: Long get() = rt.value(core)
}

abstract class RdRtInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rd: GPR,
        val rt: GPR
) : AMipsInstruction(core, data, type, rd, rt) {
    inline var vrd: Long
        get() = rd.value(core)
        set(value) = rd.value(core, value)
    inline val vrt: Long get() = rt.value(core)
}

abstract class RdRtRsInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rd: GPR,
        val rt: GPR,
        val rs: GPR
) : AMipsInstruction(core, data, type, rd, rt, rs) {
    inline var vrd: Long
        get() = rd.value(core)
        set(value) = rd.value(core, value)
    inline val vrt: Long get() = rt.value(core)
    inline val vrs: Long get() = rs.value(core)
}

abstract class RdRtSaInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rd: GPR,
        val rt: GPR,
        val sa: MipsImmediate
) : AMipsInstruction(core, data, type, rd, rt, sa) {
    inline var vrd: Long
        get() = rd.value(core)
        set(value) = rd.value(core, value)
    inline val vrt: Long get() = rt.value(core)
    inline val vsa: Int get() = sa.zext.toInt()
}

abstract class RsInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rs: GPR
) : AMipsInstruction(core, data, type, rs) {
    inline var vrs: Long
        get() = rs.value(core)
        set(value) = rs.value(core, value)
}

abstract class RsImmInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rs: GPR,
        val imm: MipsImmediate
) : AMipsInstruction(core, data, type, rs, imm) {
    inline var vrs: Long
        get() = rs.value(core)
        set(value) = rs.value(core, value)
}

abstract class RsOffsetInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rs: GPR,
        val off: MipsNear
) : AMipsInstruction(core, data, type, rs, off) {
    inline val vrs: Long get() = rs.value(core)
    inline val address: Long get() = off.offset + (core.cpu.pc + size)
    inline val eaAfterBranch: Long get() = core.cpu.pc + 8

    inline var vra: Long
        get() = GPR.ra.value(core)
        set(value) = GPR.ra.value(core, value)
}

abstract class RsRtCodeInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rs: GPR,
        val rt: GPR,
        val code: MipsImmediate
) : AMipsInstruction(core, data, type, rs, rt, code) {
    inline var vrs: Long
        get() = rs.value(core)
        set(value) = rs.value(core, value)
    inline val vrt: Long get() = rt.value(core)
    inline val vcode: Long get() = code.zext
}

abstract class RsRtOffsetInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rs: MipsRegister<*>,
        val rt: MipsRegister<*>,
        val off: MipsNear
) : AMipsInstruction(core, data, type, rs, rt, off) {
    inline val vrs: Long get() = rs.value(core)
    inline val vrt: Long get() = rt.value(core)
    inline val address: Long get() = off.offset + (core.cpu.pc + size)
    inline val eaAfterBranch: Long get() = core.cpu.pc + 8
}

abstract class RsRtPosSizeInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rs: GPR,
        val rt: GPR,
        val pos: MipsImmediate,
        val siz: MipsImmediate
) : AMipsInstruction(core, data, type, rs, rt, pos, siz) {
    inline var vrs: Long
        get() = rs.value(core)
        set(value) = rs.value(core, value)
    inline val vrt: Long get() = rt.value(core)
    inline val lsb: Int get() = pos.zext.toInt()
    inline val msb: Int get() = siz.zext.toInt()
}

abstract class RtInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rt: GPR
) : AMipsInstruction(core, data, type, rt) {
    inline val cop0: ACOP0 get() = core.cop
    inline var vrt: Long
        get() = rt.value(core)
        set(value) = rt.value(core, value)
}

abstract class RtImmInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rt: GPR,
        val imm: MipsImmediate
) : AMipsInstruction(core, data, type, rt, imm) {
    inline var vrt: Long
        get() = rt.value(core)
        set(value) = rt.value(core, value)
}

abstract class FtOffsetInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val ct: MipsRegister<*>,
        val off: MipsDisplacement
) : AMipsInstruction(core, data, type, ct, off) {
    inline var vct: Long
        get() = ct.value(core)
        set(value) = ct.value(core, value)
    inline var memword: Long
        get() = off.value(core)
        set(value) = off.value(core, value)
    inline val address: Long get() = off.effectiveAddress(core)
    var dft : Long by DoubleRegister(1)
}

abstract class RtOffsetInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rt: GPR,
        val off: MipsDisplacement
) : AMipsInstruction(core, data, type, rt, off) {
    inline var vrt: Long
        get() = rt.value(core)
        set(value) = rt.value(core, value)
    inline var memword: Long
        get() = off.value(core)
        set(value) = off.value(core, value)
    inline val address: Long get() = off.effectiveAddress(core)
    var dft : Long by DoubleRegister(1)
}

//abstract class RtRd(core: MipsCore, data: Long, type: Type, val rt: MipsRegister, val rd: MipsRegister) : AMipsInstruction(core, data, type, rt, rd)

abstract class RtRdSelInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rt: GPR,
        val rd: MipsRegister<*>
) : AMipsInstruction(core, data, type, rt, rd) {
    inline var vrt: Long
        get() = rt.value(core)
        set(value) = rt.value(core, value)
    inline var vrd: Long
        get() = rd.value(core)
        set(value) = rd.value(core, value)
}

abstract class RtRsImmInsn(
        core: MipsCore,
        data: Long,
        type: Type,
        val rt: GPR,
        val rs: GPR,
        val imm: MipsImmediate
) : AMipsInstruction(core, data, type, rt, rs, imm) {
    inline var vrt: Long
        get() = rt.value(core)
        set(value) = rt.value(core, value)
    inline val vrs: Long get() = rs.value(core)
}

abstract class EmptyInsn(core: MipsCore, data: Long) : AMipsInstruction(core, data, VOID)