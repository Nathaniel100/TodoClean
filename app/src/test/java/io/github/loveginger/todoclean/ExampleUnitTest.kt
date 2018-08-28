package io.github.loveginger.todoclean

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

  private var map = mutableMapOf<Int, String>()

  @Before
  fun setUp() {
    for (i in 1..10) {
      map[i] = i.toString()
    }

  }

  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)

    map = map.filterKeys { it % 2 != 0 }.toMutableMap()

//    for ((k, v) in map) {
//      if (k % 2 == 0) {
//        map.remove(k)
//      }
//    }

    for (i in 1..10) {
      if (i % 2 == 0) {
        assertNull(map[i])
      }
    }
  }
}
