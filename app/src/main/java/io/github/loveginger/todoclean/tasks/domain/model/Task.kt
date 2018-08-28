package io.github.loveginger.todoclean.tasks.domain.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "task")
data class Task(
  @PrimaryKey
  @ColumnInfo(name = "entryid")
  val id: String,
  @ColumnInfo(name = "title")
  val title: String,
  @ColumnInfo(name = "description")
  val description: String,
  @ColumnInfo(name = "completed")
  val completed: Boolean
) {
  val active: Boolean
    get() = !completed
  val empty: Boolean
    get() = title.isEmpty() && description.isEmpty()
}