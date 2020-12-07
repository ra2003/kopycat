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
package ru.inforion.lab403.kopycat.veos.api.misc

import ru.inforion.lab403.common.extensions.hex8
import ru.inforion.lab403.kopycat.cores.base.enums.AccessAction
import ru.inforion.lab403.kopycat.cores.base.exceptions.MemoryAccessError
import kotlin.reflect.KProperty



abstract class Pointer<T>(val address: Long) {

    open inner class field(val index: Int) {
        var cache: T? = null
        operator fun getValue(thisRef: Pointer<T>, property: KProperty<*>) = cache ?: get(index).also { cache = it }
        operator fun setValue(thisRef: Pointer<T>, property: KProperty<*>, value: T) = set(index, value).also { cache = value }
    }

    override fun toString() = address.hex8 // TODO: 64-bit address

    val get get() = get(0)
    fun set(value: T) = set(0, value)

    abstract operator fun get(index: Int): T
    abstract operator fun set(index: Int, value: T)

    val isNull get() = address == 0L
    val isNotNull get() = address != 0L

    fun throwIfNull() {
        if (isNull) throw MemoryAccessError(-1, address, AccessAction.LOAD, "VEOS pointer is null")
    }
}

