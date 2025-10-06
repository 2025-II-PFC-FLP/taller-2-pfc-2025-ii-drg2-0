## Algoritmo Conjuntos Difusos
### Definicion de la clase 

La clase `ConjuntosDifusos` modela conjuntos difusos como funciones de pertenencia de tipo `Int =>
Double` y provee operaciones basicas: pertenencia, generacion de un conj."grande", complemento, union, interseccion, inclusion y igualdad. Se usa programacion funcional:
 cada conjunto difuso es una funcion que, dado un `Int`, devuelve un grado de pertenencia en [0,1] (tipo `Double`)

## Codigo Fuente

```scala
package taller

class ConjuntosDifusos {
  type ConjDifuso = Int => Double
  def pertenece(elem: Int, s: ConjDifuso): Double = {
    s(elem)
  }

  def grande(d: Int, e: Int): ConjDifuso = {
    require(d >= 1, "d debe ser >= 1")
    require(e >= 1, "e debe ser >= 1")
    (n: Int) =>
      if (n <= 0) 0.0
      else {
        val form1 = n.toDouble / (n + d).toDouble
        val form2 = math.pow(form1, e.toDouble)
        if (form2.isNaN) 0.0 else math.max(0.0, math.min(1.0, form2))
      }
  }

  def complemento(c: ConjDifuso): ConjDifuso = {
    (n: Int) =>
      val per = c(n)
      val num1 = if (per.isNaN || per.isInfinite) 0.0 else math.max(0.0, math.min(1.0, per))
      1.0 - num1
  }

  def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (n: Int) =>
      val val1 = cd1(n)
      val val2 = cd2(n)
      math.max(val1,val2)
  }

  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (n: Int) =>
      val val1 = cd1(n)
      val val2 = cd2(n)
      math.min(val1,val2)
  }

  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    @annotation.tailrec
    def aux(n: Int): Boolean = {
      if (n > 1000) true
      else if (cd1(n) > cd2(n)) false
      else aux(n + 1)
    }
    aux(0)
  }

  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    inclusion(cd1, cd2) && inclusion(cd2, cd1)
  }
}
```

## Explicacion detalla por metodo

`type ConjDifuso = Int => Double`
- Define el alias de tipo para una funcion de membresia: un entero (elemento)
mapea a un grado de pertenencia `Double` (esperadoen [0,1])

`def pertenece(elem: Int, s: ConjDifuso): Double`
- Devuelve el grado e pertenencia del elemento `elem` en el conjunto difuso `s` 
simplemente evaluando la funcion: `s(elem)`

### Ejemplo

```Scala
val cd = (n: Int) => if (n >= 10) 1.0 else 0.0
pertenece(12, cd) // 1.0
pertenece(5, cd)  // 0.0
```

