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
  def cesar(m: Mensaje, k: Int): Mensaje = ???

  // Punto 2 -------------------------------------------------------------------

  /**
   * El mismo César como proceso iterativo: espacio constante.
   * Cuando la función esté escrita, anótela con @tailrec: el compilador
   * comprueba que la llamada recursiva sea lo último que hace.
   */
  final def cesarCola(m: Mensaje, k: Int, acc: Mensaje = ""): Mensaje = ???

  // Punto 3 -------------------------------------------------------------------

  /**
   * Cuenta las letras minúsculas del mensaje, de mayor a menor frecuencia y,
   * en empate, en orden alfabético. El recorrido es recursivo de cola.
   *
   */
  def frecuencias(m: Mensaje): Frecuencias = {
    // Cuenta cuantas veces aparece una letra específica en el mensaje
    @tailrec
    def calcularLetras(mensajeSobra: Mensaje, letra: Char, totalLetra:Int): Int = {
      // Caso base: Cuando no hay caracteres por revisar
      if (mensajeSobra.isEmpty) {
        totalLetra
      }
      // Si el caracter actual coincide con la letra buscada
      // aumenta el acumulador en 1
      else if (mensajeSobra.head == letra) {
          calcularLetras(mensajeSobra.tail, letra, totalLetra + 1)
      }
      // Si no coincide, continua con el mismo acumulador
      else{
        calcularLetras(mensajeSobra.tail,letra, totalLetra)
      }
    }
    // Recorre las letras desde 'a' hasta la 'z'
    // Construyendo la lista de frecuencias
    @tailrec
    def examinarLetra(letra: Char, guardar: Frecuencias): Frecuencias = {
      // Caso base: Cuando se revisaron todas las letras
      if(letra > 'z'){
        // Ordena de mayor a menor la frecuencia
        // Si hay frecuencias iguales, se ordena alfabéticamente
        guardar.sortWith((a,b) =>
            if(a._2 == b._2){
              a._1 < b._1
            }
            else{
              a._2 > b._2
             }
        )
      }
      else{
          // Calcula cuantas veces aparece la letra actual
          val totalLetra: Int = calcularLetras(m, letra, 0)
          // Si aparece al menos una vez
          // Se agrega la letra con su frecuencia
          if (totalLetra>0){
            val listaNueva: Frecuencias = guardar :+ (letra, totalLetra)
            // Avanza a la siguente letra del alfabeto
            examinarLetra((letra.toInt + 1 ).toChar, listaNueva)
          }
          // Si no aparece, no se agrega a la lista
          else{
            // Avanza a la siguiente letra
            examinarLetra((letra.toInt + 1) .toChar, guardar)
          }
      }

    }
    // La revisión comienza desde la letra 'a'
    // con una lista de frecuencias vacías
    examinarLetra('a',List())
  }

  // Punto 4 -------------------------------------------------------------------

  /**
   * Supone que la letra más frecuente del mensaje cifrado es la 'e' del
   * original y devuelve la distancia entre las dos. Sin letras, cero.
   */
  def desplazamientoProbable(m: Mensaje): Int = ???

  def romperCesar(m: Mensaje): Mensaje = ???

  // Punto 5 -------------------------------------------------------------------

  /**
   * Cuántos mensajes de longitud n se forman con a letras sin dos iguales
   * seguidas.
   */
  def combinaciones(n: Int, a: Int): BigInt = ???

  /**
   * Vigenère: cada letra se corre según la letra de la clave que le toca. Lo
   * que no es letra minúscula se copia y no consume clave.
   */
  def vigenere(m: Mensaje, clave: Clave): Mensaje = ???
}
