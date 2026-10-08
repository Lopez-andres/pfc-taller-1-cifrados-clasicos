

# Informe de Corrección taller 1: Cifrados Clásicos

Asignatura de Fundamentos de Programación Funcional y Concurrente.
Documento realizado por estudiantes.

## 1. Argumentar la corrección de programas recursivos

## Punto 1. Cifrado César recursivo lineal 

Sea: 
$$
f: \text{Mensaje} \times \mathbb{Z} \to \text{Mensaje}
$$

La función que aplica un desplazamiento $k$ a
cada letra minúscula de un mensaje. Es decir, las letras se desplazan dentro del alfabeto de 26 letras, 
no obstante los caracteres que no son minúsculas se mantienen sin cambios.

Sea $P_f$ el programa en Scala que implementa el cifrado César mediante recursión.


Se desea demostrar que:

$$
\forall m \in \text{Mensaje}, \forall k \in \mathbb{Z} : P_f(m,k) == f(m,k)
$$

La demostración se realiza mediante inducción estructural sobre el mensaje.

$\text{Mensaje}$ se define recursivamente: $"" \in \text{Mensaje}$, y si $c$ es un
carácter y $r \in \text{Mensaje}$, entonces $c+r \in \text{Mensaje}$.


La función $f$ se define por:

$$
f("",k) = ""
$$

$$
f(c+r,k) = \text{cifrar}(c,k) + f(r,k)
$$

$$
\text{cifrar}(c,k) = 
\begin{cases}
\text{letraNueva} & \text{si } c \text{ es minúscula} \\
c & \text{en otro caso}
\end{cases}
$$

donde $\text{letraNueva}$ es la letra desplazada $k$ posiciones, definida más abajo.

### Caso base: $m = ""$

Cuando el mensaje está vacío, la función no tiene caracteres que procesar y devuelve una cadena vacía:

$$
P_f("",k) \rightarrow ""
$$

Por otro lado, según la especificación del cifrado César, el resultado de cifrar un mensaje vacío también es vacío:

$$
f("",k) = ""
$$

Por lo tanto:

$$
P_f("",k) == f("",k)
$$

Así, se cumple el caso base.

### Caso de inducción

Sea:

$$ 
m= c + r
$$

Donde $c$ es el primer carácter o letra del mensaje y $r$ es el resto del mensaje.

**La hipótesis de inducción es:**

$$
P_f(r,k) == f(r,k)
$$

Tesis:

$$
P_f(c+r,k) == f(c+r,k)
$$

Para demostrar la tesis, se analiza el primer carácter $c$ del mensaje. La función obtiene este carácter mediante  
$\text{m.head}$ y lo almacena en la variable $c$. Posteriormente, utiliza $\text{esMinuscula}(c)$ para determinar si dicho carácter
corresponde a una letra minúscula.

#### Primer caso: $c$ es una letra minúscula.

Si $c$ es una letra minúscula, el programa calcula su posición dentro del alfabeto
mediante:

$$
\text{ubicacionInicial} = \text{c.toInt} - \text{primera}
$$

Después, calcula la nueva posición aplicando el desplazamiento $k$:

$$
\text{ubicacionNueva} = ((\text{ubicacionInicial} + k)\bmod 26 + 26)\bmod 26
$$

Esta operación permite mantener la posición de la letra dentro del intervalo de 0 a 25
incluso cuando el desplazamiento $k$ es negativo.

A continuación, se obtiene la nueva letra:

$$
\text{letraNueva} = (\text{ubicacionNueva} + \text{primera}).\text{toChar}
$$

Al obtener la nueva letra el programa realiza la llamada recursiva sobre el resto del mensaje.

$$
P_f(c+r,k) \rightarrow \text{letraNueva} + P_f(r,k)
$$

**Aplicando la hipótesis de inducción:**

$$
P_f(r,k) == f(r,k)
$$

Por lo tanto:

$$
\text{letraNueva} + P_f(r,k) \rightarrow \text{letraNueva} + f(r,k)
$$

Por definición del cifrado César, $\text{letraNueva}$ corresponde al resultado de aplicar el desplazamiento $k$
al carácter $c$. Además, $f(r, k)$ corresponde al resultado de aplicar el mismo desplazamiento al resto del mensaje.

Por lo tanto:
$$
\text{letraNueva} + f(r, k) = f(c+r, k) 
$$

Así:
$$
P_f(c+r,k)==f(c+r,k)
$$

#### Segundo Caso: $c$ no es una letra minúscula.

Si $c$ no es minúscula el programa conserva el carácter sin modificaciones y realiza la llamada recursiva sobre el resto del mensaje.
$$
P_f(c+r, k) \rightarrow c+P_f(r, k)
$$

**Aplicamos la hipótesis de inducción**

