package com.softyorch.taskapp.di

import android.content.Context
import androidx.room.Room
import com.softyorch.taskapp.data.database.datastore.DatastoreDataBase
import com.softyorch.taskapp.data.database.tasks.TaskDatabase
import com.softyorch.taskapp.data.database.tasks.TaskDatabase.Companion.TASK_DB_NAME
import com.softyorch.taskapp.data.database.tasks.TaskDatabaseDao
import com.softyorch.taskapp.data.database.userdata.UserDataBase
import com.softyorch.taskapp.data.database.userdata.UserDataBase.Companion.USERDATA_DB_NAME
import com.softyorch.taskapp.data.database.userdata.UserDataBaseDao
import com.softyorch.taskapp.data.repository.DatastoreRepository
import com.softyorch.taskapp.data.repository.task.TaskRepository
import com.softyorch.taskapp.data.repository.UserDataRepository
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.datastoreUseCase.DeleteData
import com.softyorch.taskapp.domain.datastoreUseCase.GetData
import com.softyorch.taskapp.domain.datastoreUseCase.SaveData
import com.softyorch.taskapp.domain.taskUsesCase.*
import com.softyorch.taskapp.domain.userdataUseCase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDatabaseDao = taskDatabase.taskDao

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): TaskDatabase =
        Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            TASK_DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesUserData(userDataBase: UserDataBase): UserDataBaseDao = userDataBase.userDataDao()

    @Singleton
    @Provides
    fun providesUserDataBase(@ApplicationContext context: Context): UserDataBase =
        Room.databaseBuilder(
            context,
            UserDataBase::class.java,
            USERDATA_DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesDatastoreDataBase(@ApplicationContext context: Context): DatastoreDataBase =
        DatastoreDataBase(context = context)

    @Singleton
    @Provides
    fun providesDatastoreUseCases(datastoreRepository: DatastoreRepository): DatastoreUseCases =
        DatastoreUseCases(
            getData = GetData(repository = datastoreRepository),
            saveData = SaveData(repository = datastoreRepository),
            deleteData = DeleteData(repository = datastoreRepository)
        )


    @Singleton
    @Provides
    fun providesTaskUseCases(taskRepository: TaskRepository): TaskUseCases =
        TaskUseCases(
            getAllTask = GetAllTask(repository = taskRepository),
            getCheckedTask = GetCheckedTask(repository = taskRepository),
            getUncheckedTask = GetUncheckedTask(repository = taskRepository),
            getTaskId = GetTaskId(repository = taskRepository),
            addNewTask = AddNewTask(repository = taskRepository),
            updateTask = UpdateTask(repository = taskRepository),
            deleteTask = DeleteTask(repository = taskRepository),
            deleteAllTask = DeleteAllTask(repository = taskRepository),
            fakeData = FakeData(repository = taskRepository)
        )

    @Singleton
    @Provides
    fun providesUserDataUseCases(userDataRepository: UserDataRepository): UserDataUseCases =
        UserDataUseCases(
            getUserEmailExist = GetUserEmailExist(repository = userDataRepository),
            loginUser = LoginUser(repository = userDataRepository),
            newAccountUser = NewAccountUser(repository = userDataRepository),
            updateUser = UpdateUser(repository = userDataRepository)
        )
}












