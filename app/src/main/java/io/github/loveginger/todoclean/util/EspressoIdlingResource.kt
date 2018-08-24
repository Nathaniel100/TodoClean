package io.github.loveginger.todoclean.util

object EspressoIdlingResource {

  private val resource = "GLOBAL"

  val idlingResource = SimpleCountingIdlingResource(resource)

  fun increment() = idlingResource.increment()

  fun decrement() = idlingResource.increment()

}