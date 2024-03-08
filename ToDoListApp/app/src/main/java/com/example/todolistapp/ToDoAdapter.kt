package com.example.todolistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView

class ToDoAdapter(): RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {
    lateinit var mDb: ToDoDB

    constructor(db: ToDoDB): this(){
        mDb = db
    }

    var todoList: ArrayList<ToDoModel> = ArrayList()

    /**
     * Setting View
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.task_item, parent, false)

        return ViewHolder(view)
    }

    /**
     * Setting data
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Getting item
        val item: ToDoModel = todoList[position]

        // Text
        holder.mCheckBox.text = item.task
        // If the status value is not 0, check.
        holder.mCheckBox.isChecked = toBoolean(item.status)

        holder.mCheckBox.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(comButton: CompoundButton?, isChecked: Boolean) {

                if(isChecked){
                    mDb.updateStatus(item.id, 1)
                } else {
                    mDb.updateStatus(item.id, 0)
                }
            }
        })
    }

    /**
     * Status Check
     * If the status value is not 0, true
     */
    private fun toBoolean(n: Int) = n != 0

    /**
     * Loading the number of data
     */
    override fun getItemCount(): Int = todoList.size

    /**
     * Setting Tasks
     */
    fun setTask(todoList: ArrayList<ToDoModel>){
        this.todoList = todoList
        notifyDataSetChanged()
    }

    /**
     * Deleting tasks
     */
    fun removeTask(position: Int){
        todoList.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val mCheckBox: CheckBox = view.findViewById(R.id.m_check_box)
    }
}