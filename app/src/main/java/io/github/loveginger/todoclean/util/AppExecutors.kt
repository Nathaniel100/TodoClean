package io.github.loveginger.todoclean.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors(
  val diskIo: Executor,
  val networkIo: Executor,
  val mainThread: Executor
) {
  constructor() : this(
    DiskIoThreadExecutor(),
    Executors.newFixedThreadPool(THREAD_COUNT),
    MainThreadExecutor()
  )

  private class MainThreadExecutor : Executor {
    private val handler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable) {
      handler.post(command)
    }
  }

  companion object {
    private const val THREAD_COUNT = 3
  }
}


