package ru.inforion.lab403.kopycat.cores.x86.exceptions

import ru.inforion.lab403.common.extensions.insert
import ru.inforion.lab403.kopycat.cores.base.exceptions.HardwareException
import ru.inforion.lab403.kopycat.cores.x86.enums.ExcCode

/**
 * Created by batman on 14/09/16.
 */
abstract class x86HardwareException(excCode: ExcCode, where: Long, val errorCode: Long = 0): HardwareException(excCode, where) {
    override fun toString(): String = "$prefix: $excCode"

    class DivisionByZero(where: Long) : x86HardwareException(ExcCode.DivisionByZero, where)
    class Debug(where: Long) : x86HardwareException(ExcCode.Debug, where)
    class NMI(where: Long) : x86HardwareException(ExcCode.NMI, where)
    class Breakpoint(where: Long) : x86HardwareException(ExcCode.Breakpoint, where)
    class Overflow(where: Long) : x86HardwareException(ExcCode.Overflow, where)
    class BoundRangeExceeded(where: Long) : x86HardwareException(ExcCode.BoundRangeExceeded, where)
    class InvalidOpcode(where: Long) : x86HardwareException(ExcCode.InvalidOpcode, where)
    class DeviceNotAvailable(where: Long) : x86HardwareException(ExcCode.DeviceNotAvailable, where)
    class DoubleFault(where: Long, errorCode: Long) : x86HardwareException(ExcCode.DoubleFault, where, errorCode)
    class CoprocessorSegmentOverrun(where: Long) : x86HardwareException(ExcCode.CoprocessorSegmentOverrun, where)
    class InvalidTSS(where: Long, errorCode: Long) : x86HardwareException(ExcCode.InvalidTSS, where, errorCode)
    class SegmentNotPresent(where: Long, errorCode: Long) : x86HardwareException(ExcCode.SegmentNotPresent, where, errorCode)
    class StackSegmentFault(where: Long, errorCode: Long) : x86HardwareException(ExcCode.StackSegmentFault, where, errorCode)
    class GeneralProtectionFault(where: Long, errorCode: Long) : x86HardwareException(ExcCode.GeneralProtectionFault, where, errorCode)
    class PageFault(where: Long, I: Int, R: Int, U: Int, W: Int, P: Int) :
            x86HardwareException(ExcCode.PageFault, where,
                    insert(I, 4)
                    .insert(R, 3)
                    .insert(U, 2)
                    .insert(W, 1)
                    .insert(P, 0).toLong())
}