$$
P_f(r,k) == f(r,k)
$$

Entonces:
 
$$
c+ P_f(r,k) \rightarrow c+f(r,k)
$$

Como los caracteres que no son letras minúsculas se mantienen sin cambios: 

$$ 
c+f(r,k) = f(c+r,k)
$$

Por lo tanto:
$$
P_f(c+r,k)==f(c+r,k)
$$

En ambos casos se cumple la tesis de inducción.

**Encadenamiento de los llamados** para `cesar("casa", 3)`:

$$
P_f("casa",3) \rightarrow "f" + P_f("asa",3) \rightarrow "f" + ("d" + P_f("sa",3))
\rightarrow "f" + ("d" + ("v" + P_f("a",3))) \rightarrow "f" + ("d" + ("v" + ("d" + P_f("",3))))
\rightarrow "f" + ("d" + ("v" + ("d" + ""))) \rightarrow "fdvd"
$$

Cada llamada deja una suma pendiente. Solo se resuelven al volver desde el caso 
base, y por eso la pila crece.

**Conclusión**

Como se cumple el caso base y se ha demostrado que, suponiendo la hipótesis de inducción, se cumple la 
tesis para un mensaje formado por un primer carácter y el resto del mensaje, por inducción estructural se concluye que:

$$
\forall m 
\in \text{Mensaje}, \forall k \in \mathbb{Z} : P_f(m,k) == f(m,k)
$$

Es decir, el programa recursivo lineal que implementa el cifrado César es correcto con respecto a su especificación.


## 2. Argumentar la corrección de programas iterativos

Para argumentar la corrección del proceso utilizado en la recursión de cola, se formalizan los estados de la ejecución.

Se define:

- Cómo se representa un estado de la iteración, $s$.
- Cuál es el estado inicial, $s_0$.
- Cuál es el estado final (o cómo se reconoce que un estado es final): $s_f$.
- Qué condición (o predicado) cumple todo estado: $\text{Inv}(s)$ (invariante
  de la iteración).
- El mecanismo para pasar de un estado al siguiente: $\text{transformar}(s)$.
  Si $s_i$ es el estado $i$, entonces $\text{transformar}(s_i) = s_{i+1}$.

Para argumentar la corrección del proceso se debe demostrar que:

- La invariante se cumple en el estado inicial.
- La transformación conserva la invariante.
- Cuando se alcanza el estado final, la invariante permite obtener el resultado correcto.
- El proceso alcanza el estado final.

## Punto 2: Cifrado cesarCola mediante recursión de cola

Sea:
$$
f: \text{Mensaje} \times \mathbb{Z} \to \text{Mensaje}
$$


La función aplica un desplazamiento $k$ a cada letra minúscula de un mensaje.
Los caracteres que no son letras minúsculas se mantienen sin cambios.

Sea $P_f$ el programa en Scala que implementa el cifrado César mediante recursión de cola.
Se define como $P_f(m,k) = \text{cesarCola}(m,k,"")$, porque el acumulador $acc$ tiene como valor por defecto la cadena vacía.

Se desea demostrar que:

$$
\forall m \in \text{Mensaje}, \forall k \in \mathbb{Z} : P_f(m,k) == f(m,k)
$$

El programa utiliza un acumulador $acc$. Este acumulador permite guardar el resultado que ya ha sido construido
mientras la función continúa procesando el mensaje.

Por esta razón, la demostración se realiza siguiendo los estados del proceso y verificando que una
propiedad, llamada invariante, se mantiene durante cada transformación.

### Estado de ejecución 

Sea $m$ el mensaje original.

La función cesarCola recibe tres elementos:
**cesarCola(m, k, acc)**

- $m$ es la parte del mensaje que falta por procesar.
- $k$ es el desplazamiento.
- $acc$ es la parte del resultado que ya ha sido procesada.

Por lo tanto, el estado del proceso se representa mediante:

$$
s=(m,k,acc)
$$


El estado inicial donde todavía no se ha procesado ningún carácter es:

$$
s_0 =(M,k,"")
$$

En otras palabras, al inicio todo el mensaje está pendiente y el acumulador está vacío.

El estado es final cuando no quedan caracteres por procesar:

$$
s_f=("",k,acc)
$$

La transformación consiste en procesar el primer carácter de $m$, agregando el resultado al acumulador y 
continuar con el resto del mensaje.

La invariante que se utilizará es:
 
$$
\text{Inv}(m,k,acc)
\equiv
acc+f(m,k)=f(M,k).
$$

Lo que ya está guardado en $acc$, junto con el resultado de cifrar la parte del mensaje que todavía falta por
procesar, debe ser igual al cifrado del mensaje original.

