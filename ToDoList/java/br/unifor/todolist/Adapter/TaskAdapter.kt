package br.unifor.todolist.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.unifor.todolist.Model.Task
import br.unifor.todolist.R


//Criando o ViewHolder
class TaskAdapter( val tasks: List<Task>): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

     private var listener: TaskItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView, listener)
    }

    //Carregando as informações da tarefa no TaskView(nome, descrição e se está finalizada)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.taskName.text = tasks[position].name
        holder.taskDescription.text = tasks[position].description

        if(tasks[position].isDone){
            holder.taskIsFinished.setBackgroundColor(Color.GREEN)
        } else {
            holder.taskIsFinished.setBackgroundColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun setTasckItemListener(listener: TaskItemListener?){
        this.listener = listener
    }

    //Preenchendo o ViewHolder
    class TaskViewHolder(itemView: View, listener: TaskItemListener?):RecyclerView.ViewHolder(itemView){
        val taskName: TextView = itemView.findViewById(R.id.item_task_textview_name)
        val taskDescription: TextView = itemView.findViewById(R.id.item_task_textview_description)
        val taskIsFinished: View = itemView.findViewById(R.id.item_view_isFinished)

        init {
            itemView.setOnClickListener{
                listener?.onClick(it, adapterPosition)
            }

            itemView.setOnLongClickListener{
                listener?.onLongClick(it, adapterPosition)
                true
            }
        }

    }

}
