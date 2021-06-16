package br.unifor.todolist.Database

import androidx.room.Embedded
import androidx.room.Relation
import br.unifor.todolist.Model.Task
import br.unifor.todolist.Model.User

//Relacionar usuários com suas tarefas
data class UserWithTasks(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val tasks: List<Task>
)
