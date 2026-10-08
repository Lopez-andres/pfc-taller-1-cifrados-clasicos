package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

/**
 * Cada ejemplo del enunciado es una prueba. Si el enunciado promete un valor,
 * aquí se comprueba que la solución lo produce.
 */
@RunWith(classOf[JUnitRunner])
class CifradosClasicosTest extends AnyFunSuite {

  val c = new CifradosClasicos()
  import c._

  // Punto 1: ejemplos del enunciado -------------------------------------------

  test("cesar: casa con 3 da fdvd") { assert(cesar("casa", 3) == "fdvd") }
  test("cesar: fdvd con -3 vuelve a casa") { assert(cesar("fdvd", -3) == "casa") }
  test("cesar: hola mundo con 1") { assert(cesar("hola mundo", 1) == "ipmb nvoep") }
  test("cesar: zzz con 1 da aaa") { assert(cesar("zzz", 1) == "aaa") }
  test("cesar: 29 es lo mismo que 3") { assert(cesar("abc", 29) == "def") }
  test("cesar: el mensaje vacío sale vacío") { assert(cesar("", 5) == "") }

  test("cesar: la puntuación y los dígitos pasan sin cambio") {
    assert(cesar("ab, 12!", 1) == "bc, 12!")
  }

  test("cesar: las mayúsculas no se cifran") {
    assert(cesar("Casa", 3) == "Cdvd")
  }

  test("cesar: cifrar y descifrar es la identidad") {
    assert(cesar(cesar("un mensaje cualquiera", 11), -11) == "un mensaje cualquiera")
  }


  test("cesar: desplaza una palabra completa con un desplazamiento positivo"){
    assert(cesar("universidad",7) == "bupclyzpkhk")
  }

  test("cesar: un desplazamiento negativo cruza el inicio del alfabeto"){
    assert(cesar("xyz abc", -4) == "tuv wxy")
  }

  test("cesar: conserva la mayúsculas, números y signos mientras cifra el mensaje"){
    assert(cesar("Cafe 2026!", 9) == "Cjon 2026!")
  }

  test("cesar: combina vuelta del alfabeto y desplazamiento negativo"){
    assert(cesar("zzxy abcd", -27) =="yywx zabc")
  }

  test("cesar: un desplazamiento mayor que 26 se reduce correctamente"){
    assert(cesar("programa", 55) == "surjudpd")
  }

  // Punto 2 -------------------------------------------------------------------

  test("cesarCola: casa con 3 da fdvd") { assert(cesarCola("casa", 3) == "fdvd") }
  test("cesarCola: hola mundo con 1") { assert(cesarCola("hola mundo", 1) == "ipmb nvoep") }
  test("cesarCola: con 0 el mensaje no cambia") { assert(cesarCola("abc", 0) == "abc") }

  test("cesarCola: da lo mismo que la versión lineal") {
    val casos = List(("casa", 3), ("hola mundo", 1), ("zzz", 1), ("abc", 29),
                     ("", 5), ("ab, 12!", -4))
    assert(casos.forall { case (m, k) => cesarCola(m, k) == cesar(m, k) })
  }

  test("cesarCola: funciona con diferentes desplazamientos y conserva caracteres") {
    val casos = List(
      ("abc xyz", 1),
      ("hello, world!", 13),
      ("scala 2026", 26),
      ("xyz abc", -2)
    )
    val esperados = List(
      "bcd yza",
      "uryyb, jbeyq!",
      "scala 2026",
      "vwx yza"
    )
    assert(casos.map { case (m, k) => cesarCola(m, k) } == esperados) //map aplica cesarCola a cada caso y genera la lista de resultados
    // que luego se compara con la lista espera
  }
    test("cesarCola: aguanta un mensaje largo sin desbordar la pila") {
      val largo = "abcdefghij" * 20000
      assert(cesarCola(largo, 1).length == largo.length)
    }

    test("cesarCola: cruza el final del alfabeto con varia letras") {
      assert(cesarCola("wzyz", 3) == "zcbc")
    }

    test("cesarCola: cruza el inicio del alfabeto con un desplazamiento negativo") {
      assert(cesarCola("abcd", -3) == "xyza")
    }