Ejemplo: si una parte del mensaje ya fue procesada y se encuentra en acc, entonces
el resto del mensaje todavía está en $m$. La suma de esas dos partes debe corresponder al resultado final.

### 1. La invariante se cumple en el estado inicial  

El estado inicial es:

$$
s_0=(M,k,"")
$$

Para comprobar que la invariante se cumple desde el comienzo, reemplazamos $m$
por $M$ y acc por el mensaje vacío:


$$
""+f(M,k)=f(M,k)
$$

Como concatenar un mensaje vacío no modifica el resultado:

$$ 
f(M,k)=f(M,k)
$$

Por lo tanto, la invariante se cumple en el estado inicial.
$$
\text{Inv}(s_0)
$$
Esto quiere decir que desde el comienzo se cumple la propiedad que queremos
conservar durante toda la ejecución.

### 2. La invariante se conserva durante el proceso.

Supongamos que todavía queda una parte del mensaje por procesar.

Lo representaremos como:

$$
m=c+r 
$$

Donde $c$ representa el primer carácter del mensaje que falta por procesar y $r$ representa el resto del mensaje.

Suponemos que antes de procesar $c$ la invariante ya se cumple:

$$
acc+f(c+r,k)=f(M,k)
$$

Ahora se debe comprobar que después de procesar el carácter $c$, la invariante continúa cumpliéndose.

**Se representan dos casos:**

### Caso 1: $c$ es una letra minúscula

Cuando $c$ es una letra minúscula, el programa calcula la letra correspondiente, después
de aplicar el desplazamiento $k$.

Sea:

$$
\text{letraNueva}=\text{cifrar}(c,k)
$$

El programa agrega esta letra al acumulador $acc$ y continúa con el resto del mensaje.
Por lo tanto, la transformación del estado es:

$$
(c+r,k,acc)
\rightarrow
(r,k,acc+\text{letraNueva})
$$

Esto representa exactamente lo que hace el programa: el primer carácter $c$ ya fue procesado,
se agregó su resultado al acumulador y ahora queda $r$ por procesar.

Para el nuevo estado se debe comprobar que la invariante continúa cumpliéndose:

$$
(acc+\text{letraNueva})+f(r,k)=f(M,k)
$$

Por definición del cifrado César, cifrar primero $c$ y luego cifrar $r$ produce el mismo resultado que cifrar todo $c+r$

$$
\text{letraNueva}+f(r,k)=f(c+r,k)
$$

Por lo tanto:
$$
acc+f(c+r,k)
$$
y por la hipótesis (la invariante antes de procesar $c$):
$$
acc+f(c+r, k)=f(M, k)
$$
Entonces:
$$
(acc+\text{letraNueva})+f(r, k)=f(M, k)
$$

Por lo tanto, la invariante se mantiene después de procesar una letra minúscula.
$$
\text{Inv}(r, k, acc+\text{letraNueva})
$$

### Caso 2: $c$ no es una letra minúscula

Cuando $c$ no es una letra minúscula, el carácter se conserva sin cambios. El programa lo agrega
al acumulador $acc$ y continúa con el resto del mensaje.


La transformación del estado es:

$$
(c+r,k,acc)
\rightarrow
(r,k,acc+c)
$$

Para el nuevo estado debemos demostrar que:

$$
(acc+c)+f(r,k)=f(M,k)
$$

Como $c$ no es una letra minúscula, el cifrado César lo mantiene sin cambios.

Por lo tanto:
$$
c+f(r, k)=f(c+r, k)
$$
Entonces:
$$
acc+f(c+r, k)
$$
 y por la invariante que se tenía antes de procesar $c$

$$ 
acc+f(c+r,k)=f(M,k)
$$
Por lo tanto:
$$
(acc+c)+f(r,k)=f(M,k)
$$

Así, la invariante también se mantiene cuando $c$ no es una letra minúscula.
$$
\text{Inv}(r, k, acc+c)
$$
En ambos casos, la transformación conserva la invariante.

### 3. Corrección en el estado final

El estado final se alcanza cuando no quedan caracteres por procesar.

$$
s_f= ("",k,acc)
$$

En este estado, la invariante establece que:

$$
acc+f("",k)=f(M,k)
$$

como el cifrado de un mensaje vacío es un mensaje vacío

$$
f("",k)=""
$$

Entonces:

$$
acc+""=f(M,k)
$$

Por lo tanto:

$$
acc=f(M,k)
$$

Cuando el programa encuentra que el mensaje está vacío, retorna el acumulador.

$$
\text{cesarCola}("",k,acc)\rightarrow acc
$$

Como acabamos de demostrar que:
$$
acc=f(M, k)
$$

Se concluye que:

$$
\text{cesarCola}(M,k,"") \rightarrow acc ==f(M,k)
$$

