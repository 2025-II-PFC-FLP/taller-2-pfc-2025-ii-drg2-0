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
    (n: Int) => //Funcion anonima que 'union' retorna
      //Estos 2 parametros cd1 y cd2 cada uno es un ConjDifuso y devuelven otro ConjDifuso
      val val1 = cd1(n)  //Si cd1(n) produce NaN o fuera del intervalo [0,1] val1 tomará ese valor (Por eso no hay clamp)
      val val2 = cd2(n)
      math.max(val1,val2) //Calcula y devuelve el máximo entre los 2
      //el resultado puede ser NaN
  }
  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (n: Int) => //Funcion anonima que 'interseccion' (toma n)
      val val1 = cd1(n) //Grado en el primer conjunto
      val val2 = cd2(n) //Grado en el segundo conjunto
      math.min(val1,val2) //Según la deficion el grado de pertenencia del cruce es el menor de los 2.
  }
  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    // cd1 y cd2 son Int => Double que devuelven un Boolean, esto indica si cd1 es subconjunto de cd2
    @annotation.tailrec // Este verifica que la funcion recursiva siguiente sea recursion de cola, si no lo es dará error
    def aux(n: Int): Boolean = { //Funcion interna toma entero y devuelve Booleanm
      if (n > 1000) true //Caso base si todos los n están comprobados usamos 1000 como limite y no hubo fallo, devolvemos true
        //Es decir cd1 es subconjunto de cd2 para todos los n en este rango
      else if (cd1(n) > cd2(n)) false //Si en algun caso el grado de pertenencia de cd1 es mayor que cd2, cd1 no es subconjunto de cd2 y esta inclusion falla
      else aux(n + 1) //Si ninguna condicion se cumple superando el rango y que cd1 <= cd2, avanzamos al siguiente n, llamada en posicion de cola. Por eso el @teilrec es valido
    }
    aux(0) //Verificacion desde 0 hasta 1000
  }
  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    inclusion(cd1, cd2) && inclusion(cd2, cd1) //Condicion para saber si como conjuntos difusos son iguales cd1 y cd2.
    // Son iguales sin cd1 es subconjunto de cd2 y cd2 es subconjunto de cd1. Se deben cumplir las 2 condiciones.
    //Si no devuelve false y no evalua más.
  }
}
