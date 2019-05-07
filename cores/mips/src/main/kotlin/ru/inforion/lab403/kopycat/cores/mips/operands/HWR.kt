package ru.inforion.lab403.kopycat.cores.mips.operands

import ru.inforion.lab403.common.extensions.first
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.mips.enums.Designation
import ru.inforion.lab403.kopycat.cores.mips.enums.eHWR
import ru.inforion.lab403.kopycat.cores.mips.hardware.processors.ProcType
import ru.inforion.lab403.kopycat.modules.cores.MipsCore

class HWR(desc: eHWR) : MipsRegister<eHWR>(ProcType.ImplementSpecCop, Designation.General, desc) {
    constructor(id: Int) : this(first<eHWR> { it.id == id })

    // TODO: refactor HWR into classes

    override fun value(core: MipsCore): Long = when(reg) {
//        Number of the CPU on which the program is currently running. This register
//        provides read access to the coprocessor 0 EBaseCPUNum field.
        0 -> core.cop.regs.EBase[9..0]

//      Address step size to be used with the SYNCI instruction,
//      or zero if no caches need be synchronized.
//      See that instruction’s description for the use of this value.
        1 -> 0

//      High-resolution cycle counter. This register provides read access to the coprocessor
//      0 Count Register.
        2 -> core.cop.regs.Count

//        1 CC register increments every CPU cycle
//        2 CC register increments every second CPU cycle
//        3 CC register increments every third CPU cycle
        3 -> 1

//      User Local Register. This register provides read access to the coprocessor 0
//      UserLocal register, if it is implemented. In some operating environments, the
//      UserLocal register is a pointer to a thread-specific storage block.
        29 -> core.cop.regs.UserLocal

        else -> throw NotImplementedError("Hardware register not implemented $reg")
    }
    override fun value(core: MipsCore, data: Long): Unit = TODO("not implemented")
}