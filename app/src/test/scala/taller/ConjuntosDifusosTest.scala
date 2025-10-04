package taller

import org.scalatest.funsuite.AnyFunSuite

class ConjuntosDifusosTest extends AnyFunSuite {
  val cd = new ConjuntosDifusos
  import cd._

  // tolerancia para comparar doubles
  private val eps = 1e-6
  private def approx(a: Double, b: Double, eps: Double = 1e-6): Boolean =
    math.abs(a - b) <= eps

  // ---------- Tests para grande ----------
  test("Función grande con d=1, e=2") {
    val g = grande(1, 2)
    assert(approx(g(0), 0.0, eps))
    assert(approx(g(1), 0.25, eps))           // (1/2)^2
    assert(approx(g(2), 4.0/9.0, eps))        // (2/3)^2
    assert(approx(g(3), 9.0/16.0, eps))       // (3/4)^2
    assert(approx(g(10), 100.0/121.0, eps))   // (10/11)^2
  }

  test("Función grande con d=2, e=3") {
    val g = grande(2, 3)
    assert(approx(g(0), 0.0, eps))
    assert(approx(g(1), 1.0/27.0, eps))       // (1/3)^3
    assert(approx(g(2), 0.125, eps))          // (2/4)^3
    assert(approx(g(4), 8.0/27.0, eps))       // (4/6)^3
    assert(approx(g(20), 1000.0/1331.0, eps)) // (20/22)^3
  }

  // ---------- Tests para complemento ----------
  test("Función complemento") {
    val g = grande(1, 2)
    val c = complemento(g)
    assert(approx(c(1), 0.75, eps))    // 1 - 0.25
    assert(approx(c(2), 5.0/9.0, eps)) // 1 - 4/9
    assert(approx(c(3), 7.0/16.0, eps))
    assert(approx(c(10), 21.0/121.0, eps))
    assert(approx(c(0), 1.0, eps))     // 1 - 0
  }

  // ---------- Tests para union ----------
  test("Función union") {
    val g1 = grande(1, 2)
    val g2 = grande(2, 3)
    val u = union(g1, g2)
    assert(approx(u(0), 0.0, eps))
    assert(approx(u(1), 0.25, eps))        // max(0.25, 0.037)
    assert(approx(u(2), 0.4444, 1e-3))
    assert(approx(u(3), 0.5625, eps))
    assert(approx(u(10), 0.8264, 1e-3))
  }

  // ---------- Tests para interseccion ----------
  test("Función interseccion") {
    val g1 = grande(1, 2)
    val g2 = grande(2, 3)
    val i = interseccion(g1, g2)
    assert(approx(i(0), 0.0, eps))
    assert(approx(i(1), 1.0/27.0, eps)) // min(0.25, 0.037)
    assert(approx(i(2), 0.125, eps))
    assert(approx(i(3), 0.296296, 1e-6))
    assert(approx(i(20), 0.7513, 1e-3))
  }






