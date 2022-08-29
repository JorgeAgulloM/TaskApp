package com.softyorch.taskapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.*

@Entity(tableName = "tasks_tbl")
data class Task(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "task_title") var title: String,
    @ColumnInfo(name = "task_description") var description: String,
    @ColumnInfo(name = "task_author") val author: String,
    @ColumnInfo(name = "task_entry_date") val entryDate: Date = Date.from(Instant.now()),
    @ColumnInfo(name = "task_finish_date") var finishDate: Date? = null,
    @ColumnInfo(name = "task_check_state") var checkState: Boolean = false
)