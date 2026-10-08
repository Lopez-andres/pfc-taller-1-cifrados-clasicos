## Punto 3: Conteo de frecuencias con recursión de cola
Sea $F: Mensaje -> Frecuencias$

- F(m): Función que representa el resultado correcto que debe de devolver ´frecuencias´ 
  para un mensaje 'm', según la especificación. Como tal es lo que debería de devolver según la definición matematica


La función recibe un mensaje y devuelve una lista con cada letra minúscula que aparezca dentro de el y
la cantidad de veces que aparece cada letra, el resultado debe de estár ordenado de mayor a menor frecuencia, 
y que cuando 2 o más letras tengan la misma frecuencia, debe utilizarse el orden alfabético.

Se muestra que: Para cualquier mensaje de 'm' el programa $P_F(m)$ debe de dar el mismo resultado que 
la función $F$

$$ \forall m \in \text{Mensaje} : P_F(m) = F(m) $$

- $P_F(m)$: Resultado que devuelve el programa de ´frecuencias´ con el mensaje ´m.´.

La función 'frecuencias' como se sabe

- ´calcularLetras´: Que cuenta cuantas veces aparece una letra determinada
- 'examinarLetra': Que revisa las letras desde 'a' hasta la 'z' y construye la lista de frecuencias

### 1. Corrección de `calcularLetras´

Sea $C(m, l, t)$

La función que recibe:
- $m$: La parte del mensaje que todavía no se ha recorrido
- $l$: La letra que se desea contar
- $t$: La cantidad de apariciones encontradas hasta el momento

Como tal el objetivo es demostrar que ´calcularLetras´ si devuelva la cantidad total de 
apariciones de la letra $l$.

La propiedad que se mantiene durante la ejecución es $ t $,
porque contiene la cantidad de apariciones de la letra $l$ encontrada en el mensaje 
que ya fue recorrido. 

La demostración se realiza analizando 2 casos posibles de 'mensajeSobra' :
cuando está vacío y cuando todavía contiene letras.

### Caso base: mensajeSobra = ""

Cuando el mensaje restante está vacío la función ejecuta
```scala
if (mensajeSobra.isEmpty){
  totalLetra
}
```
- 'mensajeSobra': El mensaje que falta por recorrer
- '.isEmpty': Es el que pregunta si no queda nada en 'mensajeSobra'

Por lo tanto, $C("", l, t) = t$, como ya no quedan caracteres (letras) por revisar, 
el valor de $t$ ya tiene el total de veces que apareció la letra $l$ en el mensaje.

Entonces cuando ya no quedan letras por revisar, la función devuelve correctamente
el total contado.
 

### Caso inducción: Donde hay letras que revisar

Sea $mensajeSobra = c + r $ donde

- $c$ : Es el primer carácter
- $r$ : Es el resto de mensaje

Entonces se analizan 2 casos:


#### Primer caso: c = l Se encuentra una aparición de la letra buscada

Si el primer carácter coincide con la letra buscada: 'c = l' 
```scala
else if (mensajeSobra.head == letra) {...}
```
- 'c': Es el carácter actual de 'mensajeSobra'
- '.head': Es el que toma el primer carácter de 'mensajeSobra'

La función realiza una llamada

```scala
calcularLetras(mensajeSobra.tail, letra, totalLetra + 1)
```
- '.tail': Devuelve todo lo que queda del mensaje después de quitar
  el primer carácter

$ C(c + r, l, t) -> C(r, l, t +1)$
(Si la letra actual es la que estamos buscando, se suma 1 al contador,
y se continúa revisando el resto del mensaje)

Como se encontró la letra $l$, aumentamos el contador en 1, luego la función continúa revisando
el resto del mensaje para seguir contando cuantas veces aparece esta, por tanto
$ C(c + r, l, t)$ devuelve correctamente el número total de apariciones de $l$.


#### Segundo caso: c \neq l Cuando el carácter no coincide con la letra buscada

Si el primer carácter no es igual a la letra que se busca $c \neq l$, 
entonces la función realiza:

```scala
calcularLetras(mensajeSobra.tail, letra, totalLetra)
```
Es decir $ C(c + r, l, t) -> C(r, l, t)$
(EL carácter actual $c$ no coincide con la letra $l$ buscada. Entonces se ignora está, 
el contador no cambia y se sigue revisando el resto del mensaje con el mismo valor que está en el contador)

Teniendo esto en cuenta se puede decir que el resultado del caso es correcto


#### Terminación de 'calcularLetra'