    test("cesarCola: un desplazamiento mayor que 26 produce el resultado equivalente") {
      assert(cesarCola("programacion", 55) == "surjudpdflrq")
    }

    test("cesarCola: conserva mayúsculas, números y signos") {
      assert(cesarCola("Cafe 2050!", 9) == "Cjon 2050!")
    }
    test("cesarCola: combina caracteres sin cifrar con letras que cruzan el alfabeto") {
      assert(cesarCola("zz, aa!", 1) == "aa, bb!")
    }
    // Punto 3 -------------------------------------------------------------------

    test("frecuencias: casa") {
      assert(frecuencias("casa") == List(('a', 2), ('c', 1), ('s', 1)))
    }

    test("frecuencias: aabbbc") {
      assert(frecuencias("aabbbc") == List(('b', 3), ('a', 2), ('c', 1)))
    }

    test("frecuencias: hola mundo") {
      assert(frecuencias("hola mundo") ==
        List(('o', 2), ('a', 1), ('d', 1), ('h', 1), ('l', 1), ('m', 1),
          ('n', 1), ('u', 1)))
    }

    test("frecuencias: el mensaje vacío no tiene letras") {
      assert(frecuencias("") == List())
    }

    test("frecuencias: un mensaje sin letras no tiene frecuencias") {
      assert(frecuencias("123 !?") == List())
    }

    test("frecuencias: en empate manda el orden alfabético") {
      assert(frecuencias("ba") == List(('a', 1), ('b', 1)))
    }

    // Nuevos test punto 3
    test("Extra 1 - Ignora mayusculas"){
      assert(frecuencias("aAcCBb") == List(('a',1), ('b',1), ('c',1)))
    }

    test("Extra 2 - Una sola letra repetida"){
      assert(frecuencias("zzzzz") == List(('z', 5)))
    }

    test("Extra 3 - Ordena por mayor frecuencia"){
      assert(frecuencias("qqqpp") == List(('q', 3), ('p', 2)))
    }

    test("Extra 4 - Empate de 3 letras en orden alfabético "){
      assert(frecuencias("ccbbaa") == List(('a', 2), ('b', 2), ('c', 2)))
    }

    test("Extra 5 - Combinación de frecuencia y empate alfabético ") {
      assert(frecuencias("rrssstt") == List(('s', 3), ('r', 2), ('t', 2)))
    }
      // Punto 4 -------------------------------------------------------------------

      test("desplazamientoProbable: h está 3 después de e") {
        assert(desplazamientoProbable("h") == 3)
      }

      test("desplazamientoProbable: hhhaa, con h como la más frecuente") {
        assert(desplazamientoProbable("hhhaa") == 3)
      }

      test("desplazamientoProbable: sin letras da 0") {
        assert(desplazamientoProbable("123") == 0)
      }

      test("desplazamientoProbable: en empate manda la primera alfabéticamente") {
        // 'a' y 'h' aparecen tres veces; gana 'a', que está 22 después de 'e'.
        assert(desplazamientoProbable("hhhaaa") == 22)
      }

      test("romperCesar: recupera un mensaje con suficientes letras e") {
        val original = "el mensaje secreto"
        assert(romperCesar(cesar(original, 7)) == original)
      }

      test("romperCesar: el método falla cuando la e no es la más frecuente") {
        // En este mensaje la letra más frecuente es la 'a', no la 'e'.
        val original = "cada casa amarilla"
        assert(romperCesar(cesar(original, 7)) != original)
      }

      // test - andres
      test("desplazamientoProbable: si la letra más frecuente es la e, el desplazamiento es 0") {
        assert(desplazamientoProbable("eee") == 0)
      }

      test("desplazamientoProbable: una z dominante da 21") {
        assert(desplazamientoProbable("zzzy") == 21)
      }

      test("desplazamientoProbable: en un empate gana la b y el resultado envuelve a 23") {
        // b y c aparecen una vez; gana b, que está antes de la e, así que (1 - 4) mod 26 = 23.
        assert(desplazamientoProbable("cb") == 23)
      }

      test("desplazamientoProbable: ignora los espacios, signos y digitos") {
        assert(desplazamientoProbable("xx, 99!") == 19)
      }

