package taller

import org.scalatest.funsuite.AnyFunSuite

class ConjuntosDifusosTest extends AnyFunSuite {
  val cd = new ConjuntosDifusos
  import cd._

  // tolerancia para comparar doubles
  private val eps = 1e-6
  private def approx(a: Double, b: Double, eps: Double = 1e-6): Boolean =
    math.abs(a - b) <= eps
