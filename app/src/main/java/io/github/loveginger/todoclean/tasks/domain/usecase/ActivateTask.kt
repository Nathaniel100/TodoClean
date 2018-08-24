package io.github.loveginger.todoclean.tasks.domain.usecase

import io.github.loveginger.todoclean.UseCase
import io.github.loveginger.todoclean.data.TasksRepository


class ActivateTask(private val tasksRepository: TasksRepository) :
  UseCase<ActivateTask.RequestValues, ActivateTask.ResponseValue>() {

  override fun executeUseCase(requestValues: RequestValues) {
    tasksRepository.activateTask(requestValues.activateTask)
    useCaseCallback?.onSuccess(ResponseValue())
  }

  class RequestValues(val activateTask: String) : UseCase.RequestValues


  class ResponseValue : UseCase.ResponseValue

}