      test("romperCesar: recupera varios mensajes con la e como letra dominante") {
        val originales = List(
          "esta es una prueba de eventos de verdad",
          "tres tristes tigres comen en el desierto",
          "el elefante se detiene en el verde bosque"
        )
        val desplazamientos = List(1, 5, 13, 25, 26, -3, 30)
        assert(originales.forall(o => desplazamientos.forall(k => romperCesar(cesar(o, k)) == o)))
      }

      test("romperCesar: descifra hvh como ese y conserva los signos") {
        assert(romperCesar("hvh") == "ese")
        assert(romperCesar("hvh, hvh!") == "ese, ese!")
      }

      test("romperCesar: falla por empate cuando la e pierde el desempate alfabético") {
        // ez con k = 2 da gb; en gb gana la b, el desplazamiento estimado es 23 y sale je.
        assert(romperCesar(cesar("ez", 2)) == "je")
        assert(romperCesar(cesar("ez", 2)) != "ez")
      }

      // Punto 5 -------------------------------------------------------------------

      test("combinaciones: con longitud 0 hay un mensaje, el vacío") {
        assert(combinaciones(0, 26) == BigInt(1))
      }

      test("combinaciones: con longitud 1 hay tantos como letras") {
        assert(combinaciones(1, 26) == BigInt(26))
      }

      test("combinaciones: 3 letras sobre 26 dan 16250") {
        assert(combinaciones(3, 26) == BigInt(16250))
      }

      test("combinaciones: 2 letras sobre un alfabeto de 2 dan 2") {
        assert(combinaciones(2, 2) == BigInt(2))
      }

      test("combinaciones: crece según la recurrencia") {
        assert(combinaciones(5, 4) == BigInt(3) * combinaciones(4, 4))
      }

      // test andres
      test("combinaciones: 4 letras y longitud 4 dan 24") {
        assert(combinaciones(4, 3) == BigInt(24))
      }

      test("combinaciones: con 2 letras solo hay mensajes que alternan") {
        assert(combinaciones(5, 2) == BigInt(2))
      }

      test("combinaciones: con una sola letra no hay mensajes de más de una letra") {
        assert(combinaciones(3, 1) == BigInt(0))
      }

      test("combinaciones: el resultado supera el rango de Int") {
        assert(combinaciones(20, 26) == BigInt(26) * BigInt(25).pow(19))
      }

      test("combinaciones: coincide con la forma cerrada a por (a-1) a la n-1") {
        val casos = List((1, 5), (2, 5), (6, 3), (8, 4))
        assert(casos.forall { case (n, a) => combinaciones(n, a) == BigInt(a) * BigInt(a - 1).pow(n - 1) })
      }

      test("vigenere: ab c con la clave bd, el espacio no gasta clave") {
        assert(vigenere("ab c", "bd") == "be d")
      }

      test("vigenere: una clave más larga que el mensaje solo usa su inicio") {
        assert(vigenere("hi", "zzzz") == "gh")
      }

      test("vigenere: la clave se reinicia al terminarse") {
        assert(vigenere("aaaaa", "abc") == "abcab")
      }

      test("vigenere: no cifra mayúsculas ni signos y no gasta clave en ellos") {
        assert(vigenere("Hola, Mundo!", "ab") == "Homa, Mvneo!")
      }

      test("vigenere: varios espacios seguidos se copian sin consumir clave") {
        assert(vigenere("  a b", "bc") == "  b d")
      }

      test("vigenere: ataque con la clave sol") {
        assert(vigenere("ataque", "sol") == "shliip")
      }

      test("vigenere: hola mundo con la clave ab") {
        assert(vigenere("hola mundo", "ab") == "hplb mvneo")
      }

      test("vigenere: con la clave vacía el mensaje no cambia") {
        assert(vigenere("casa", "") == "casa")
      }

      test("vigenere: el espacio no consume letra de la clave") {
        // Sin el espacio la clave iría corrida y la m se cifraría con b.
        assert(vigenere("hola mundo", "ab").charAt(5) == 'm')
      }

      test("vigenere: con una clave de una sola letra es un César") {
        assert(vigenere("hola mundo", "d") == cesar("hola mundo", 3))
      }

}



