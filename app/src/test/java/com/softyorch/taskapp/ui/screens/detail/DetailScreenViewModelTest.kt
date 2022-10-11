package com.softyorch.taskapp.ui.screens.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softyorch.taskapp.domain.taskUsesCase.TaskModelUseCase
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import com.softyorch.taskapp.ui.models.mapToTaskModelUI
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
        TaskModelUseCase(id = id, title = "test", description = "Testing", author = "jorge"),
        TaskModelUseCase(
            id = UUID.randomUUID(),
            title = "test2",
            description = "Testing, second part",
            author = "jorge"
        )
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
    fun `A task is requested from the viewModel`() =
        runTest {
            val task = taskList.first()
            //Given
            coEvery { taskUseCase.getTaskId.invoke(taskId = taskList.first().id.toString()) } returns task

            //When
            detailScreenViewModel.getTask(id = taskList.first().id.toString())

            //then
            delay(500)
            assert(detailScreenViewModel.taskEntityDetail.value == task.mapToTaskModelUI())
        }

}