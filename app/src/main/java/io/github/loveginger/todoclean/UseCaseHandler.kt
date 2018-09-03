package io.github.loveginger.todoclean

import io.github.loveginger.todoclean.util.EspressoIdlingResource


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
    callback: UseCaseCallback<R>
  ) {
    useCase.requestValues = values
    useCase.useCaseCallback = UiCallbackWrapper(callback, this)

    EspressoIdlingResource.increment() // App is busy until further notice

    useCaseScheduler.execute {
      useCase.run()
      if (!EspressoIdlingResource.idlingResource.isIdleNow) {
        EspressoIdlingResource.decrement()
      }
    }
  }

  fun <V : UseCase.ResponseValue> notifyResponse(response: V, callback: UseCaseCallback<V>) {
    useCaseScheduler.notifyResponse(response, callback)
  }

  fun <V : UseCase.ResponseValue> notifyError(callback: UseCaseCallback<V>) {
    useCaseScheduler.notifyError(callback)
  }
}

private class UiCallbackWrapper<V : UseCase.ResponseValue>(
  private val callback: UseCaseCallback<V>,
  private val useCaseHandler: UseCaseHandler
) : UseCaseCallback<V> {

  override fun onSuccess(response: V) {
    useCaseHandler.notifyResponse(response, callback)
  }

  override fun onError() {
    useCaseHandler.notifyError(callback)
  }

}

