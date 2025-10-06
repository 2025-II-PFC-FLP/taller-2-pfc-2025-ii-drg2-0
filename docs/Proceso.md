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
`def grande (d: Int, e: Int): ConjDifuso `
- Crea un conjunto difuso que modela "ser grande" parametrizando por `d` y `e`
- Precondiciones: `d >= 1` y `e >= 1` (verificadascon `requiere`)
- Para un `n` dado:-
   - Si `n <= 0` → retorna 0.0
   - Calcula `form1 = n/(n+d)` (en `Double`)
   - Calcula `form2 = (form1)^e`
   - Aplica clamp a [0,1] y maneja `NaN` devolviendo `0.0` si corresponde 

## Formula (LaTeX):

$$
\mu_{\text{grande}}(n) =
\begin{cases}
0, & n \le 0 \\[4pt]
\text{clamp}\left[\left(\dfrac{n}{n+d}\right)^e, 0, 1\right], & n > 0
\end{cases}
$$


### Ejemplo
```Scala
val g = new ConjuntosDifusos().grande(5, 2)
g(0)   // 0.0
g(5)   // (5/(5+5))^2 = (0.5)^2 = 0.25
g(10)  // (10/(10+5))^2 = (2/3)^2 ≈ 0.444...
```
`def complemento(c: ConjDifuso): ConjDifuso`
- Devuelve la funcion complemento 
$$
  \mu_{\neg C}(n) = 1 - \mu_C(n)
$$
- Antes de restar, aplica clamp a [0,1] y corrige `NaN/Infinite`

### Ejemplo 
```Scala
val c = (n:Int) => if (n>=10) 0.8 else 0.2
val comp = new ConjuntosDifusos().complemento(c)
comp(12) // 1 - 0.8 = 0.2
comp(5)  // 1 - 0.2 = 0.8
```
`def union(cd1: ConjDifuso, cd2: ConjDifuso, cd3: ConjDifuso): ConjDifuso`
- Union difusa estandar por maximo: 
$$
  \mu_{A \cup B}(n) = \max(\mu_A(n), \mu_B(n))
$$

### Ejemplo
```Scala
val i = new ConjuntosDifusos().interseccion(a,b)
i(6) // min(0.7,0.2) = 0.2
```
`def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean`
- Determina si `cd1` esta incluido en `cd2` es decir, para todo `n` en el dominio 
considerado $$
  \mu_{A \cap B}(n) = \min(\mu_A(n), \mu_B(n))
  $$
- Implementacion practica: recorre `n` desde `0` hasta `1000` (limite fijado) con una
funcion recursiva de cola `aux`
    - Si encuentra `cd1(n) > cd2(n)` devuelve `falsa`
    - Si supera `1000` sin violaciones, devuelve `true`
- Uso de `@annotation.tailrec` garantiza que `aux` sea recursiva de cola (si no lo fuera, el compilador fallaria)

### Ejemplo conceptual 
```Scala
val cd1 = (n:Int) => if (n>5) 0.5 else 0.1
val cd2 = (n:Int) => if (n>5) 0.6 else 0.2
inclusion(cd1, cd2) // true (en el rango 0..1000)
```
### Observaciones
- El limite `1000` es una decision practica (evita infinitas comprobaciones en ℤ)
- Si las funciones devuelven `NaN`, la comparacion puede comportarse de forma inesperada

