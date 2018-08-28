package io.github.loveginger.todoclean.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import io.github.loveginger.todoclean.tasks.domain.model.Task


@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

  abstract fun taskDao(): TasksDao

  companion object {
    private var INSTANCE: TodoDatabase? = null

    fun getInstance(context: Context): TodoDatabase = INSTANCE ?: synchronized(this) {
      INSTANCE ?: createDatabase(context).also { INSTANCE = it }
    }

    private fun createDatabase(context: Context): TodoDatabase =
      Room.databaseBuilder(context.applicationContext, TodoDatabase::class.java, "Task.db")
        .build()
  }
}