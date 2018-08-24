package io.github.loveginger.todoclean.util

import android.support.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger


/**
 *
 */
class SimpleCountingIdlingResource(private val resourceName: String) : IdlingResource {
  private val counter = AtomicInteger(0)

  private var resourceCallback: IdlingResource.ResourceCallback? = null

  override fun getName(): String {
    return resourceName;
  }

  override fun isIdleNow(): Boolean {
    return counter.get() == 0
  }

  override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback?) {
    this.resourceCallback = resourceCallback
  }

  fun increment() {
    counter.getAndIncrement()
  }

  fun decrement() {
    val counterVal = counter.decrementAndGet()
    if (counterVal == 0) {
      resourceCallback?.onTransitionToIdle()
    }

    if (counterVal < 0) {
      throw IllegalStateException("Counter has been corrupted")
    }
  }

}