package io.github.loveginger.todoclean.data.local

import android.arch.persistence.room.*
import io.github.loveginger.todoclean.tasks.domain.model.Task


@Dao
interface TasksDao {
  @Query("SELECT * FROM task")
  fun getTasks(): List<Task>

  @Query("SELECT * FROM task WHERE entryid = :taskId")
  fun getTaskById(taskId: String): Task?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertTask(task: Task)

  @Update
  fun updateTask(task: Task)

  @Query("UPDATE task SET completed = :completed WHERE entryid = :taskId")
  fun updateCompleted(taskId: String, completed: Boolean)

  @Query("DELETE FROM task WHERE entryid = :taskId")
  fun deleteTaskById(taskId: String): Int

  @Query("DELETE FROM task")
  fun deleteTasks()

  @Query("DELETE FROM task WHERE completed = 1")
  fun deleteCompletedTasks(): Int
}