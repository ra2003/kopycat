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
package ru.inforion.lab403.kopycat.cores.arm.instructions.cpu.system

import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.enums.GPR
import ru.inforion.lab403.kopycat.cores.arm.enums.ProcessorMode
import ru.inforion.lab403.kopycat.cores.arm.exceptions.ARMHardwareException
import ru.inforion.lab403.kopycat.cores.arm.hardware.registers.RegisterBanking
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegisterList
import ru.inforion.lab403.kopycat.cores.base.enums.Datatype
import ru.inforion.lab403.kopycat.cores.base.like
import ru.inforion.lab403.kopycat.modules.cores.AARMCore




// LDM (User registers), see B9.3.6
class LDMur(cpu: AARMCore,
            opcode: Long,
            cond: Condition,
            val increment: Boolean,
            val wordhigher: Boolean,
            val rn: ARMRegister,
            val registers: ARMRegisterList,
            size: Int):
        AARMInstruction(cpu, Type.VOID, cond, opcode, rn, registers, size = size) {
    // TODO: not correct mnem
    override val mnem = "LDM$mcnd"

    override fun execute() {
        if (core.cpu.CurrentModeIsHyp())
            throw ARMHardwareException.Undefined
        else if (core.cpu.CurrentModeIsUserOrSystem())
            throw ARMHardwareException.Unpredictable
        else {
            val length = 4 * registers.bitCount
            var address = if (increment) rn.value(core) else rn.value(core) - length
            if (wordhigher) address += 4
            if (core.cpu.sregs.cpsr.m == ProcessorMode.fiq.id.toLong())
                TODO("Write user regs from r8 to r12")
            registers.forEachIndexed { _, reg ->
                if (reg.reg < GPR.SPMain.id)
                    reg.value(core, core.inl(address like Datatype.DWORD))
                else
                    /** Class [RegisterBanking] contains registers from r8 to lr */
                    core.cpu.banking[ProcessorMode.usr.ordinal].write(reg.reg - 8, core.inl(address like Datatype.DWORD))
                address += 4
            }
        }
    }
}