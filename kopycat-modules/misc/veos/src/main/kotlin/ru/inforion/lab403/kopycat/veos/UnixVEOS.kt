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
package ru.inforion.lab403.kopycat.veos

import ru.inforion.lab403.kopycat.cores.base.AGenericCore
import ru.inforion.lab403.kopycat.cores.base.common.Module
import ru.inforion.lab403.kopycat.modules.BUS32
import ru.inforion.lab403.kopycat.modules.memory.VirtualMemory
import ru.inforion.lab403.kopycat.veos.api.impl.PosixAPI
import ru.inforion.lab403.kopycat.veos.api.impl.StdioAPI
import ru.inforion.lab403.kopycat.veos.api.impl.StdlibAPI
import ru.inforion.lab403.kopycat.veos.api.impl.StringAPI
import ru.inforion.lab403.kopycat.veos.kernel.Process
import ru.inforion.lab403.kopycat.veos.loader.UnixOsLoader
import ru.inforion.lab403.kopycat.veos.ports.posix.PosixThread

class UnixVEOS<C: AGenericCore>(parent: Module, name: String, bus: Long = BUS32): VEOS<C>(parent, name, bus) {
    override val loader = UnixOsLoader(this)

    override fun initialize(): Boolean {
        if (!super.initialize())
            return false
        addApi(StdioAPI(this),
                StdlibAPI(this),
                StringAPI(this),
                PosixAPI(this))
        return true
    }

    override fun newProcess(memory: VirtualMemory) = PosixThread(sys, processIds.allocate(), memory)
}