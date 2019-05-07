package ru.inforion.lab403.kopycat.cores.arm.enums

/**
 * Created by r.valitov on 16.01.18
 */

enum class VectorTable(val exceptionNumber: Long, val IRQNumber: Int, val offset: Int){
    Reset    (1, -15, 0x4 ),
    NMI      (2, -14, 0x8 ),
    HardFault(3, -13, 0xC ),
    SVCall   (11, -5, 0x2C),
    PendSV   (14, -2, 0x38),
    SysTick  (15, -1, 0x3C),
    IRQ0     (16,  0, 0x40),
    IRQ1     (17,  1, 0x44),
    IRQ2     (18,  2, 0x48),
    IRQ3     (19,  3, 0x4C),
    IRQ4     (20,  4, 0x50),
    IRQ5     (21,  5, 0x54),
    IRQ6     (22,  6, 0x58),
    IRQ7     (23,  7, 0x5C),
    IRQ8     (24,  8, 0x60),
    IRQ9     (25,  9, 0x64),
    IRQ10    (26, 10, 0x68),
    IRQ11    (27, 11, 0x6C),
    IRQ12    (28, 12, 0x70),
    IRQ13    (29, 13, 0x74),
    IRQ14    (30, 14, 0x78),
    IRQ15    (31, 15, 0x7C),
    IRQ16    (32, 16, 0x80),
    IRQ17    (33, 17, 0x84),
    IRQ18    (34, 18, 0x88),
    IRQ19    (35, 19, 0x8C),
    IRQ20    (36, 20, 0x90),
    IRQ21    (37, 21, 0x94),
    IRQ22    (38, 22, 0x98),
    IRQ23    (39, 23, 0x9C),
    IRQ24    (40, 24, 0xA0),
    IRQ25    (41, 25, 0xA4),
    IRQ26    (42, 26, 0xA8),
    IRQ27    (43, 27, 0xAC),
    IRQ28    (44, 28, 0xB0),
    IRQ29    (45, 29, 0xB4),
    IRQ30    (46, 30, 0xB8),
    IRQ31    (47, 31, 0xBC);

    companion object {
        val COUNT: Int get() = values().size
        fun fromOffset(id: Int): VectorTable = values().first { it.offset == id }
    }
}