package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository
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

    private val myTask = TaskEntity(
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
    fun `cuando se llame al caso de uso para que devuelva una task`() = runBlocking {
        //Given
        coEvery { repository.getTaskById(idTask = myTask.id.toString()) } returns myTask

        //When
        val result = getTaskId.invoke(taskId = myTask.id.toString())

        //Then
        coVerify(exactly = 1) { repository.getTaskById(any()) }
        assert(result.data == myTask)
    }

}