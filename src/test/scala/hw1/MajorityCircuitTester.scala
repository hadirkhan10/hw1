// See README.md for license details.

package hw1

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class MajorityCircuitTester extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "MajorityCircuit"
  it should "correctly pass tests on all 8 possible inputs" in {
    test(new MajorityCircuit) { dut =>
      // FILL IN HERE
      for (a <- 0 until 2) {
        for (b <- 0 until 2) {
          for (c <- 0 until 2) {
            dut.io.a.poke(a.U)
            dut.io.b.poke(b.U)
            dut.io.c.poke(c.U)
            dut.io.out.expect((a & b) | (a & c) | (b & c))

          }
        }
      }

    }
  }
}
