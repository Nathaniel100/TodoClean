package io.github.loveginger.todoclean

import android.os.Handler
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


object UseCaseThreadPoolScheduler : UseCaseScheduler {
  private const val POOL_SIZE = 2
  private const val MAX_POOL_SIZE = 4
  private const val TIMEOUT = 30L

  private val handler = Handler()
  private val threadPoolExecutor = ThreadPoolExecutor(
    POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
    TimeUnit.SECONDS, ArrayBlockingQueue<Runnable>(POOL_SIZE)
  )

  override fun execute(runnable: () -> Any) {
    threadPoolExecutor.execute {
      runnable()
    }
  }

  override fun <V : UseCase.ResponseValue> notifyResponse(
    response: V,
    useCaseCallback: UseCaseCallback<V>
  ) {
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