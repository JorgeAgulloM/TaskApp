package com.softyorch.taskapp.data.repository.task

import android.util.Log
import com.softyorch.taskapp.data.database.tasks.TaskDatabaseDao
import kotlinx.coroutines.flow.*
import java.time.Instant
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDatabaseDao: TaskDatabaseDao) {
    fun getAllTaskFromDatabase(): Flow<List<TaskModel>> = taskDatabaseDao.getTasks().let { flow ->
        flow.map { list -> list.map { it.mapToTaskModel() } }
    }

    suspend fun getTaskById(idTask: String): TaskModel =
        taskDatabaseDao.getTaskById(id = idTask).mapToTaskModel()

    suspend fun addTask(taskModel: TaskModel) =
        taskDatabaseDao.insert(taskModel.mapToTaskEntity())

    suspend fun updateTask(taskModel: TaskModel) =
        taskDatabaseDao.update(taskModel.mapToTaskEntity())

    suspend fun deleteTask(taskModel: TaskModel) =
        taskDatabaseDao.deleteTask(taskModel.mapToTaskEntity())

    suspend fun deleteAllTask() = taskDatabaseDao.deleteAll()

    suspend fun fakeData() {
        fakeData.forEach {
            addTask(it)
            Log.d("TASK_FAKE", "Tarea creada -> $it")
        }
    }

    private val loremIpsum = "What is Lorem Ipsum?\n" +
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "\n" +
            "Why do we use it?\n" +
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like)."
    private val fakeData = listOf(
        TaskModel(
            id = null,
            title = "Revisar proyecto",
            description = "Revisar todo el proyecto en busca de fallos",
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Lorem Ipsum",
            description = loremIpsum,
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Pexels",
            description = "Gracias a Pexels.com por su API de imagenes",
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Modificar DB",
            description = "Cambiar versión de DB Room",
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Revisar Login",
            description = "Elevar la sheet",
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Revisar new account",
            description = "Elevar la sheet",
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Cambiar sheet",
            description = "Debería cambiar la sheet del Login por una custom",
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Lorem Lorem",
            description = loremIpsum,
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Revisar textos",
            description = loremIpsum,
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Gracias Aristidevs",
            description = "Gracias por tu trabajo que me ha permitido mejorar mis habilidades",
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Gracias Mouredev",
            description = "Gracias a tu contenido he visto de otra forma el ser Freelance",
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Lista fake",
            description = "Incluir datos fake para no tener que estar creando tareas",
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Mostrar versión Beta",
            description = "Crear vídeo de presentación para la Beta y publicarlo",
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Crear versión beta",
            description = "Crear la versión Beta en GitHub",
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Lorem Lorem",
            description = loremIpsum,
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Lorem Lorem",
            description = loremIpsum,
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),
        TaskModel(
            id = null,
            title = "Lorem Lorem",
            description = loremIpsum,
            author = "Jorge",
            entryDate = Date.from(
                Instant.now()
            ),
            finishDate = null,
            checkState = false
        ),

        )

}
