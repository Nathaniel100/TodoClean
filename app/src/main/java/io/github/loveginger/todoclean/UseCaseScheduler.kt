package io.github.loveginger.todoclean


interface UseCaseScheduler {
  fun execute(runnable: () -> Any)

  fun <V : UseCase.ResponseValue> notifyResponse(response: V, useCaseCallback: UseCaseCallback<V>)

  fun <V : UseCase.ResponseValue> notifyError(useCaseCallback: UseCaseCallback<V>)
}