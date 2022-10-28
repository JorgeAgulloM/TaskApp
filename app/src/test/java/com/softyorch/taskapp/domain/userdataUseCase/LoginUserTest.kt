package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.user.UserDataRepository
import com.softyorch.taskapp.utils.emptyString
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test

class LoginUserTest {

    @RelaxedMockK
    private lateinit var repository: UserDataRepository

    lateinit var loginUser: LoginUser

    private val user = UserDataEntity(
        username = "jorge",
        userEmail = "jorge@mail.com",
        userPass = "123456aA",
        userPicture = emptyString
    )

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        loginUser = LoginUser(repository = repository)
    }

    @Test
    fun `when the user incorrectly enters a password`() = runBlocking {

        //Given
        coEvery {
            repository.singInUser(
                email = user.userEmail,
                password = "12345678"
            )
        } returns user

        //When
        val result = loginUser.invoke(email = user.userEmail, password = user.userPass)

        //Then
        coVerify(exactly = 1) { repository.singInUser(any(), any()) }
        assert(result != user)
    }

    @Test
    fun `when the user incorrectly enters an incorrect email address`() = runBlocking {

        //Given
        coEvery {
            repository.singInUser(
                email = "mail@mail.com",
                password = user.userPass
            )
        } returns user

        //When
        val result = loginUser.invoke(email = user.userEmail, password = user.userPass)

        //Then
        coVerify(exactly = 1) { repository.singInUser(any(), any()) }
        assert(result != user)
    }

    @Test
    fun `when the user correctly enters an email address and a password`() = runBlocking {

        //Given
        coEvery {
            repository.singInUser(
                email = user.userEmail,
                password = user.userPass
            )
        } returns user

        //When
        val result = loginUser.invoke(email = user.userEmail, password = user.userPass)

        //Then
        coVerify(exactly = 1) { repository.singInUser(any(), any()) }
        assert(result == user)
    }
}