package io.github.loveginger.todoclean

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


object UseCaseThreadPoolScheduler : UseCaseScheduler {
  private const val POOL_SIZE = 2
  private const val MAX_POOL_SIZE = 4
  private const val TIMEOUT = 30L

  private val threadPoolExecutor = ThreadPoolExecutor(
    POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
    TimeUnit.SECONDS, ArrayBlockingQueue<Runnable>(POOL_SIZE)
  )

  override fun execute(runnable: () -> Any) {
    threadPoolExecutor.execute {
      runnable()
    }
  }
}