package io.github.loveginger.todoclean

import io.github.loveginger.todoclean.util.EspressoIdlingResource
import java.util.concurrent.Executor


/**
 * Runs UseCases using a UseCaseScheduler
 */
class UseCaseHandler(private val useCaseScheduler: UseCaseScheduler) {
  companion object {
    private var INSTANCE: UseCaseHandler? = null
    val instance = INSTANCE ?: synchronized(this) {
      INSTANCE ?: UseCaseHandler(UseCaseThreadPoolScheduler).also { INSTANCE = it }
    }
  }

  fun <T : UseCase.RequestValues, R : UseCase.ResponseValue> execute(
    useCase: UseCase<T, R>,
    values: T,
    callback: UseCaseCallback<R>,
    callbackExecutor: Executor
  ) {
    useCase.requestValues = values
    useCase.useCaseCallback = CallbackWrapper(callback, callbackExecutor)

    EspressoIdlingResource.increment() // App is busy until further notice

    useCaseScheduler.execute {
      useCase.run()
      if (!EspressoIdlingResource.idlingResource.isIdleNow) {
        EspressoIdlingResource.decrement()
      }
    }
  }
}

private class CallbackWrapper<V : UseCase.ResponseValue>(
  private val callback: UseCaseCallback<V>,
  private val callbackExecutor: Executor
) : UseCaseCallback<V> {

  override fun onSuccess(response: V) {
    callbackExecutor.execute {
      callback.onSuccess(response)
    }
  }

  override fun onError() {
    callbackExecutor.execute {
      callback.onError()
    }
  }

}

