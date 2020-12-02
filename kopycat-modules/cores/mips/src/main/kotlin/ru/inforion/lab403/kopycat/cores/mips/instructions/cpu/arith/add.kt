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
package ru.inforion.lab403.kopycat.cores.mips.instructions.cpu.arith

import ru.inforion.lab403.common.extensions.isIntegerOverflow
import ru.inforion.lab403.common.extensions.toULong
import ru.inforion.lab403.kopycat.cores.mips.exceptions.MipsHardwareException
import ru.inforion.lab403.kopycat.cores.mips.instructions.RdRsRtInsn
import ru.inforion.lab403.kopycat.cores.mips.operands.MipsRegister
import ru.inforion.lab403.kopycat.modules.cores.MipsCore

/**
 *
 * ADD rd, rs, rt
 *
 * To add 32-bit integers. If an overflow occurs, then trap.
 *
 * The 32-bit word value in GPR rt is added to the 32-bit value in GPR rs to produce a 32-bit result.
 */
class add(
        core: MipsCore,
        data: Long,
        rd: MipsRegister,
        rs: MipsRegister,
        rt: MipsRegister) : RdRsRtInsn(core, data, Type.VOID, rd, rs, rt) {

    override val mnem = "add"

    override fun execute() {
        // MIPS guide is weird and cause exception each time if second operand < 0
        val op1 = vrs.toInt()
        val op2 = vrt.toInt()
        val res = op1 + op2
        if (isIntegerOverflow(op1, op2, res))
            throw MipsHardwareException.OV(core.pc)
        vrd = res.toULong()
    }
}
