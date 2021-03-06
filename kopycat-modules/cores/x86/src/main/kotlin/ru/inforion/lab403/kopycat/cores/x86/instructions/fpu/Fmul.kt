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
package ru.inforion.lab403.kopycat.cores.x86.instructions.fpu

import ru.inforion.lab403.common.extensions.ieee754
import ru.inforion.lab403.kopycat.cores.base.operands.AOperand
import ru.inforion.lab403.kopycat.cores.x86.exceptions.x86HardwareException
import ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.Prefixes
import ru.inforion.lab403.kopycat.cores.x86.instructions.AX86Instruction
import ru.inforion.lab403.kopycat.modules.cores.x86Core
import kotlin.math.absoluteValue



class Fmul(core: x86Core, opcode: ByteArray, prefs: Prefixes, val popCount: Int, vararg operands: AOperand<x86Core>):
        AX86Instruction(core, Type.VOID, opcode, prefs, *operands) {
    override val mnem = "fmul"

    override fun execute() {
        val a1 = op1.value(core).ieee754()
        val a2 = op2.value(core).ieee754()
        if((a1 == 0.0 && a2.absoluteValue == Double.MAX_VALUE) || (a2 == 0.0 && a1.absoluteValue == Double.MAX_VALUE)) {
            throw x86HardwareException.FpuException(core.pc)
        }
        val res = a1 * a2
        op1.value(core, res.ieee754())
        core.fpu.pop(popCount)
    }
}