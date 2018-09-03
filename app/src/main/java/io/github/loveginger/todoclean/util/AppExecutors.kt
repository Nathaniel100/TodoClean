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
    override fun execute(command: Runnable) {
      if (currentIsMainThread()) {
        command.run()
      } else {
        val handler = Handler(Looper.getMainLooper())
        handler.post(command)
      }
    }
  }

  companion object {
    private const val THREAD_COUNT = 3

    private fun currentIsMainThread() = Thread.currentThread() == Looper.getMainLooper().thread
  }
}


