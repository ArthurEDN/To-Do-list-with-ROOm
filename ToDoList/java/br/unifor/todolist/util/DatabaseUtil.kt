package br.unifor.todolist.util

import android.content.Context
import androidx.room.Room
import br.unifor.todolist.Database.ToDoDatabase

object DatabaseUtil {

    private  var db: ToDoDatabase? = null

    fun getInstance(context: Context): ToDoDatabase{

        if(db == null){
           db = Room.databaseBuilder(
                context,
                ToDoDatabase::class.java,
                "todolist.db"
            ).build()
        }
        
        return db !!
    }

}