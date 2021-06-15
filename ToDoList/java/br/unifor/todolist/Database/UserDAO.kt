package br.unifor.todolist.Database

import androidx.room.*
import br.unifor.todolist.Model.User

@Dao //CRUD
interface UserDAO {

   @Insert fun insert(user: User)

   @Update fun update(user: User)

   @Delete fun delete(user: User)

   @Query("SELECT * FROM users WHERE id = :id ") fun find(id: Int): User

   @Query("SELECT * FROM users WHERE email = :email ") fun findByEmail(email: String): User

   @Query("SELECT * FROM users") fun findAll(): List<User>

   @Transaction
   @Query("SELECT * FROM users WHERE id = :id") fun findUserWithTasks(id:Int):UserWithTasks
}