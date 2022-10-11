package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.task.TaskRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class GetAllTaskTest {

    @RelaxedMockK
    private lateinit var repository: TaskRepository

    lateinit var getAllTask: GetAllTask

    private val id = UUID.randomUUID()

    private var taskFlowList = flow<List<TaskEntity>> {
        listOf(TaskEntity(id = id, title = "test", description = "Testing", author = "jorge"),
        TaskEntity(
            id = UUID.randomUUID(),
            title = "test2",
            description = "Testing, second part",
            author = "jorge"
        ))
    }

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getAllTask = GetAllTask(repository = repository)
    }

    @Test
    fun `all tasks are requested`() = runBlocking {
        //Given
        coEvery { repository.getAllTaskFromDatabase() } returns emptyFlow()

        //When
        getAllTask.invoke()

        //Then
        coVerify(exactly = 1) { repository.getAllTaskFromDatabase() }
    }

}