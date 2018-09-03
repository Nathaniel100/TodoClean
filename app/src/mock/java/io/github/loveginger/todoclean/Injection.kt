package io.github.loveginger.todoclean

import android.content.Context
import io.github.loveginger.todoclean.data.FakeTasksRemoteDataSource
import io.github.loveginger.todoclean.data.TasksRepository
import io.github.loveginger.todoclean.data.local.TasksLocalDataSource
import io.github.loveginger.todoclean.data.local.TodoDatabase
import io.github.loveginger.todoclean.tasks.domain.usecase.ActivateTask
import io.github.loveginger.todoclean.tasks.domain.usecase.ClearCompleteTasks
import io.github.loveginger.todoclean.tasks.domain.usecase.CompleteTask
import io.github.loveginger.todoclean.tasks.domain.usecase.GetTasks
import io.github.loveginger.todoclean.util.AppExecutors


object Injection {

  fun provideTasksRepository(context: Context): TasksRepository {
    val database = TodoDatabase.getInstance(context)
    return TasksRepository.getInstance(
      FakeTasksRemoteDataSource,
      TasksLocalDataSource.getInstance(database.taskDao(), AppExecutors())
    )
  }

  fun provideUseCaseHandler(): UseCaseHandler {
    return UseCaseHandler.instance
  }

  fun provideGetTasks(context: Context): GetTasks {
    return GetTasks(provideTasksRepository(context))
  }

  fun provideCompleteTasks(context: Context): CompleteTask {
    return CompleteTask(provideTasksRepository(context))
  }

  fun provideActivateTask(context: Context): ActivateTask {
    return ActivateTask(provideTasksRepository(context))
  }

  fun provideClearCompleteTasks(context: Context): ClearCompleteTasks {
    return ClearCompleteTasks(provideTasksRepository(context))
  }

}
