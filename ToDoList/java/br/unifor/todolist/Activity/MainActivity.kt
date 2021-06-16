package br.unifor.todolist.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.unifor.todolist.Adapter.TaskAdapter
import br.unifor.todolist.Adapter.TaskItemListener
import br.unifor.todolist.Database.TaskDAO
import br.unifor.todolist.Database.UserDAO
import br.unifor.todolist.Database.UserWithTasks
import br.unifor.todolist.Model.Task
import br.unifor.todolist.R
import br.unifor.todolist.util.DatabaseUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener, TaskItemListener {

    private lateinit var userDAO: UserDAO
    private lateinit var TaskDAO: TaskDAO
    private lateinit var mTaskRecyclerView: RecyclerView
    private lateinit var mTaskAdd: FloatingActionButton
    private lateinit var mUserWithTasks: UserWithTasks
    private lateinit var taskAdapter: TaskAdapter

    private var mUserId = -1
    private val handler = Handler(Looper.getMainLooper())
    private var mTaskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userDAO = DatabaseUtil.getInstance(applicationContext).getUserDao()
        TaskDAO = DatabaseUtil.getInstance(applicationContext).getTaskDao()
        mTaskRecyclerView = findViewById(R.id.Main_recyclerview_tasks)

        mTaskAdd = findViewById(R.id.main_floatingbutton_addtask)
        mTaskAdd.setOnClickListener(this)
        
        if (intent != null) {
            mUserId = intent.getIntExtra("userId", -1)
        }
    }

    override fun onStart() {
        super.onStart()

        GlobalScope.launch {

            if (mUserId != -1) {

                //Organizando o Recycle view na main para mostrar todas as tarefas do usuário
                mUserWithTasks = userDAO.findUserWithTasks(mUserId)
                mTaskList.clear()
                mTaskList.addAll(mUserWithTasks.tasks)

                handler.post{
                    taskAdapter = TaskAdapter(mTaskList)
                    taskAdapter.setTasckItemListener(this@MainActivity)
                    val llm = LinearLayoutManager(applicationContext)

                    mTaskRecyclerView.apply {
                        adapter = taskAdapter
                        layoutManager = llm
                    }
                }

            }

        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //Redireciona para a criação de uma nova tarefa
            R.id.main_floatingbutton_addtask -> {
                val it = Intent(applicationContext, TaskFormActivity::class.java)
                it.putExtra("userId", mUserId)
                startActivity(it)
            }
        }
    }

    override fun onClick(v: View, position: Int) {
        //Redireciona para a edição de uma tarefa
        val it = Intent(applicationContext, TaskFormActivity::class.java)
        it.putExtra("userId", mUserId)
        it.putExtra("taskId", mUserWithTasks.tasks[position].id)
        startActivity(it)
    }

    override fun onLongClick(v: View, position: Int) {

        //Excluindo tarefa
        
        val dialog = AlertDialog.Builder(this)
                .setTitle("ToDo List")
                .setMessage("Você deseja excluir a tarefa  ${mUserWithTasks.tasks[position].name}?")
                .setPositiveButton("Sim"){dialog, _ ->

                    GlobalScope.launch {

                        val taskId = mUserWithTasks.tasks[position].id
                        val task = TaskDAO.find(taskId!!)
                        TaskDAO.delete(task)

                        mUserWithTasks = userDAO.findUserWithTasks(mUserId)
                        mTaskList.removeAt(position)

                        handler.post{
                            taskAdapter.notifyItemRemoved(position)
                        }
                    }

                    dialog.dismiss()
                }
                .setNegativeButton("Não") {dialog, _ ->
                    dialog.dismiss()
                }
                .create()

        dialog.show()
    }

}
