package ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.thumb32

import ru.inforion.lab403.common.extensions.asLong
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.common.extensions.signext
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.exceptions.ARMHardwareException.Undefined
import ru.inforion.lab403.kopycat.cores.arm.exceptions.ARMHardwareException.Unpredictable
import ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.decoders.ADecoder
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.base.operands.Immediate
import ru.inforion.lab403.kopycat.modules.cores.AARMCore
import ru.inforion.lab403.kopycat.modules.cores.AARMCore.InstructionSet.THUMB_EE

object Thumb32blxImm {
    class T1(cpu: AARMCore,
             private val constructor: (
                     cpu: AARMCore,
                     opcode: Long,
                     cond: Condition,
                     imm32: Immediate<AARMCore>,
                     targetInstrSet: AARMCore.InstructionSet,
                     size: Int) -> AARMInstruction) : ADecoder<AARMInstruction>(cpu) {
        override fun decode(data: Long): AARMInstruction {
            val S = data[26]
            val J1 = data[13]
            val J2 = data[11]
            val I1 = (J1 xor S).inv()[0]
            val I2 = (J2 xor S).inv()[0]
            val imm10 = data[25..16]
            val imm11 = data[10..0]
            val imm32 = Immediate<AARMCore>(signext((S shl 24) + (I1 shl 23) + (I2 shl 22) + (imm10 shl 12) + (imm11 shl 1), 25).asLong)
            val targetInstrSet = core.cpu.CurrentInstrSet()
            if(core.cpu.InITBlock() && !core.cpu.LastInITBlock()) throw Unpredictable
            return constructor(core, data, Condition.AL, imm32, targetInstrSet, 4)
        }
    }
    class T2(cpu: AARMCore,
             private val constructor: (
                     cpu: AARMCore,
                     opcode: Long,
                     cond: Condition,
                     imm32: Immediate<AARMCore>,
                     targetInstrSet: AARMCore.InstructionSet,
                     size: Int) -> AARMInstruction) : ADecoder<AARMInstruction>(cpu) {
        override fun decode(data: Long): AARMInstruction {
            val H = data[0]
            if(core.cpu.CurrentInstrSet() == THUMB_EE || H == 1L) throw Undefined
            val S = data[26]
            val J1 = data[13]
            val J2 = data[11]
            val I1 = (J1 xor S).inv()
            val I2 = (J2 xor S).inv()
            val imm10L = data[10..1]
            val imm10H = data[25..16]
            val imm32 = Immediate<AARMCore>((S shl 24) + (I1 shl 23) + (I2 shl 22) + (imm10H shl 12) + (imm10L shl 2))
            val targetInstrSet = core.cpu.CurrentInstrSet()
            if(core.cpu.InITBlock() && !core.cpu.LastInITBlock()) throw Unpredictable
            return constructor(core, data, Condition.AL, imm32, targetInstrSet, 4)
        }
    }
}