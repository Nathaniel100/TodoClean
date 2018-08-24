package io.github.loveginger.todoclean.tasks.domain.usecase

import io.github.loveginger.todoclean.UseCase
import io.github.loveginger.todoclean.data.TasksDataSource
import io.github.loveginger.todoclean.data.TasksRepository
import io.github.loveginger.todoclean.tasks.TasksFilterType
import io.github.loveginger.todoclean.tasks.domain.filter.FilterFactory
import io.github.loveginger.todoclean.tasks.domain.model.Task

class GetTasks(private val tasksRepository: TasksRepository) :
  UseCase<GetTasks.RequestValues, GetTasks.ResponseValue>() {

  override fun executeUseCase(requestValues: RequestValues) {
    if (requestValues.forceUpdate) {
      tasksRepository.refreshTasks()
    }
    tasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
      override fun onTasksLoaded(tasks: List<Task>) {
        val taskFilter = FilterFactory.create(requestValues.tasksFilterType)
        val tasksFiltered = taskFilter.filter(tasks)
        useCaseCallback?.onSuccess(ResponseValue(tasksFiltered))
      }

      override fun onDataNotAvailable() {
        useCaseCallback?.onError()
      }
    })
  }

  class RequestValues(val forceUpdate: Boolean, val tasksFilterType: TasksFilterType) :
    UseCase.RequestValues

  class ResponseValue(val tasks: List<Task>) : UseCase.ResponseValue
}