En cada llamada recursiva se usa:
```scala
mensajeSobra.tail
```

Por lo tanto, el tamaño del mensaje restante disminuye -1:
$ |m_{i+1}| = |m_i| - 1 $ 
(Donde cada llamada recursiva queda -1 letra menos por revisar)

- $|m_{i+1}|$: Cantidad de letras que están en la siguiente llamada
- $|m_i|$: Cantidad de caracteres que quedan en la llamada actual
- $ - 1 $: Quita la letra porque ya fue revisada

Ejemplo: 
Antes casa = 4 letras, después asa = 3 letras
3 = 4 - 1

Así la longitud sigue la secuencia:
$n, n - 1, n - 2, ..., 0 $

- $n$: Cantidad inicial de letras
- $n - 1$: Disminución de 1 letra (-1)
- $n - 2$: Disminución de 1 letra (-1)
- $.....$: Continuación del proceso 
- $0$: No quedan más letras por revisar

Cuando llega a 0 es que alcanzó el caso base, entonces 'calcularLetras' siempre termina
quedando la llamada recursiva como última operación de la función, porque se usa
recursión de cola.

### 2. Corrección de 'examinarLetra'
Sea $E(l, g)$ la función que examina las letras desde $l$ hasta la 'z' donde:
- $l$: Es la letra revisada
- $g$: Guarda lo que se ha contado hasta el momento

Su llamada inicial es:
```scala
examinarLetra('a', List())
```
Por lo tanto, inicialmente es $g = []$ (g está vacío) y todavía no se ha examinado las letras

- $g$: Lista de letras ya examinadas que aparecen en el mensaje junto con su frecuencia 

#### Caso base
Ocurre cuando:
```scala
if (letra > 'z')
```
Es decir cuando $l > z$ 
(la letra 'l' ya paso después de la letra 'z' en el alfabeto)

En ese momento se han examinado todas las letras minúsculas desde la 'a' hasta la 'z',
por lo tanto, 'guardar' contiene todas las letras minúsculas que aparecen en el mensaje 
junto con la frecuencia. 

Entonces la función que hace el ordenamiento:
```scala
guardar.sortWith((a,b) =>
  if (a._2 == b._2){
    a._1 < b._1
  }
  else{
    a._2 > b._2
  }
)
```
- 'a._1': Representa la letra del primer elemento
- 'a._2': Representa su frecuencia
- 'b._1': Representa la letra del segundo elemento
- 'b._2': Representa su frecuencia
- 'sortWith': Ordena una lista usando condiciones personalizadas
- 'a._2 == b._2 ': Compara si las frecuencias son iguales
  
Cada elemento tiene la forma: $(letra, frecuencia)$
- Verifica si ambas frecuencias son iguales se usa: $ a._2 == b._2 $
- Si las frecuencias son iguales comparan las letras y se ordenan alfabéticamente
  y se usa: $ a._1 < b._1 $ 
- Si las frecuencias son diferentes se organiza de mayor a menor frecuencia
  y se usa: $ a._2 > b._2 $

Ejemplo:
```scala
List(('a', 2), ('c', 1), ('s', 1))
```
Entonces 'a' queda de primero porque tiene una frecuencia de 2.
Entre 'c' y 's' hay un empate: $1 = 1$, 
por lo que se compara $c < s$ y en el abecedario 'c' está antes de 's',
por eso 'c' queda antes que 's'

### Caso recursivo

Para cada letra $l$ se calcula
```scala
val totalLetra: Int  = calcularLetras(m, letra, 0)
```
- 'totalLetra' = número de apariciones de 'l' en 'm'

Como se demostró que calcularLetras está bien implementada entonces
se analizaron 2 casos

#### Primer caso: 'totalLetra > 0' Veces que apareció una letra
Si 'totalLetra > 0' la letra aparece al menos una vez en el mensaje,
por lo tanto, se agrega $(l, totalLetra)$ a la lista 'guardar'.

En el programa toma la lista 'guardar' y la agrega al final
```scala
val listaNueva: Frecuencias = guardar :+ (letra, totalLetra)
```
Ejemplo: ('a', 2)

Después de llamar recursivamente a la siguiente letra
```scala
examinarLetra((letra.toInt + 1).toChar, listaNueva)
```
- 'listaNueva': Contiene todo lo que ya encontró, guardando las letras que revisó y
  las veces que apareció

Ejemplo: 
a -> b -> c -> d -> ... -> 'z'
(Pasa a la siguiente letra)

