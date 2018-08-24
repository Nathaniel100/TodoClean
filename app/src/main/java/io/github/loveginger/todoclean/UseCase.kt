package io.github.loveginger.todoclean

interface UseCaseCallback<R> {
  fun onSuccess(response: R)
  fun onError()
}

abstract class UseCase<Q : UseCase.RequestValues, P : UseCase.ResponseValue> {
  var requestValues: Q? = null
  var useCaseCallback: UseCaseCallback<P>? = null

  internal fun run() {
    if (requestValues != null) {
      executeUseCase(requestValues!!)
    }
  }

  protected abstract fun executeUseCase(requestValues: Q)

  /**
   * Data passed to a request
   */
  interface RequestValues

  /**
   * Data received from a request
   */
  interface ResponseValue
}