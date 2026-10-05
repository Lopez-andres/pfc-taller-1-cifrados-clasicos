

#  Informe de Corrección taller 1: Cifrados Clásicos

Asignatura de Fundamentos de Programación Funcional y Concurrente.
Documento realizado por estudiantes.

## 1. Argumentar la corrección de programas recursivos

## Punto 1. Cifrado César recursivo Lineal 

Sea: 
$$
f: \text{Mensaje} \times \mathbb{Z} \to \text{Mensaje}
$$

La función que aplica un desplazamiento (k) a
cada letra minúscula de un mensaje. Es decir las letras se desplazan dentro del alfabeto de 26 letras,
no obstante los caracteres que no son minúsculas se mantienen sin cambios.

Sea $P_f$ el programa de escala que implementa el cifrado cesar mediante recursión.

 Se desea demostrar que:

$$
\forall m \in \text{Mensaje}, \forall k \in \mathbb{Z} : P_f(m,k) == f(m,k)
$$

La demostración se realiza mediante inducción estructural sobre el mensaje.

### Caso base: $m = ""$

Cuando el mensaje está vacío, la función no tienen caracteres que procesar y devuelve una cadena vacía:

$$
P_f("",k) \rightarrow ""
$$

Por otro lado, según la especificación del cifrado cesar, el resultado de cifra un mensaje vacío también es vacío:

$$
P_f("",k) = ""
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
$m.head$ y lo almacena en la variable $c$. posteriormente, utiliza $esMinuscula(c)$ para demostrar si dicho carácter
corresponde a una letra minúscula.

##Primer caso: $c$ es una letra minúscula.

Si $c$ es una letra minúscula, el programa calcula su posición dentro del alfabeto
mediante:

$$
\text{ubicacionInicial} = c.toInt - \text{primera}
$$

Después, calcula la nueva posición aplicando el desplazamiento k:

$$
\text{ubicacionNueva} = ((ubicacionInicial + k)\bmod 26 + 26)\bmod 26
$$

Esta operación permite mantener la posición de la letra dentro del intervalo de 0 a 25
incluso cuando el desplazamiento $k$ es negativo.

A continuación, se obtiene la nueva letra:

$$
\text{letraNueva} = (\text{ubicacionNueva} + \text{primera}).toChar
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
\text{LetraNueva} + P_f(r,k) \rightarrow \text{letraNueva} + f(r,k)
$$

Por definición del cifrado cesar, $\text{letraNueva}$ corresponde al resultado de aplicar el desplazamiento $k$
al carácter $c$. Además, $f(r, k)$ corresponde al resultado de aplicar el mismo desplazamiento al resto del mensaje.

Por lo tanto:
$$
\text{nuevaletra} + f(r0, k) = f(c+r, k) 
$$

Así:
$$
P_f(c+r,k)==f(c+r,k)
$$

## Segundo Caso: $c$ no es una letra minúscula.

Si $c$ no es minúscula el programa conserva el carácter sin modificaciones y realiza la llamada recursiva sobre el resto del mensaje.
$$
P_f(c+r, k) \rightarrow c+P_f(r, k)
$$

**Aplicamos la hipotesis de inducción**

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

Por lo tanto:
$$
P_f(c+r,k)==f(c+r,k)
$$

En ambos casos se cumple la tesis de inducción.

**Conclusión**

Como se cumple el caso base y se ha demostrado que, suponiendo la hipótesis de inducción, se cumple la 
tesis para mensaje formado por un primer carácter y el resto del mensaje por inducción estructural se contruye que:

$$
\forall m 
\in \text{Mensaje}, \forall k \in \mathbb{Z} : P_f(m,k) == f(m,k)
$$

Es decir el programa recursivo lineal que implementa el cifrado cesar es correcto con respecto a su especificaciónes.


##  2.1 Argumentar la corrección de programas iterativos

Para argumentar la corrección del proceso utilizando en la recursión de cola, se formalizan los estados de la ejecución

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
- La transformación conserva la invariante
- Cuando se alcanza el estado final, la invariante permite obtener el resultado correcto.
- El proceso alcanza el estado final.

## Punto 2: Cifrado cesarCola mediante recursión de cola

Sea:
$$
f: \text{Mensaje} \times \mathbb{Z} \to \text{Mensaje}
$$


La función aplica un desplazamiento $k$ a cada letra minúscula de un mensaje.
Los carácteres que no son letras minúsculas se mantiene sin cambios.

Sea $P_f$ el programa de escala que implementa el cifrado cesar mediante recursión.

Se desea demostrar que:

$$
\forall m \in \text{Mensaje}, \forall k \in \mathbb{Z} : P_f(m,k) == f(m,k)
$$

El programa utiliza un acumulador $acc$; Este acumulador permite guardar el resultado que ya ha sido construido
mientras la función continúa procesando el mensaje.

Por esta razón, la demostración se realiza siguiendo los estados del proceso y verificando que una
propiedad, llamada invariante, se mantiene durante cada transformación.

## Estado de ejecución 

Sea $m$ el mensaje original.

La función cesarCola recibe tres elementos:
**cesarCola(m, k, acc)**

-- $m$ es la parte del mensaje que falta por procesar.
-- $k$ es el desplazador.
-- $acc$ es la parte del resultado que ya ha sido procesada.

Por lo tanto, el estado del proceso se representa mediante:

$$
s=(m,k,acc)
$$


El estado inicial donde todavía no se ha procesado ningún carácter es:

$$
s_O =(m,k,"")
$$

En otras palabras, al inicio todo el mensaje está pendiente y el acumulador está vacío.

El estado es final cuando no quedan caracteres por procesar:

$$
s_f=("",k,acc)
$$

La transformación consíste en procesar el primer carácter de $m$, agregando el resultado al acumuladr y 
continuar con el resto del mensaje.

La invariante que se utilizará es:
 
$$
\text{Inv}(m,k,acc)
\equiv
acc+f(m,k)=f(m,k)
$$

Lo que ya está guardado en $acc$, junto con el resultado de cifrar la parte del mensaje que todavía falta por
procesar, debe ser igual al cifrado del mensaje original.

Ejemplo: si una parte de msj ya fue procesada y se encuentra en acc, entonces
el resto del msj todavía está en $m$. La suma de esas dos pártes debe corresponder al resultado final.

## 1. la invariante se cumple en el estado inicial  

El estado inicial es:

$$
s_O=(m,k,"")
$$

Para comprobar que la invariante se cumple desde el comienzo, reemplazamos $m$
por $M$ y acc por el mensaje vacío:


$$
""+f(M,k)=f(M,k)
$$

Como concatenar un mensaje vacío no modificar el resultado.

$$ 
f(M,k)=f(M,k)
$$

Por lo tanto, la invariante se cumple en el estado inicial.
$$
\text{Inv}(s_O)
$$
Esto quiere decir que desde el comienzo se cumple la propiedad que queremos
conservar durante toda la ejecución.

## 2. La invariante se conserva durante el proceso.

Supongamos que todavía queda una parte del mensaje por procesar.

Lo representaremos como:

$$
m=c+r 
$$

Donde $C$ representa el primer carácter del mensaje que falta por procesar y $r$ representa el resto del mensaje.

Suponemos que antes de procesar $c$ la invariante ya se cumple:

$$
acc+f(c+r,k)=f(M,k)
$$

Ahora se debe comprobar que después de procesar el carácter $c$, la invariante continùa cumpliéndose.

**Se representa dos casos:**

## caso 1: $C$ es una letra minúscula

Cuando $c$ es una letra minúscula, el programa calcula la letra correspondiente, después
de aplicar el desplazamiento $k$.

Sea:

$$
\text{letraNueva}=\text{cifrar}(c,k)
$$

El programa agrega esta letra al acumulador $acc$ y continua con el resto del mensaje.
Por lo tanto, la transformación del estado es:

$$
(c+r,k,acc)
\rightarrow
(r,k,acc+\text{letraNueva})
$$

Esto representa exactamente lo que hace el programa: el primer carácter $c$ ya fue procesado,
se agregó su resultado al acumulador y ahora queda $r$ por procesar.

Para el nuevo estado se debe comprobar que la invariante continùa cumpliéndose:

$$
(acc+text{letraNueva})+f(r,k)=f(M,k)
$$

Por definición del cifrado cesar, cifrar primero $c$ y luego cifrar $r$ produce el mismo resultado que cifrar todo $c+r$

$$
\text{letraNueva}+f(r,k)=f(c+r,k)
$$

Por lo tanto:
$$
acc+f(c+r.k)
$$
y como inicialmente se cumple al invariante:
$$
acc+f(c+r, k)=f(M, k)
$$
Entonces:
$$
(acc+letraNueva)+f(r, k)=f(M, k)
$$

Por lo tanto, la invariante se mantiene después procesar una letra minúscula.
$$
\text{Inv}(r, k, acc+letraNueva)
$$

## Caso 2: $c$ no es una letra minúscula

Cuando $c$ es una letra minúscula, el carácter se conserva sin cambios. El programa lo agrega
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

Como $c$ no es una letra minúscula, el cifrado cesar lo mantiene sin cambios.

Por lo tanto:
$$
c+f(r, k)=(c+r, k)
$$
Entonces:
$$
acc+f(c+r, k)
$$
 y por la invariante que se tenía antes de procesar $C$

$$ 
acc+f(c+r,k)=f(M,k)
$$
Por lo tanto:
$$
(acc+c)´f(r,k)=f(M,k)
$$

Asi, la invariante también se mantiene cuando $c$ no es una letra minúscula.
$$
\text{Inv}(r, k, acc+c)
$$
En ambos casos, la transformación conserva la invariante.

## 3. Corrección en el estado final

El estado final se alcanza cuado no queda caracteres por procesar.

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

Cunado el programa encuentra que el mensaje está vacío, retorna el acumulado.

$$
P_f("",k,acc)\rightarrow acc
$$

Como acabamos de demotras que:
$$
acc=f(M, k)
$$

Se concluye que:

$$
P_f(M,k)=f(M,k)
$$

En esta parte cuando el programa termina, el acumulador contiene exactamente el resultado correcto
del cifrado cesar

## 4. el proceso alcanza la face final 

Finalmente, se debe comprobar que el programa realmente llege al esatado final.

En cada llamada recursiva, el programa toma el primer carácter del mensaje mediante
m.head y continúa con el resto del mensaje mediante $m.tail$.

Por lo tanto, el mensaje pendiente disminuye en un carácter en cada llamada:

$$
c+r\rightarrow 
$$

Después de procesar todos los carácteres, el mensaje pendiente queda vacío.

$$
m=""
$$
Por lo tanto, el proceso alcanza el estado final:

$$
s_F=("",k,acc)
$$

## Conclusión

Se ha demostrado que la invariante se cumple en el estado inicial, se conserva durante cada trasformación y permite obtener el resultado
correcto cuando se alcanza el estado final. Además, el proceso alcanza dicho estado porque en cada
llamada recursiva se elimina un carácter del mensaje pendiente.

Por último el programa cesarCola es correcto con respeto a la especificación del cifrado cesar.