En esta parte, cuando el programa termina, el acumulador contiene exactamente el resultado correcto
del cifrado César.

### 4. El proceso alcanza el estado final 

Finalmente, se debe comprobar que el programa realmente llegue al estado final.

En cada llamada recursiva, el programa toma el primer carácter del mensaje mediante
$\text{m.head}$ y continúa con el resto del mensaje mediante $\text{m.tail}$.

Por lo tanto, el mensaje pendiente disminuye en un carácter en cada llamada:

$$
|c+r| = |r| + 1
$$

Como $|m|$ es un entero no negativo y disminuye en 1 en cada llamada, después de $|M|$ llamadas
se cumple $|m| = 0$, es decir, el mensaje pendiente queda vacío:

$$
m=""
$$
Por lo tanto, el proceso alcanza el estado final:

$$
s_f=("",k,acc)
$$

### 5. Encadenamiento de los llamados

Para `cesarCola("casa", 3)` con $f$ desplazando 3 posiciones, los estados son:

| Paso | $m$ (pendiente) | $acc$ | $acc + f(m,3)$ |
|-----|--------------|-------|--------------|
| 0 | "casa" | "" | "" + "fdvd" |
| 1 | "asa" | "f" | "f" + "dvd" |
| 2 | "sa" | "fd" | "fd" + "vd" |
| 3 | "a" | "fdv" | "fdv" + "d" |
| 4 | "" | "fdvd" | "fdvd" + "" |

En todos los pasos el valor de $acc + f(m,3)$ es "fdvd" $= f(M,3)$, y en el estado final
$acc =$ "fdvd".

En ningún paso queda una operación pendiente: cada llamada es lo último que hace la función, por eso la pila
no crece (a diferencia de `cesar`, que dejaba sumas pendientes).

## Conclusión del punto 2

Se ha demostrado que la invariante se cumple en el estado inicial, se conserva durante cada transformación y permite obtener el resultado
correcto cuando se alcanza el estado final. Además, el proceso alcanza dicho estado porque en cada
llamada recursiva se elimina un carácter del mensaje pendiente.

Por último, el programa cesarCola es correcto con respecto a la especificación del cifrado César.

## Punto 4. Romper César: desplazamientoProbable y romperCesar

`desplazamientoProbable` y `romperCesar` no son recursivas por sí mismas: la primera analiza casos 
y la segunda compone funciones. Por eso la corrección se argumenta por análisis de casos y composición, apoyándose en lo demostrado
en los puntos 1 y 3.

### Especificación
Sea $\text{pos}(l) = l - \text{a}$ la posición de la letra minúscula $l$ en el alfabeto, de modo que $\text{pos}(l) \in \{0,\ldots,25\}$ y $\text{pos}(\text{e}) = 4$. 

Sea $\text{frec}(m)$ la lista de pares $(l,n)$ donde $l$ es una letra minúscula que aparece $n \geq 1$ veces en $m$, ordenada de mayor a menor $n$ y, con $n$ igual, por orden alfabético de $l$.

Sea $d : \text{Mensaje} \to \mathbb{N}$ el desplazamiento probable:

$$
d(m) = 
\begin{cases}
0 & \text{si } \text{frec}(m) = \text{List}() \\
(\text{pos}(l) - 4) \bmod 26 & \text{si } \text{frec}(m) = (l,n) :: t
\end{cases}
$$

Y sea $\text{romper}(m) = f(m, -d(m))$, con $f$ el cifrado César definido en el punto 1.

Sean $P_d$ y $P_r$ los programas `desplazamientoProbable` y `romperCesar`. Se desea demostrar que:

$$
\forall m \in \text{Mensaje} : P_d(m) == d(m) \;\land\; P_r(m) == \text{romper}(m)
$$

### Hipótesis tomadas de otros puntos

- **(H1)** Por el punto 3, $\text{frecuencias}(m) == \text{frec}(m)$ para todo $m$, incluido el orden de desempate.
- **(H2)** Por el punto 1, $\text{cesar}(m,k) == f(m,k)$ para todo $m$ y $k$.

No se demuestran de nuevo: se usan como consignas ya probadas.

### Consigna del residuo

En Scala `%` conserva el signo del dividendo, así que puede dar negativos. Para todo $x \in \mathbb{Z}$:

$$
((x \% 26) + 26) \% 26 = x \bmod 26 \in \{0, \ldots,25\}
$$

Prueba: sea $r = x \% 26$. Entonces $r \equiv x \pmod{26}$ y $-25 \leq r \leq 25$.
Así $r + 26 \in [1, 51]$ es positivo y congruente con $x$, y para un número positivo `%` coincide
con el módulo matemático, que cae en $\{0,\ldots,25\}$. Como en ese rango hay un único representante de la clase $x$, el resultado 
es $x \bmod 26$.