Manteniendo las frecuencias correctas implementadas hasta el momento


### Segundo caso: 'totalLetra = 0' Letra que no aparece en el mensaje
Si 'totalLetra = 0', significa que la letra no aparece en el mensaje,
por esta razón no se agrega al resultado, entonces la función continúa con:
```scala
examinarLetra((letra.toInt + 1).toChar, guardar)
```
- 'toInt': Convierte el carácter en valor numérico
- 'toChar': Convierte el número en carácter
La lista permanece sin cambios y se pasa a la siguiente letra

Ejemplo: Si se está examinando la letra 'b' en el mensaje de 'casa'
```scala
calcularLetras("casa", 'b', 0)
```
El resultado es: 0 porque esta no aparece en el mensaje


### Encadenamientos de los llamados
Los llamados 'examinarLetra' avanza de la siguiente manera:

$'a' -> 'b' -> 'c' -> 'd' -> .... -> 'z'$

En cada llamada se examina exactamente una letra, cuando supera la letra z, 
es que alcanzó el caso base; por lo tanto, se examinan las letras minúsculas del alfabeto en inglés.

### Terminación de 'examinarLetra'
En cada llamada recursiva se avanza a la siguiente letra mediante:
```scala
(letra.toInt + 1).toChar
```
Ejemplo:
$'a' -> 'b' -> 'c' -> 'd' -> .... -> 'z'$

Como el recorrido comienza en 'a' y en cada llamada se avanza exactamente una letra,
después de examinar 'z' se obtiene un carácter mayor a este, en ese momento se
cumple el caso base:
```scala
if(letra > 'z')
```
Por lo tanto, examinarLetra siempre va a terminar.

## 3. Corrección del ordenamiento

Después de que esté construida la lista de las frecuencias,
la función utilizada es:
```scala
guardar.sortWith((a,b) =>
  if (a._2 == b._2 ){
    a._1 < b._1
  }
  else{
    a._2 > b._2
  }
)
```
Sean 2 elementos de la lista $(l1, f1)$ y $(l2,f2)$ 
donde $l$ representa la letra y $f$ su frecuencia

### Primer caso: 'f_1\neq f_2' Las 2 letras tienen frecuencias diferentes
Si las frecuencias son diferentes, la comparación usada es: $f1 > f2$

Ejemplo:
(a,2),  (c,1)
f_1 = 2   f_2=1
f_1 \neq f_2
Porque  2 > 1

Por lo tanto, la letra con la mayor frecuencia aparece primero, Así la lista queda
ordenada de mayor a menor

### Segundo caso = 'f1 = f2' Las letras aparecen en la misma cantidad de veces
Si tienen la misma frecuencia 'f1 = f2', ya no se puede ordenar por la cantidad de frecuencia,
entonces se usa el orden alfabético para decidir quien va primero.

La función para comparar las letras $l_1 < l_2$

Ejemplo:
```scala
frecuencias("casa")
```
Se obtiene los siguientes conteos:
f(a) = 2
f(c) = 1
f(s) = 1
Las otras letras del abecedario tiene una frecuencia de 0 entonces se omiten,
antes o después del ordenamiento se obtiene:

```scala
List(('a', 2), ('c',1), ('s', 1))
```
La letra 'a' aparece primero porque tiene una frecuencia de 2,
las otras letras 'c' y 's' tienen la misma frecuencia: $f(c) = f(s) = 1$ 
entonces se aplica lo del orden alfabético $c < s$, dando como resultado:
```scala
List(('a', 2), ('c',1), ('s', 1))
```

### Conclusión
Se demostró que:
1. 'calcularLetras' cuenta correctamente todas las apariciones de una letra en 
  el mensaje
2. 'examinarLetra' revisa todas las letras desde la 'a' hasta la 'z'
3. Las llamadas recursivas terminan
4. El ordenamiento coloca primero las letras de mayor frecuencia
5. En caso de empate, se utiliza el sistema de ordenamiento alfabético en inglés
6. El acumulador 'totalLetra' conserva correctamente el conteo de apariciones durante la recursión
7. Solo se agregan a la lista las letras cuya frecuencia > 0

Teniendo en cuenta lo anterior se puede decir que:
$ \forall m \in \text{Mensaje}:P_F(m) = F(m)$
(El programa funciona bien si para cualquier mensaje devuelve 
el resultado correcto)
- m: Un mensaje cualquiera
- P_F(m): Resultado que da el programa
- F(m): Resultado correcto esperado






