package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.UserDataRepository
import com.softyorch.taskapp.utils.emptyString
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetUserEmailExistTest {

    @RelaxedMockK
    private lateinit var repository: UserDataRepository

    lateinit var getUserEmailExist: GetUserEmailExist

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getUserEmailExist = GetUserEmailExist(repository = repository)
    }

    @Test
    fun `when requesting an email that does not exist`() = runBlocking {

        val user = UserDataEntity(
            username = emptyString,
            userEmail = emptyString,
            userPass = emptyString,
            userPicture = emptyString
        )

        //Given
        coEvery { repository.getUserDataEmail(email = user.userEmail) } returns null

        //When
        getUserEmailExist.invoke(email = user.userEmail)

        //Then
        coVerify(exactly = 1) { repository.getUserDataEmail(any()) }
    }

    @Test
    fun `when requesting an email that exists`() = runBlocking {

        val user = UserDataEntity(
            username = "Jorge",
            userEmail = "jorge@mail.com",
            userPass = emptyString,
            userPicture = emptyString
        )

        //Given
        coEvery { repository.getUserDataEmail(email = user.userEmail) } returns user

        //When
        val getUser = getUserEmailExist.invoke(email = user.userEmail)

        //Then
        coVerify(exactly = 1) { repository.getUserDataEmail(any()) }
        assert(user == getUser)
    }
}