### Corrección de desplazamientoProbable

El programa obtiene $\text{frecuencias}(m)$ y decide si la lista está vacía.

#### Primer caso: $\text{frec}(m)$ está vacía (el mensaje no tiene letras minúsculas)

Por (H1) la lista es vacía y el programa devuelve 0:

$$
P_d(m) \rightarrow 0 = d(m)
$$

#### Segundo caso: $\text{frec}(m) = (l,n) :: t$

Por (H1) la cabeza de la lista es la letra $l$, y el programa calcula:

$$
P_d(m) \rightarrow ((\text{l.toInt} - \text{'e'.toInt}) \% 26 + 26) \% 26
$$

Como $\text{l.toInt} = 97 + \text{pos}(l)$ y $\text{'e'.toInt} = 101 = 97 + 4$, la diferencia es $\text{pos}(l) - 4$. Por la consigna del residuo:

$$
P_d(m) = (\text{pos}(l) - 4) \bmod 26 = d(m)
$$

En ambos casos $P_d(m) == d(m)$.

### Corrección de romperCesar

$$
\begin{aligned}
P_r(m) &\rightarrow \text{cesar}(m, -P_d(m)) \\
&= \text{cesar}(m, -d(m)) \\
&= f(m, -d(m)) \\
&= \text{romper}(m)
\end{aligned}
$$

La segunda igualdad usa lo demostrado para $P_d$, y la tercera usa (H2). Por lo tanto $P_r(m) == \text{romper}(m)$.

### Cuándo el método recupera el original y cuándo falla 

Hasta aquí se demostró que los programas cumplen su especificación. Otra cosa es si esa especificación deshace el cifrado, y eso depende de que la letra más frecuente del original sea la `e`.

