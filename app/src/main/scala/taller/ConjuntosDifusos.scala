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
    (n: Int) => //Función anónima lamda que recibe int
      // Quitamos los casos negativos ya que no son números grandes
      if (n <= 0) 0.0 // Se devuelve 0.0 para n <= 0
      else {
        val form1 = n.toDouble / (n + d).toDouble // n/(n+d)
        val form2 = math.pow(form1, e.toDouble) // (n/(n+d))^e
        // Clamp por intervalo [0,1] evita retornar ligeras desviaciones por redondeo (Limites de intervalos maximo y minimo)
        if (form2.isNaN) 0.0 else math.max(0.0, math.min(1.0, form2))
      }
  }
  def complemento(c: ConjDifuso): ConjDifuso = {
    (n: Int) => //funcion anonima que 'complemento' retorna ya que n toma un entero y se calcula el grado de pertenencia
      val per = c(n) //grado de pertenencia del elemento n al conjunto difuso c
      // per podrá ser cualquier doble
      val num1 = if (per.isNaN || per.isInfinite) 0.0 else math.max(0.0, math.min(1.0, per)) // Clamp a intervalo [0,1] (Limites de intervalos maximo y minimo)
      1.0 - num1 // complemento se calcula como 1 - grado de pertenencia
  }

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