`def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean`
- Define igualdad como inclusion mutua:
```Scala
inclusion(cd1, cd2) && inclusion(cd2, cd1)
```
- Es una comparacion en el dominio discretizado `0..1000`
### Ejemplo uso completo
```Scala
val cds = new ConjuntosDifusos()

val g1 = cds.grande(5, 2)        // conj. "grande" parametrizado
val g2 = cds.grande(3, 1)

println(cds.pertenece(10, g1))  // grado de pertenencia en g1
println(cds.pertenece(10, g2))

val compG1 = cds.complemento(g1)
println(compG1(10))             // 1 - mu_g1(10)

val u = cds.union(g1, g2)
val inter = cds.interseccion(g1, g2)

println(cds.inclusion(g2, g1))  // boolean
println(cds.igualdad(g1, g2))   // boolean
```
### Ejecucion paso a paso (ejemplo: grande(5,2)(n=10))
1. `grande(5,2)` crea la funcion anonima 
2. Evaluamos con `n=10`:
    - `form1 = 10 / (10 + 5) = 10/15 = 2/3 ≈ 0.6667`
    - `form2 = (2/3)^2 ≈ 0.4444`
    - Clamp a ([0,1]→0.4444` (ya está en rango)
3. Resultado final:≈ 0.4444

## Diagrama en llamadas (Mermaid)
### Diagrama: evaluacion de `grande(d,e)` y `pertenece`
``` mermaid
sequenceDiagram
participant Caller as Código cliente
participant CDS as ConjuntosDifusos
participant Func as función mu(n)

    Caller->>CDS: grande(5,2)
    CDS-->>Caller: retorna func(n)
    Caller->>Func: func(10)
    Func-->>Caller: calcula form1, form2 y retorna 0.4444
```
### Diagrama: comprobación inclusion(cd1, cd2) (recursión de cola)
``` mermaid
sequenceDiagram
    participant Main as inclusion(cd1,cd2)
    participant Aux as aux(0..1000)
    Main->>Aux: aux(0)
    Aux->>Aux: comprueba cd1(0) <= cd2(0) ? aux(1) : return false
    Aux->>Aux: ...
    Aux->>Main: si n>1000 return true
```
## Notacion matematica 
- Función de pertenencia:$$\mu_C: \mathbb{Z} \to [0,1]$$
- Complemento: $$\mu_{\neg C}(n) = 1 - \mu_C(n)$$
- Unión: $$\mu_{A\cup B}(n) = \max(\mu_A(n), \mu_B(n))$$
- Intersección: $$\mu_{A\cap B}(n) = \min(\mu_A(n), \mu_B(n))$$
- Inclusión (práctica en 0..1000): $$(A \subseteq B \iff \forall n \in \{0,\dots,1000\},\ \mu_A(n) \le \mu_B(n)$$
---
#  Pruebas del Algoritmo de Conjuntos Difusos

A continuación se presentan los **tests unitarios** realizados al módulo `ConjuntosDifusos`, desarrollados con **ScalaTest**.  
Cada grupo de pruebas valida una de las operaciones definidas para los conjuntos difusos, comprobando la corrección de los resultados numéricos dentro de una tolerancia `ε = 1×10⁻⁶`.

##  1. Función `grande(d, e)`

Evalúa el comportamiento del grado de pertenencia de un conjunto difuso que modela la noción de “ser grande”, con parámetros `d` y `e`.

Se espera que:
- Los valores crezcan progresivamente.
- Cumplan la fórmula de definición de la función de pertenencia:

$$
\mu_{grande}(n) = \left( \frac{n}{n + d} \right)^e
$$

###  Test 1: `grande(1, 2)`
Verifica varios valores de \( n \) para confirmar la forma cuadrática del crecimiento:

| n | Esperado | Cálculo |
|:-:|:-:|:-:|
| 0 | 0.0 | (0/1)² |
| 1 | 0.25 | (1/2)² |
| 2 | 0.4444 | (2/3)² |
| 3 | 0.5625 | (3/4)² |
| 10 | 0.8264 | (10/11)² |

###  Test 2: `grande(2, 3)`
Evalúa la versión cúbica de la función:

| n | Esperado | Cálculo |
|:-:|:-:|:-:|
| 0 | 0.0 | (0/2)³ |
| 1 | 0.037 | (1/3)³ |
| 2 | 0.125 | (2/4)³ |
| 4 | 0.296 | (4/6)³ |
| 20 | 0.751 | (20/22)³ |



## 2. Función `complemento`

Comprueba la correcta implementación de:

$$
\mu_{\neg C}(n) = 1 - \mu_C(n)
$$

Se espera que los valores sean **inversos** a los de la función `grande`.  
Ejemplo: si \( \mu_C(2) = 4/9 \), entonces \( \mu_{\neg C}(2) = 5/9 \).

| n | Resultado esperado | Justificación |
|:-:|:-:|:-:|
| 1 | 0.75 | 1 − 0.25 |
| 2 | 0.555... | 1 − 4/9 |
| 3 | 0.4375 | 1 − 9/16 |
| 10 | 0.1736 | 1 − 100/121 |
| 0 | 1.0 | 1 − 0 |



## 3. Función `union`

Evalúa que la unión cumpla:

$$
\mu_{A \cup B}(n) = \max(\mu_A(n), \mu_B(n))
$$

El test utiliza los conjuntos `g1 = grande(1, 2)` y `g2 = grande(2, 3)`.

| n | Resultado esperado | Cálculo |
|:-:|:-:|:-:|
| 0 | 0.0 | max(0, 0) |
| 1 | 0.25 | max(0.25, 0.037) |
| 2 | 0.444 | max(0.444, 0.125) |
| 3 | 0.562 | max(0.562, 0.216) |
| 10 | 0.826 | max(0.826, 0.751) |



## 4. Función `interseccion`

Comprueba la implementación de:

$$
\mu_{A \cap B}(n) = \min(\mu_A(n), \mu_B(n))
$$

| n | Resultado esperado | Cálculo |
|:-:|:-:|:-:|
| 0 | 0.0 | min(0, 0) |
| 1 | 0.037 | min(0.25, 0.037) |
| 2 | 0.125 | min(0.444, 0.125) |
| 3 | 0.216 | min(0.562, 0.216) |
| 20 | 0.751 | min(0.826, 0.751) |



## 5. Función `inclusion`

Evalúa el cumplimiento de la propiedad de inclusión difusa:

$$
A \subseteq B \iff \forall n,\ \mu_A(n) \le \mu_B(n)
$$

Casos evaluados:
- `g1` **no** está incluido en `g2`.
- `g2` **sí** está incluido en `g1`.
- Un conjunto nulo (todo 0) siempre está incluido en cualquier otro.
- Un conjunto total (todo 1) nunca está incluido en uno menor.

---

## 6. Función `igualdad`

Verifica que dos conjuntos sean iguales si y solo si:

$$
A = B \iff \forall n,\ \mu_A(n) = \mu_B(n)
$$

Casos probados:
- `g1` y `g3` (idénticos) → **iguales** 
- `g1` y `g2` (distintos) → **no iguales** 
- `zero` y `zero` → **iguales** 
- `one` y `one` → **iguales** 
- `zero` y `one` → **no iguales** 



## Conclusión

Los tests validan que cada operación difusa respeta las propiedades matemáticas de los conjuntos difusos:
- Complemento → inverso al conjunto original.
- Unión/Intersección → siguen las leyes de **máximo y mínimo**.
- Inclusión e igualdad → coherentes con las definiciones de pertenencia.

La implementación demuestra **consistencia y precisión numérica** con un margen de error menor a `10⁻⁶`.
