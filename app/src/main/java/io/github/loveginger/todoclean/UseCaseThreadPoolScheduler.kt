package io.github.loveginger.todoclean

import android.os.Handler
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class UseCaseThreadPoolScheduler : UseCaseScheduler {
  companion object {
    const val POOL_SIZE = 2
    const val MAX_POOL_SIZE = 4
    const val TIMEOUT = 30L
  }

  private val handler = Handler()
  private val threadPoolExecutor = ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
      TimeUnit.SECONDS, ArrayBlockingQueue<Runnable>(POOL_SIZE))

  override fun execute(runnable: () -> Any) {
    threadPoolExecutor.execute {
      runnable()
    }
  }

  override fun <V : UseCase.ResponseValue> notifyResponse(response: V,
      useCaseCallback: UseCaseCallback<V>) {
    handler.post {
      useCaseCallback.onSuccess(response)
    }
  }

  override fun <V : UseCase.ResponseValue> notifyError(useCaseCallback: UseCaseCallback<V>) {
    handler.post {
      useCaseCallback.onError()
    }
  }
}