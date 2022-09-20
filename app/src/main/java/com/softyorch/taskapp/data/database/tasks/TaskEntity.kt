package com.softyorch.taskapp.data.database.tasks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.softyorch.taskapp.data.database.tasks.TaskDatabase.Companion.TASK_DB_NAME
import com.softyorch.taskapp.utils.*
import java.time.Instant
import java.util.*

@Entity(tableName = TASK_DB_NAME)
data class TaskEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = TITLE) var title: String,
    @ColumnInfo(name = DESCRIPTION) var description: String,
    @ColumnInfo(name = AUTHOR) val author: String,
    @ColumnInfo(name = ENTRY_DATE) val entryDate: Date = Date.from(Instant.now()),
    @ColumnInfo(name = FINISH_DATE) var finishDate: Date? = null,
    @ColumnInfo(name = CHECK_STATE) var checkState: Boolean = false
)