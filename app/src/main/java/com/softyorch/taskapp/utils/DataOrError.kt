package com.softyorch.taskapp.utils

import com.softyorch.taskapp.data.network.pexels.response.*

class DataOrError<T, E> {
    var data: T? = null
    var error: E? = null

    companion object{
        val listResponse = DataOrError<List<Media>, String>()
        val mediaResponse = DataOrError<Media, String>()
    }
}

/*sealed class DataOrError<out Data, out Error>{
    abstract fun isData(): Boolean
    abstract fun isError(): Boolean

    inline fun <R> result(isData: (Data) -> R, isError: (Error) -> R): R = when (this) {
        is ResultData -> isData(data)
        is ResultError -> isError(error)
    }

    data class ResultData<out Data> constructor(val data: Data) : DataOrError<Data, Nothing>(){
        override fun isData(): Boolean = true
        override fun isError(): Boolean = false
    }

    data class ResultError<out Error> constructor(val error: Error) : DataOrError<Nothing, Error>(){
        override fun isData(): Boolean = false
        override fun isError(): Boolean = true
    }


}

sealed class Either<out L, out R> {
    abstract fun isLeft(): Boolean
    abstract fun isRight(): Boolean

    inline fun <C> fold(ifLeft: (L) -> C, ifRight: (R) -> C): C = when (this) {
        is Left -> ifLeft(l)
        is Right -> ifRight(r)
    }

    fun swap(): Either<R, L> = fold({ Right(it) }, { Left(it) })

    fun <C> map(f: (R) -> C): Either<L, C> = fold({ Left(it) }, { Right(f(it)) })

    fun <C> mapLeft(f: (L) -> C): Either<C, R> = fold({ Left(f(it)) }, { Right(it) })

    data class Left<out L> constructor(val l: L) : Either<L, Nothing>() {
        override fun isLeft(): Boolean = true
        override fun isRight(): Boolean = false
    }

    data class Right<out R> constructor(val r: R) : Either<Nothing, R>() {
        override fun isLeft(): Boolean = false
        override fun isRight(): Boolean = true
    }
}*/
