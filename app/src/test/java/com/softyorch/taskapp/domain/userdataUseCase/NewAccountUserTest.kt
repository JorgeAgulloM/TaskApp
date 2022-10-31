package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.user.UserDataRepository
import com.softyorch.taskapp.utils.emptyString
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class NewAccountUserTest {

    @RelaxedMockK
    private lateinit var repository: UserDataRepository

    lateinit var newAccountUser: NewAccountUser

    private val user = UserDataEntity(
        username = "jorge",
        userEmail = "jorge@mail.com",
        userPass = "123456aA",
        userPicture = emptyString
    )

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        newAccountUser = NewAccountUser(repository = repository)
    }

    @Test
    fun `create a new user account`() = runBlocking {

        //Given
        //coEvery { repository.addUserData(userDataEntity = user) }

        //When
        newAccountUser.invoke(userDataEntity = user)

        //Then
        coVerify(exactly = 1) { repository.addUserData(any()) }

    }
}