/*
 *
 * This file is part of Kopycat emulator software.
 *
 * Copyright (C) 2020 INFORION, LLC
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Non-free licenses may also be purchased from INFORION, LLC, 
 * for users who do not want their programs protected by the GPL. 
 * Contact us for details kopycat@inforion.ru
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package ru.inforion.lab403.kopycat.cores.mips.instructions.cop.memory

import ru.inforion.lab403.kopycat.cores.base.exceptions.GeneralException
import ru.inforion.lab403.kopycat.cores.mips.instructions.FtOffsetInsn
import ru.inforion.lab403.kopycat.cores.mips.operands.MipsDisplacement
import ru.inforion.lab403.kopycat.cores.mips.operands.MipsRegister
import ru.inforion.lab403.kopycat.modules.cores.MipsCore

/**
 *
 * LDC2 ft, offset(base)
 */
class ldc2(
        core: MipsCore,
        data: Long,
        ct: MipsRegister<*>,
        off: MipsDisplacement) : FtOffsetInsn(core, data, Type.VOID, ct, off) {

//    override val store = false
//    override val dtyp = Datatype.QWORD
//    override val core = ProcType.ImplementSpecCop
    override val mnem = "ldc2"

    override fun execute() {
        throw GeneralException("Sorry, but I don't know how to execute this instruction!")
    }
}