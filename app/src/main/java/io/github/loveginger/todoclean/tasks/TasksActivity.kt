package io.github.loveginger.todoclean.tasks

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.NavigationView
import android.support.test.espresso.IdlingResource
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.github.loveginger.todoclean.Injection
import io.github.loveginger.todoclean.R
import io.github.loveginger.todoclean.util.EspressoIdlingResource
import io.github.loveginger.todoclean.util.addFragmentToActivity
import kotlinx.android.synthetic.main.tasks_act.*


class TasksActivity : AppCompatActivity() {

  companion object {
    private const val CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY"
  }

  private lateinit var tasksPresenter: TasksPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.tasks_act)

    setSupportActionBar(toolbar)
    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    drawer_layout.setStatusBarBackground(R.color.colorPrimaryDark)
    if (nav_view != null) {
      setupDrawerContent(nav_view)
    }
    var tasksFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as TasksFragment?
    if (tasksFragment == null) {
      tasksFragment = TasksFragment.newInstance()
      addFragmentToActivity(tasksFragment, R.id.contentFrame)
    }

    tasksPresenter = TasksPresenter(
      Injection.provideUseCaseHandler(),
      tasksFragment,
      Injection.provideGetTasks(applicationContext),
      Injection.provideCompleteTasks(applicationContext),
      Injection.provideActivateTask(applicationContext),
      Injection.provideClearCompleteTasks(applicationContext)
    )

    if (savedInstanceState != null) {
      val currentFiltering =
        savedInstanceState.getSerializable(CURRENT_FILTERING_KEY) as TasksFilterType
      tasksPresenter.setFiltering(currentFiltering)
    }
  }

  override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
    outState?.putSerializable(CURRENT_FILTERING_KEY, tasksPresenter.getFiltering())
    super.onSaveInstanceState(outState, outPersistentState)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == android.R.id.home) {
      drawer_layout.openDrawer(GravityCompat.START)
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  private fun setupDrawerContent(navigationView: NavigationView) {
    navigationView.setNavigationItemSelectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.list_navigation_menu_item -> {
          // Do nothing
        }
        R.id.statistics_navigation_menu_item -> TODO("StatisticsActivity")
        else -> {
        }
      }
      menuItem.isChecked = true
      drawer_layout.closeDrawers()
      true
    }
  }

  @VisibleForTesting
  val countingIdlingResource: IdlingResource
    get() = EspressoIdlingResource.idlingResource

}