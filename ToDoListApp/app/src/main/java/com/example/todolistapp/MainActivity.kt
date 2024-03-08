package com.example.todolistapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class MainActivity : AppCompatActivity() {

    lateinit var taskList: ArrayList<ToDoModel>
    lateinit var adapter: ToDoAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var fab: FloatingActionButton
    lateinit var todoText: EditText
    lateinit var addBtn: Button
    lateinit var db: ToDoDB
    lateinit var bottomLayout: LinearLayout

    var gId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // DB
        db = ToDoDB(this)

        // Initialize
        taskList = ArrayList()
        bottomLayout = findViewById(R.id.bottom_section)
        todoText = findViewById(R.id.todo_text)
        addBtn = findViewById(R.id.add_btn)
        fab = findViewById(R.id.fab)

        // Setting recyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Setting adapter
        adapter = ToDoAdapter(db)
        adapter.setTask(taskList)

        // Applying adapter
        recyclerView.adapter = adapter

        selectData()

        // Add mode
        fab.setOnClickListener {
            viewMode("ADD")
        }

        // Add button
        addBtn.setOnClickListener {
            viewMode("FAB")
            // Text value
            var text = todoText.text.toString()

            // Add or update
            if(addBtn.text.toString() == "ADD"){
                val task = ToDoModel(0, text, 0)
                db.addTask(task)
                selectReset("ADD")
            } else {
                db.updateTask(gId, text)
                selectReset("UPDATE")
            }

            // Hide Keyboard
            hideKeyboard(todoText)
        }

        // Check tasks
        todoText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSeq: CharSequence?, start: Int, before: Int, count: Int) {

                if(charSeq.toString() == "") {
                    addBtn.isEnabled = false
                    addBtn.setTextColor(Color.GRAY)
                } else {
                    addBtn.isEnabled = true
                    addBtn.setTextColor(Color.BLACK)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Swipe function
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition

                when(direction){
                    // Deleting tasks
                    ItemTouchHelper.LEFT -> {
                        val id = taskList[position].id
                        adapter.removeTask(position)
                        db.deleteTask(id)
                    }
                    // Editing tasks
                    ItemTouchHelper.RIGHT -> {
                        viewMode("UPDATE")
                        val task = taskList[position].task
                        gId = taskList[position].id
                        todoText.setText(task)
                        addBtn.text = "UPDATE"
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(Color.RED)
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(Color.WHITE)

                    .addSwipeRightBackgroundColor(Color.BLUE)
                    .addSwipeRightActionIcon(R.drawable.ic_edit)
                    .addSwipeRightLabel("Edit")
                    .setSwipeRightLabelColor(Color.WHITE)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }).attachToRecyclerView(recyclerView)

    } // OnCreate()

    /**
     * Getting tasks
     */
    private fun selectData(){
        // Getting tasks
        taskList = db.getAllTasks()

        // Sort in a newest order
        taskList.reverse()

        // Setting tasks
        adapter.setTask(taskList)

        // Applying
//        adapter.notifyDataSetChanged()
    }

    /**
     * Select and reset
     */
    private fun selectReset(type: String){
        selectData()
        todoText.setText("")

        if(type != "ADD"){
            addBtn.text = ""
        }
    }

    private fun viewMode(type: String){
        if(type == "FAB"){
            bottomLayout.visibility = View.GONE

            fab.visibility = View.VISIBLE
        } else {
            bottomLayout.visibility = View.VISIBLE

            fab.visibility = View.INVISIBLE
        }
    }

    /**
     * Hide keyboard
     */
    private fun hideKeyboard(editText: EditText){
        val manager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(editText.applicationWindowToken, 0)
    }
}