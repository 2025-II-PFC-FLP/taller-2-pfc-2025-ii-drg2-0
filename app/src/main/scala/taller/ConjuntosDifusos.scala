package taller

class ConjuntosDifusos {
  type ConjDifuso = Int => Double
  def pertenece(elem: Int, s: ConjDifuso): Double = {
    s(elem)
  }
  def grande(d: Int, e: Int): ConjDifuso = {
// Declara la función grande que recibe dos enteros d y e y devuelve un ConjDifuso
    require(d >= 1, "d debe ser >= 1")
    require(e >= 1, "e debe ser >= 1")
// grande retorna una función que toma un Int y devuelve un Double.
    (n: Int) =>
      // Quitamos los casos negativos ya que no son números grandes
      if (n <= 0) 0.0
      else {
        val form1 = n.toDouble / (n + d).toDouble // n/(n+d)
        val form2 = math.pow(form1, e.toDouble) // (n/(n+d))^e
        // Clamp por intervalo [0,1] evita retornar ligeras desviaciones por redondeo
        if (form2.isNaN) 0.0 else math.max(0.0, math.min(1.0, form2))
      }
  }
  def complemento(c: ConjDifuso): ConjDifuso = {
    // Implementacion de la funcion complemento
  }
  4
  def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    // Implementacion de la funcion union
  }
  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    // Implementacion de la funcion interseccion
  }
  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    // Implementacion de la funcion inclusion
  }
  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    // Implementacion de la funcion igualdad
  }
}
