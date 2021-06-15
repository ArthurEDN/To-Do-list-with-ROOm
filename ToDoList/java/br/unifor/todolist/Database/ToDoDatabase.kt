package br.unifor.todolist.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.unifor.todolist.Model.Task
import br.unifor.todolist.Model.User

@Database(entities = [User::class, Task::class],  version = 1)
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun getUserDao():UserDAO

    abstract fun getTaskDao():TaskDAO
}