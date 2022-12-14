package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskModel
import com.softyorch.taskapp.data.repository.task.TaskRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class GetTaskIdTest {

    @RelaxedMockK
    private lateinit var repository: TaskRepository

    lateinit var getTaskId: GetTaskId

    private val myTask = TaskModel(
        id = UUID.randomUUID(),
        title = "Test",
        description = "Testing",
        author = "Jorge"

    )

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getTaskId = GetTaskId(repository = repository)
    }

    @Test
    fun `when the use case is called upon to return a task`() = runBlocking {
        //Given
        coEvery { repository.getTaskById(idTask = myTask.id.toString()) } returns myTask

        //When
        val result = getTaskId.invoke(taskId = myTask.id.toString())

        //Then
        coVerify(exactly = 1) { repository.getTaskById(any()) }
        assert(result == myTask.mapToTaskModelUseCase())
    }

}