package com.softyorch.taskapp.ui.screens.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class DetailScreenViewModelTest {

    @RelaxedMockK
    private lateinit var taskUseCase: TaskUseCases

    private lateinit var detailScreenViewModel: DetailScreenViewModel

    private val id = UUID.randomUUID()

    private var taskList = listOf(
        TaskEntity(id = id, title = "test", description = "Testing", author = "jorge"),
        TaskEntity(
            id = UUID.randomUUID(),
            title = "test2",
            description = "Testing, second part",
            author = "jorge"
        )
    )

    private var task: Resource<TaskEntity> = Resource.Success(
        data = taskList.first()
    )

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        detailScreenViewModel = DetailScreenViewModel(taskUseCase = taskUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando el viewmodel carga los datos desde el repositorio al livedata buscando una task`() =
        runTest {

            //Given
            coEvery { taskUseCase.getTaskId.invoke(taskId = taskList.first().id.toString()) } returns task

            //When
            detailScreenViewModel.getTask(id = taskList.first().id.toString())

            //then
            delay(500)
            assert(detailScreenViewModel.taskEntityDetail.value == task.data)
        }

}