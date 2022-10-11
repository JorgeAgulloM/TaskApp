package com.softyorch.taskapp.domain.datastoreUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.DatastoreRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test

class GetDataTest {

    @RelaxedMockK
    private lateinit var repository: DatastoreRepository

    lateinit var getData: GetData

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getData = GetData(repository = repository)
    }

    @Test
    fun `request data from the repository about the logged-in user`() = runBlocking {
        val user = flowOf(
            UserDataEntity(
                username = "Jorge",
                userEmail = "jorge@mail.com",
                userPass = "123456aA"
            )
        )

        //Given
        coEvery { repository.getData() } returns user

        //When
        val result = getData.invoke()

        //Then
        coVerify(exactly = 1) { repository.getData() }
        assert(result == user)

    }

}