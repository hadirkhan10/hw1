// See README.md for license details.

package hw1

import chisel3._
import chisel3.util._

// Note ??? will compile but not work at runtime.

/**
 * io.instWord: 32b UInt Input
 * io.opcode: 7b UInt Output
 * io.funct3: 3b UInt Output
 * io.rs1: 5b UInt Output
 * io.rd: 5b UInt Output
 * io.immSignExtended: 32b UInt Output
 */
class RiscvITypeDecoder extends Module {
    val io = IO(new Bundle {
      val instWord = Input(UInt(32.W))
      val opcode = Output(UInt(7.W))
      val funct3 = Output(UInt(3.W))
      val rs1 = Output(UInt(5.W))
      val rd = Output(UInt(5.W))
      val immSignExtended = Output(UInt(32.W))
    })

  io.opcode := io.instWord(6,0)
  io.funct3 := io.instWord(14, 12)
  io.rs1 := io.instWord(19, 15)
  io.rd := io.instWord(11, 7)
  val imm = io.instWord(31, 20)
  val msb = io.instWord(31)
  io.immSignExtended := Cat(Fill(10, msb), imm)


}


class MajorityCircuit extends Module {
    val io = IO(new Bundle {
        val a = Input(Bool())
        val b = Input(Bool())
        val c = Input(Bool())
        val out = Output(Bool())
    })
    io.out := (io.a & io.b) | (io.a & io.c) | (io.b & io.c)
}


/**
  * c0: 6-bit Int
  * c1: 6-bit Int
  * c2: 6-bit Int
  * io.enable: Bool Input
  * io.x: 6-bit UInt Input
  * io.out: ???-bit UInt Output
  */
class PolyEval(c0: Int, c1: Int, c2: Int) extends Module {
  require (c0 >= 0 && c0 < 64)
  require (c1 >= 0 && c1 < 64)
  require (c2 >= 0 && c2 < 64)
    val io = IO(new Bundle {
      val x = Input(UInt(6.W))
      val enable = Input(Bool())
      val out = Output(UInt())
    })

  when(io.enable)
  {
    io.out := c0.asUInt + c1.asUInt * io.x + c2.asUInt * (io.x * io.x)
  } .otherwise
  {
    io.out := 0.U
  }
}


/**
  * onlyAdder: Boolean
  * io.doAdd: Bool Input
  * io.real0: 5-bit SInt Input
  * io.imag0: 5-bit SInt Input
  * io.real1: 5-bit SInt Input
  * io.imag1: 5-bit SInt Input
  * io.realOut: SInt() Output
  * io.imagOut: SInt() Output
  */
class ComplexALU(onlyAdder: Boolean) extends Module {
  val io = IO(new Bundle {
      val doAdd   = Input(Bool())
      val real0   = Input(SInt(5.W))
      val imag0   = Input(SInt(5.W))
      val real1   = Input(SInt(5.W))
      val imag1   = Input(SInt(5.W))
      val realOut = Output(SInt())
      val imagOut = Output(SInt())
  })
  if (onlyAdder)
  {
    // no subtraction logic
    io.realOut := io.real0 + io.real1
    io.imagOut := io.imag0 + io.imag1
  } else
  {
    // both the addition and subtraction logic depending upon the doAdd signal
    when(io.doAdd)
    {
      io.realOut := io.real0 + io.real1
      io.imagOut := io.imag0 + io.imag1
    } .otherwise
    {
      io.realOut := io.real0 - io.real1
      io.imagOut := io.imag0 - io.imag1
    }
  }
}