**Consigna A.** $f(f(o,k),j) = f(o, k+j)$, y $f(o,k) = f(o,k')$ si $k \equiv k' \pmod{26}$.
Se prueba por inducción estructural sobre $o$, como en el punto 1: una
letra en la posición $p$ pasa a $((p+k) \bmod 26 + j) \bmod 26 = (p+k+j) \bmod 26$, 
y los caracteres que no son letras no cambian. 

**Consigna B.** El desplazamiento $p \mapsto (p+k) \bmod 26$ es una biyección de las posiciones. Por eso, en $m = f(o,k)$ la letra en la posición $(p+k) \bmod 26$ aparece exactamente tantas veces como la letra en la posición $p$ en $o$.

**Teorema.** Sea $m = f(o,k)$. Entonces $\text{romper}(m) = o$ si y solo si $o$ no tiene letras, o la cabeza de $\text{frec}(m)$ es la letra en la posición $(4+k) \bmod 26$.

- ($\Leftarrow$) Si $o$ no tiene letras, $m = o$, $d(m) = 0$ y $\text{romper}(m) = f(o,0) = o$. Si la cabeza es la letra en la posición $(4+k) \bmod 26$, entonces $d(m) = k \bmod 26$ y, por la consigna A, $\text{romper}(m) = f(o, k - (k \bmod 26)) = o$.
- ($\Rightarrow$) Por la consigna A, $\text{romper}(m) = f(o, k - d(m))$. Si $o$ tiene una letra en la posición $p$ y $\text{romper}(m) = o$, entonces $(p + k - d(m)) \bmod 26 = p$, es decir, $d(m) \equiv k \pmod{26}$. Eso significa que la cabeza está en la posición $(4+k) \bmod 26$.

**Condición suficiente:** la `e` es la única letra más frecuente de $o$. Por la consigna B, su imagen es la única más frecuente de $m$, así que es la cabeza sin desempate.

**El método falla** cuando:

- La letra más frecuente del original no es la `e` (textos cortos, con pocas `e` o con palabras poco comunes).
- Hay empate. El desempate es alfabético sobre las letras de $m$ (ya cifradas), y el desplazamiento no conserva el orden alfabético porque se envuelve después de la `z`. Una `e` que empata en $o$ puede perder en $m$.

**Mensaje concreto 1 (falla por frecuencia):** $o$ = "cada casa amarilla", $k = 7$.

- No tiene ninguna 'e' y la 'a' aparece 7 veces
- $m = f(o,7)$ = "jhkh jhzh hthypssh".
- $\text{frec}(m) = (\text{h},7) :: (\text{j},2) :: (\text{s},2) :: \ldots$, así que
- $d(m) = (7-4) \bmod 26 = 3 \neq 7$.
- $\text{romperCesar}(m)$ = "gehe gewe eqevmppe" $\neq$ "cada casa amarilla".

**Mensaje concreto 2 (falla por desempate):** $o$ = "ez", $k = 2$.

- $m$ = "gb", $\text{frec}(m) = (\text{b},1) :: (\text{g},1)$, y gana la `b` por orden alfabético.
- $d(m) = (1-4) \bmod 26 = 23 \neq 2$ y $\text{romperCesar}(m)$ = "je" $\neq$ "ez".
- En $o$ la `e` ganaba el empate sobre la `z`, pero en $m$ la `b` precede a la `g`.

**Caso que sí funciona** (el del enunciado): $o$ = "el mensaje secreto", $k = 7$. Da $m$ = "ls tluzhql zljylav", la `l` aparece 5 veces, $d(m) = 7$ y se recupera $o$.

### Encadenamiento de los llamados

Como Scala evalúa por valor, el cálculo ocurre en dos fases. Se muestra $m$ = "hvh" = $f(\text{"ese"}, 3)$:

$$
\text{romperCesar}("hvh") \rightarrow \text{cesar}("hvh", -\text{desplazamientoProbable}("hvh"))
$$

**Fase 1:** se evalúa $\text{desplazamientoProbable}$, que llama a $\text{frecuencias}$:

$$
\text{desplazamientoProbable}("hvh") \rightarrow \text{frecuencias}("hvh") \rightarrow (\text{h},2) :: (\text{v},1) :: \text{List}() \rightarrow ((104 - 101)\%26 + 26)\%26 \rightarrow 3
$$

**Fase 2:** con el argumento ya calculado, empieza $\text{cesar}$:

$$
\text{cesar}("hvh",-3) \rightarrow "e" + \text{cesar}("vh",-3) \rightarrow "e" + ("s" + \text{cesar}("h",-3)) \rightarrow "e" + ("s" + ("e" + "")) \rightarrow "ese"
$$

Cuando empieza la fase 2, los llamados de $\text{desplazamientoProbable}$ y $\text{frecuencias}$ ya terminaron. La pila solo crece dentro de $\text{cesar}$, como se vio en el punto 1. 
En $\text{frecuencias}$ no crece, porque es recursiva de cola (punto 3).

### Conclusión
Se demostró que $P_d(m) == d(m)$ y que $P_r(m)$ == $\text{romper}(m)$ para todo mensaje, apoyándose en la corrección de `frecuencias` (punto 3) y de `cesar` (punto 1). 
Ahora bien, $\text{romper}$ solo deshace el cifrado cuando la letra más frecuente del original es la `e` (o cuando no hay letras). Los dos mensajes de arriba muestran
que fuera de esa condición el método falla, aunque los programas sean correctos respecto a su especificación. 

## Punto 5. Vigenère y conteo de mensajes: combinaciones y vigenere

`combinaciones` y `vigenere` son recursivas lineales: la llamada recursiva queda
dentro de una operación pendiente (`*` en `combinaciones`, `+` en `vigenere`), así 
que no son de cola. Por eso la corrección se argumenta por inducción estructural,
como en el punto 1. 

### 5.1 combinaciones 

#### Especificación

Sea $C(n,a)$ la cantidad de mensajes de longitud $n$ que se forman con $a$ letras 
sin dos letras iguales seguidas, con $n \in \mathbb{N}$ y $a \geq 1$. La primera
letra puede ser cualquiera de las $a$, y cada letra siguiente puede ser cualquiera
menos la anterior ($a-1$ opciones). Entonces:

$$
C(n,a) =
\begin{cases}
1 & \text{si } n = 0 \\
a \, (a-1)^{n-1} & \text{si } n \geq 1
\end{cases}
$$

Sea $P_c$ el programa `combinaciones`. Se desea demostrar que:

$$
\forall n \in \mathbb{N} : P_c(n,a) == C(n,a)
$$

Se fija un $a \geq 1$ cualquiera y se hace inducción sobre $n$.

#### Casos base
- **$n = 0$:** se cumple `n == 0`, así que $P_c(0,a) \rightarrow 1 = C(0,a)$.
- **$n = 1$:** no se cumple `n == 0` y sí `n == 1`, así que $P_c(1,a) \rightarrow a = a\,(a-1)^0 = C(1,a)$.

#### Paso inductivo

Sea $k \geq 1$.

- **Hipótesis de inducción:** $P_c(k,a) == a\,(a-1)^{k-1}$.
- **Tesis:** $P_c(k+1,a) == a\,(a-1)^{k}$.

Como $k+1 \geq 2$, no se cumple ni `n == 0` ni `n == 1`, y el programa usa la tercera rama:

$$
\begin{aligned}
P_c(k+1,a) &\rightarrow (a-1) \cdot P_c(k,a) \\
&= (a-1) \cdot a \, (a-1)^{k-1} \quad \text{(por la hipótesis de inducción)} \\
&= a \, (a-1)^{k} \\
&= C(k+1,a)
\end{aligned}
$$

El paso empieza en $k = 1$ porque para $n = 1$ el programa no recurre, y la forma de 
$C(0,a)$ es distinta a la de $n \geq 1$. Por eso hacen falta los dos casos base. 

**Terminación:** en cada llamada recursiva $n$ baja en 1, y con $n \geq 1$ se llega
a $n = 1$, donde no hay más llamadas. Para $n < 0$ no termina, pero eso queda fuera del dominio.

Por lo tanto $P_c(n,a) == C(n,a)$ para todo $n \in \mathbb{N}$.

#### Encadenamiento de los llamados

Con $n = 3$ y $a = 3$:

$$
\begin{aligned}
\text{combinaciones}(3,3) &\rightarrow 2 \cdot \text{combinaciones}(2,3) \\
&\rightarrow 2 \cdot (2 \cdot \text{combinaciones}(1,3)) \\
&\rightarrow 2 \cdot (2 \cdot 3) \\
&\rightarrow 12
\end{aligned}
$$

Las multiplicaciones quedan pendientes hasta llegar a $n = 1$, igual que las 
concatenaciones de `cesar` en el punto 1. El resultado coincide con $C(3,3) = 3
\cdot 2^2 = 12$.

### 5.2 vigenere

#### Especificación 

Sea $K$ la clave, con $n = \lvert K \rvert$, formada solo por letras minúsculas.
Sea $\text{pos}(x) = x - \text{a}$ la posición de una letra minúscula, como en el punto 4.
Sea $s(c,x)$ la letra minúscula en la posición $(\text{pos}(c) + \text{pos}(x)) \bmod 26$.

Sea $\varepsilon$ el mensaje vacío y $\cdot$ la concatenación. Para un índice de clave $j \in \{0,\dots,n-1\}$:

$$
V(m,j) = 
\begin{cases}
\varepsilon & \text{si } m = \varepsilon \\
s(c, K[j]) \cdot V(t, (j+1) \bmod n) & \text{si } m = c \cdot t \text{ y } c \text{
es minúscula} \\
c \cdot V(t, j) & \text{si } m = c \cdot t \text{ y } c \text{ no es minúscula}
\end{cases}
$$

Es decir, cada letra minúscula se corre según la letra de la clave que le toca, la 
clave se reinicia al terminarse, y lo que no es letra minúscula se copia sin consumir clave. 
La especificación completa es:

$$
v(m,K) = 
\begin{cases}
m & \text{si } K = \varepsilon \\
V(m,0) & \text{si } K \neq \varepsilon
\end{cases}
$$

Sean $P_v$ y $P_a$ los programas `vigenere` y su función interna `auxClave`, que usa la clave $K$ 
del programa externo. Se desea demostrar que:

$$
P_v(m,K) == v(m,K)
$$

#### Estados de la clave

La llamada recursiva de `auxClave` cambia el parámetro `claveRestante`, así que no se puede demostrar
con ese parámetro fijo. La hipótesis de inducción debe valer para todos los valores que `claveRestante` puede tomar.

Sea $R = \{\text{drop}(K,j) : 0 \leq j \leq n\}$ el conjunto de sufijos de $K$
(incluidos $K$ y $\varepsilon$). Cada llamada empieza con `claveRestante` en $R$, y
se asocia a cada $r \in R$ un índice:

$$
\iota(r) = (n - \lvert r \rvert) \bmod n 
$$

Así $\iota(K) = 0$, $\iota(\varepsilon) = 0$ y, para $r = \text{drop}(K,j)$ con $0 
\leq j < n$, $\iota(r) = j$.

**Consigna C (clave actual).** Sea $r \in R$ y $j = \iota(r)$. La clave actual del programa
(`K` si `r` es vacía, `r` si no) es $\text{drop}(K,j)$, no es vacía, su cabeza
es $K[j]$ y su cola es $\text{drop}(K,j+1) \in R$ con $\iota(\text{drop}(K,j+1)) = (j+1) \bmod n$.

Prueba: si $r = \varepsilon$, la clave actual es $K = \text{drop}(K,0)$ y $j = 0$.
Si $r \neq \varepsilon$, entonces $r = \text{drop}(K,j)$ con $j < n$. En ambos casos 
queda $\text{drop}(K,j)$, que tiene $n - j \geq 1$ letras. Su cola tiene $n-j-1$ letras, 
luego su índice es $(n - (n-j-1)) \bmod n = (j+1) \bmod n$.

**Consigna D (aritmética del programa).** Si $c$ y $x$ son minúsculas, la letra que construye
el programa es $s(c,x)$.

Prueba: el programa calcula $((\text{pos}(c)) + (\text{pos}(x) \% 26) + 26) \% 26$.
Como $0 \leq \text{pos}(x) \leq 25$, entonces $\text{pos}(x) \% 26 = \text{pos}(x)$.
La suma $\text{pos}(c) + \text{pos}(x) + 26$ es positiva, y sumar 26 no cambia el
residuo, por lo que `%` coincide con el módulo matemático y da $(\text{pos}(c) + \text{pos}(x)) \bmod 26$. Sumarle `'a'` da la letra $s(c,x)$.

#### consigna principal

$$
\forall m \;\; \forall r \in R : P_a(m,r) == V(m, \iota(r))
$$

Se demuestra por inducción estructural sobre $m$.

**Caso base: $m = \varepsilon$.** El programa devuelve `""`, y $V(\varepsilon,\iota(r)) = \varepsilon$.

**Paso inductivo: $m = c \cdot t$.**

- **Hipótesis de inducción:** para todo $r' \in R$, $P_a(t,r') == V(t, \iota(r'))$.
- **Tesis:** para todo $r \in R$, $P_a(c \cdot t, r) == V(c \cdot t, \iota(r))$.

Sea $r \in R$ y $j = \iota(r)$.

- **Si $c$ es minúscula:** por las consignas C y D, el programa devuelve $s(c, K[j])
\cdot P_a(t, \text{drop}(K,j+1))$. Como $\text{drop}(K,j+1) \in R$, la hipótesis de inducción da:

$$
\begin{aligned}
P_a(c \cdot t, r) &\rightarrow s(c, K[j]) \cdot P_a(t, \text{drop}(K,j+1)) \\
&= s(c, K[j]) \cdot V(t, (j+1) \bmod n) \\
&= V(c \cdot t, j)
\end{aligned}
$$

- **Si $c$ no es minúscula:** el programa devuelve $c \cdot P_a(t, r)$ con la misma `claveRestante`.
  Como $r \in R$, la hipótesis de inducción da:

$$
\begin{aligned}
P_a(c \cdot t, r) &\rightarrow c \cdot P_a(t, r) \\
&= c \cdot V(t, j) \\
&= V(c \cdot t, j)
\end{aligned}
$$

En ambos casos se cumple la tesis.

#### Corrección de vigenere

- **Si $K = \varepsilon$:** el programa devuelve $m = v(m,K)$.
- **Si $K \neq \varepsilon$:** el programa llama a $P_a(m,K)$. Como $K \in R$ y
  $\iota(K) = 0$, por la consigna principal $P_a(m,K) == V(m,0) = v(m,K)$.

Por lo tanto $P_v(m,K) == v(m,K)$ para todo $m$ y toda clave de minúsculas. Si la clave
tuviera letras que no son minúsculas, el programa quedaría fuera de la especificación.

#### Encadenamiento de los llamados 
Con $m$ = "ab c" y $K$ = "bd" ($n = 2$):

| Llamada | `m` | `claveRestante` | $\iota$ | Clave usada                           | Aporta       |
|---------|-----|------------|---------|---------------------------------------|--------------|
| 1       | `"ab c"` | `"bd"`     | 0       | 'b'                                   | `b` (de `a`) |
| 2       | `"b c"` | `"d"`      | 1       | 'd'                                   | `e` (de `b`) |
| 3       | `" c"` | `""`       | 0       | ninguna (el espacio no consume clave) | `" "`        |
| 4       | `"c"` | `""`       | 0       | 'b' (la clave se reinicia con 'K')    | `d` (de `c`) |
| 5       | `""` | `"d"`      | 1       | ninguna                               | `""`         |

$$
\begin{aligned}
\text{auxClave}("ab c", "bd") &\rightarrow "b" + \text{auxClave}("b c", "d") \\
&\rightarrow "b" + ("e" + \text{auxClave}(\text{" c"},"")) \\
&\rightarrow "b" + ("e" + (\text{" "} + \text{auxClave}("c",""))) \\
&\rightarrow "b" + ("e" + (\text{" "} + ("d" + \text{auxClave}("","d")))) \\
&\rightarrow "b" + ("e" + (\text{" "} + ("d" + ""))) \\
&\rightarrow "be\ d"
\end{aligned}
$$

Los índices $\iota$ de la tabla (0, 1, 0, 0, 1) coinciden con el avance de $V$: sube
con cada letra minúscula y vuelve a 0 al llegar a $n = 2$. Las concatenaciones
quedan pendientes hasta llegar al mensaje vacío, como en `cesar` del punto 1, así que la 
pila crece con la longitud de $m$.

### Conclusión del punto 5

Se demostró que $P_c(n,a) == C(n,a)$ por inducción sobre $n$ con dos casos base, y que $P_v(m,K) == v(m,K)$ por inducción
sobre $m$, generalizando la hipótesis a todas las posibles `claveRestante`. Ambos programas son correctos respecto a su especificación.