package com.softyorch.taskapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.*

@Entity(tableName = "tasks_tbl")
data class Task(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "task_title")
    val title: String,

    @ColumnInfo(name = "task_description")
    val description: String,

    @ColumnInfo(name = "task_author")
    val author: String,

    @ColumnInfo(name = "task_entry_date")
    val entryDate: Date = Date.from(Instant.now()),

    @ColumnInfo(name = "task_check_state")
    val checkState: Boolean
)