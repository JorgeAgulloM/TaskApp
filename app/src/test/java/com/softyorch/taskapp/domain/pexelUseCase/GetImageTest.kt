package com.softyorch.taskapp.domain.pexelUseCase

import com.softyorch.taskapp.data.repository.pexels.PexelsRepository
import com.softyorch.taskapp.data.repository.pexels.model.MediaModel
import com.softyorch.taskapp.utils.DataOrError
import com.softyorch.taskapp.utils.emptyString
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test

class GetImageTest {

    @RelaxedMockK
    private lateinit var repository: PexelsRepository

    lateinit var getImage: GetImage

    private val emptyMedia = MediaModel(
        imageOriginalSrc = emptyString,
        imageUrl = emptyString,
        photographer = emptyString,
        photographerUrl = emptyString
    )

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getImage = GetImage(repository = repository)
    }

    @Test
    fun `cuando se realiza una peticion de imagen`() = runBlocking {
        val imageDOE = DataOrError<MediaModel, String>()
        imageDOE.apply {
            this.data = emptyMedia
        }

        //Given
        coEvery { repository.getRandomImage() } returns imageDOE

        //When
        val result = getImage.invoke()

        //then
        coVerify(exactly = 1) { repository.getRandomImage() }
        assert(result.data == imageDOE.data?.mapToMediaModelDomain())
    }

    @Test
    fun `cuando se pide una imagen pero se produce un error`() = runBlocking {
        val imageDOE = DataOrError<MediaModel, String>()
        imageDOE.apply {
            this.error = null
        }

        //Given
        coEvery { repository.getRandomImage() } returns imageDOE

        //When
        val result = getImage.invoke()

        //then
        coVerify(exactly = 1) { repository.getRandomImage() }
        assert(result.error == imageDOE.error)
    }

}