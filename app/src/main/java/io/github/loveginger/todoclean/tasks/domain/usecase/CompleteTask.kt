package io.github.loveginger.todoclean.tasks.domain.usecase

import io.github.loveginger.todoclean.UseCase
import io.github.loveginger.todoclean.data.TasksRepository

class CompleteTask(private val tasksRepository: TasksRepository) :
  UseCase<CompleteTask.RequestValues, CompleteTask.ResponseValue>() {

  override fun executeUseCase(requestValues: RequestValues) {
    tasksRepository.completeTask(requestValues.completedTask)
    useCaseCallback?.onSuccess(ResponseValue())
  }

  class RequestValues(val completedTask: String) : UseCase.RequestValues

  class ResponseValue : UseCase.ResponseValue

}