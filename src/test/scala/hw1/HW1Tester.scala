// See README.md for license details.

package hw1

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class HW1Tester extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "RiscvITypeDecoder"
  it should  "correctly decode instructions" in {
    test(new RiscvITypeDecoder) { c =>
      // addi x1, x2, 1
      c.io.instWord.poke("h00110093".U(32.W))
      c.io.opcode.expect("b010011".U)
      c.io.funct3.expect("b000".U)
      c.io.rs1.expect("b00010".U)
      c.io.rd.expect("b00001".U)
      c.io.immSignExtended.expect("h1".U(32.W))
    }
  }

  // See src/test/scala/hw1/MajorityCircuitTester.scala for Problem2

  behavior of "PolyEval"
  it should "correctly calculate out" in {
    val c0 = 63
    val x = 63
    test(new PolyEval(c0, c0, c0)) { dut =>
      dut.io.enable.poke(true.B)
      dut.io.x.poke(x.U)
      dut.io.out.expect(c0  + c0 * x + c0 * (x*x))
      dut.io.enable.poke(false.B)
      dut.io.out.expect(0.U)
    }
  }

  behavior of "ComplexALU"
  it should "correctly calculate realOut onlyAdd=true" in {
    test(new ComplexALU(onlyAdder=true)) { dut =>
      dut.io.doAdd.poke(false.B)   // this is a don't care and should not have any impact
      dut.io.real0.poke(2.S)
      dut.io.imag0.poke(-4.S)
      dut.io.real1.poke(7.S)
      dut.io.imag1.poke(-11.S)

      dut.io.realOut.expect(9.S)
    }
  }
  it should "correctly calculate realOut onlyAdd=false" in {
    test(new ComplexALU(onlyAdder = false)) { dut =>
      dut.io.doAdd.poke(true.B)   // this signal matters here
      dut.io.real0.poke(2.S)
      dut.io.imag0.poke(-4.S)
      dut.io.real1.poke(7.S)
      dut.io.imag1.poke(-11.S)

      dut.io.realOut.expect(9.S)

      dut.io.doAdd.poke(false.B)

      dut.io.realOut.expect(-5.S)
    }
  }
  it should "correctly calculate imagOut onlyAdd=true" in {
    test(new ComplexALU(onlyAdder = true)) { dut =>
      dut.io.doAdd.poke(false.B)   // this is a don't care and should not have any impact
      dut.io.real0.poke(2.S)
      dut.io.imag0.poke(-4.S)
      dut.io.real1.poke(7.S)
      dut.io.imag1.poke(-11.S)

      dut.io.imagOut.expect(-15.S)
    }
  }
  it should "correctly calculate imagOut onlyAdd=false" in {
    test(new ComplexALU(onlyAdder = false)) { dut =>
      dut.io.doAdd.poke(true.B)   // this signal matters here
      dut.io.real0.poke(2.S)
      dut.io.imag0.poke(-4.S)
      dut.io.real1.poke(7.S)
      dut.io.imag1.poke(-11.S)

      dut.io.imagOut.expect(-15.S)

      dut.io.doAdd.poke(false.B)

      dut.io.imagOut.expect(7.S)
    }
  }
}
