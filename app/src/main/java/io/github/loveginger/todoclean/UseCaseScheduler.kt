package io.github.loveginger.todoclean


interface UseCaseScheduler {
  fun execute(runnable: () -> Any)
}