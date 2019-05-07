package ru.inforion.lab403.kopycat.cores.x86.hardware.registers

import ru.inforion.lab403.kopycat.cores.base.abstracts.ARegistersBank
import ru.inforion.lab403.kopycat.cores.x86.enums.FR
import ru.inforion.lab403.kopycat.cores.x86.operands.x86Register
import ru.inforion.lab403.kopycat.modules.cores.x86Core

/**
 * Created by davydov_vn on 08.09.16.
 */

class FLBank(core: x86Core) : ARegistersBank<x86Core, FR>(core, FR.values(), bits = 32) {
    override val name: String = "Flags Register"

    var eflags: Long
        get() = x86Register.eflags.value(core)
        set(value) = x86Register.eflags.value(core, value)

    var iopl: Int
        get() = x86Register.eflags.iopl(core)
        set(value) = x86Register.eflags.iopl(core, value)

    var ac: Boolean
        get() = x86Register.eflags.ac(core)
        set(value) = x86Register.eflags.ac(core, value)
    var rf: Boolean
        get() = x86Register.eflags.rf(core)
        set(value) = x86Register.eflags.rf(core, value)
    var vm: Boolean
        get() = x86Register.eflags.vm(core)
        set(value) = x86Register.eflags.vm(core, value)
    var vif: Boolean
        get() = x86Register.eflags.vif(core)
        set(value) = x86Register.eflags.vif(core, value)
    var vip: Boolean
        get() = x86Register.eflags.vip(core)
        set(value) = x86Register.eflags.vip(core, value)
    var id: Boolean
        get() = x86Register.eflags.id(core)
        set(value) = x86Register.eflags.id(core, value)


    var cf: Boolean
        get() = x86Register.eflags.cf(core)
        set(value) = x86Register.eflags.cf(core, value)
    var pf: Boolean
        get() = x86Register.eflags.pf(core)
        set(value) = x86Register.eflags.pf(core, value)
    var af: Boolean
        get() = x86Register.eflags.af(core)
        set(value) = x86Register.eflags.af(core, value)
    var zf: Boolean
        get() = x86Register.eflags.zf(core)
        set(value) = x86Register.eflags.zf(core, value)
    var sf: Boolean
        get() = x86Register.eflags.sf(core)
        set(value) = x86Register.eflags.sf(core, value)
    var tf: Boolean
        get() = x86Register.eflags.tf(core)
        set(value) = x86Register.eflags.tf(core, value)
    var ifq: Boolean
        get() = x86Register.eflags.ifq(core)
        set(value) = x86Register.eflags.ifq(core, value)
    var df: Boolean
        get() = x86Register.eflags.df(core)
        set(value) = x86Register.eflags.df(core, value)
    var of: Boolean
        get() = x86Register.eflags.of(core)
        set(value) = x86Register.eflags.of(core, value)
}