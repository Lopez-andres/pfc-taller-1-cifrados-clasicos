package taller

import scala.annotation.tailrec

/**
 * Taller 1 — cifrados clásicos con recursión.
 *
 * Solo se cifran las 26 letras minúsculas del alfabeto inglés; cualquier otro
 * carácter se copia sin cambio.
 */
class CifradosClasicos {

  type Mensaje = String
  type Clave = String

  // Una frecuencia asocia cada letra con las veces que aparece.
  type Frecuencias = List[(Char, Int)]

  val letras = 26
  val primera = 'a'.toInt

  def esMinuscula(c: Char): Boolean = c >= 'a' && c <= 'z'

  // Punto 1 -------------------------------------------------------------------

  /** César con recursión lineal: una operación pendiente por letra. */
  def cesar(m: Mensaje, k: Int): Mensaje = {
    if (m.isEmpty){ // verifica si el msj esta vacío
      "" // devuelve un msj vacío
    }else{
      val c= m.head //toma el primer carácter(letra)
      if (esMinuscula(c)){ // verifica si es minúscula
        val ubicacionInicial = c.toInt - primera // obtiene la posición de la letra
        val ubicacionNueva = ((ubicacionInicial + k ) % letras + letras)% letras // aplica el desplazamiento
        val letraNueva = (ubicacionNueva + primera).toChar //  convierte la posición en letra

        letraNueva + cesar(m.tail, k) // une la letra y sigue

      }else {
         c + cesar(m.tail, k) // mantiene la letra y sigue

      }

    }
  }

  // Punto 2 -------------------------------------------------------------------

  /**
   * El mismo César como proceso iterativo: espacio constante.
   * Cuando la función esté escrita, anótela con @tailrec: el compilador
   * comprueba que la llamada recursiva sea lo último que hace.
   */
  @tailrec
  final def cesarCola(m: Mensaje, k: Int, acc: Mensaje = ""): Mensaje = {

      if (m.isEmpty){
        acc
      }else {
        val c = m.head

        if (esMinuscula(c)){
          val ubicacionInicial = c.toInt - primera
          val ubicacionNueva = ((ubicacionInicial + k ) % letras + letras) % letras
          val letraNueva = (ubicacionNueva + primera).toChar

          cesarCola(m.tail, k, acc + letraNueva)
        }else {
          cesarCola(m.tail, k , acc + c)
        }
      }
  }

  // Punto 3 -------------------------------------------------------------------

  /**
   * Cuenta las letras minúsculas del mensaje, de mayor a menor frecuencia y,
   * en empate, en orden alfabético. El recorrido es recursivo de cola.
   */
  def frecuencias(m: Mensaje): Frecuencias = ???

  // Punto 4 -------------------------------------------------------------------

  /**
   * Supone que la letra más frecuente del mensaje cifrado es la 'e' del
   * original y devuelve la distancia entre las dos. Sin letras, cero.
   */
  def desplazamientoProbable(m: Mensaje): Int = {
    val f: Frecuencias = frecuencias(m)

    if (f.isEmpty) 0
    else {
      val letra = f.head._1
      ((letra.toInt - 'e'.toInt) % letras + letras) % letras
    }
  }

  def romperCesar(m: Mensaje): Mensaje = {
    cesar(m, -desplazamientoProbable(m))
  }

  // Punto 5 -------------------------------------------------------------------

  /**
   * Cuántos mensajes de longitud n se forman con a letras sin dos iguales
   * seguidas.
   */
  def combinaciones(n: Int, a: Int): BigInt = {
    if (n == 0) 1
    else if (n == 1) a
    else (a-1) * combinaciones(n - 1, a)
  }

  /**
   * Vigenère: cada letra se corre según la letra de la clave que le toca. Lo
   * que no es letra minúscula se copia y no consume clave.
   */
  def vigenere(m: Mensaje, clave: Clave): Mensaje = {
    def auxClave(m: Mensaje, claveRestante: Clave): Mensaje = {
      if (m.isEmpty) ""
      else {
        val claveActual = if (claveRestante.isEmpty) clave else claveRestante

        val letraMensaje = m.head
        if (esMinuscula(letraMensaje)) {
          val indiceClave = ((letraMensaje - 'a') + (claveActual.head.toInt - 'a'.toInt) % letras + letras) % letras
          (indiceClave + 'a').toChar + auxClave(m.tail, claveRestante)
        } else letraMensaje + auxClave(m.tail, claveRestante)
      }
    }

    if (clave.isEmpty) m else auxClave(m, clave)
  }
}
