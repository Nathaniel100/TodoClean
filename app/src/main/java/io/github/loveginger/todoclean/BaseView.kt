package io.github.loveginger.todoclean


interface BaseView<in T : BasePresenter> {
  fun setPresenter(presenter: T)
}