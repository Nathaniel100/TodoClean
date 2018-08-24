package io.github.loveginger.todoclean.tasks.domain.usecase

import io.github.loveginger.todoclean.UseCase
import io.github.loveginger.todoclean.data.TasksRepository

class ClearCompleteTasks(private val tasksRepository: TasksRepository) :
  UseCase<ClearCompleteTasks.RequestValues, ClearCompleteTasks.ResponseValue>() {

  override fun executeUseCase(requestValues: RequestValues) {
    tasksRepository.clearCompletedTasks()
    useCaseCallback?.onSuccess(ResponseValue())
  }

  class RequestValues : UseCase.RequestValues

  class ResponseValue : UseCase.ResponseValue

}

