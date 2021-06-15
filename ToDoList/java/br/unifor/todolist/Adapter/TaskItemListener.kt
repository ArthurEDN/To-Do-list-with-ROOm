package br.unifor.todolist.Adapter

import android.view.View

interface TaskItemListener {

    fun onClick(v: View, position:Int)

    fun onLongClick(v: View, position: Int)
}