package com.example.todolistapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ToDoDB(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    private var db: SQLiteDatabase? = null

    companion object{
        private const val DATABASE_NAME = "todo_list.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "todo_list"
        private const val ID = "id"
        private const val TASK = "task"
        private const val STATUS = "status"
    }

    /**
     *  DB Generation
     */
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "$TASK TEXT, $STATUS INTEGER)"

        db?.execSQL(query)
    }

    /**
     * Upgrade
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /**
     *  Writing Data
     */
    fun openDatabase(){
        db = this.writableDatabase
    }

    /**
     * Getting all tasks
     */
    fun getAllTasks(): ArrayList<ToDoModel>{
        // Initialize
        val taskList: ArrayList<ToDoModel> = ArrayList()
        var cursor: Cursor? = null
        var query = "SELECT * FROM $TABLE_NAME"

        // Read DB
        db = this.readableDatabase

        if(db != null){
            cursor = db!!.rawQuery(query, null)

            while(cursor.moveToNext()){

                val task = ToDoModel(cursor.getInt(0), cursor.getString(1), cursor.getInt(2))
                // Adding to list
                taskList.add(task)

            }
        }
        return taskList
    }

    /**
     * Adding Task
     */
    fun addTask(task: ToDoModel){
        openDatabase()

        val cv = ContentValues()
        cv.put(TASK, task.task)
        cv.put(STATUS, 0)
        db!!.insert(TABLE_NAME, null, cv)
    }

    /**
     * Updating Status
     */
    fun updateStatus(id: Int, status: Int){
        openDatabase()

        val cv = ContentValues()
        cv.put(STATUS, status)
        db!!.update(TABLE_NAME, cv, "id=?", arrayOf(id.toString()))
    }

    /**
     * Updating Task
      */
    fun updateTask(id: Int, task: String){
        openDatabase()

        val cv = ContentValues()
        cv.put(TASK, task)
        db!!.update(TABLE_NAME, cv, "id=?", arrayOf(id.toString()))
    }

    /**
     * Deleting Task
      */
    fun deleteTask(id: Int){
        openDatabase()
        db!!.delete(TABLE_NAME, "id=?", arrayOf(id.toString()))
    }
}