package com.example.recipe

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_Name, null, DB_Version) {

    companion object{
        private val DB_Name = "recipe_Food"
        private val DB_Version = 1
        private val Table_Name = "recipe"
        private val recipeID = "recipeID"
        private val recipeName = "recipeName"
        private val recipeType = "recipeType"
        private val recipeIngredients = "recipeIngredients"
        private val recipeStep = "recipeStep"
        private val recipeImage = "recipeImage"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val Create_Table: String = "CREATE TABLE $Table_Name ($recipeID INTEGER PRIMARY KEY,$recipeName VARCHAR,$recipeType VARCHAR,$recipeIngredients varchar,$recipeStep varchar, $recipeImage varchar);"
        p0?.execSQL(Create_Table)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val Drop_Table = "DROP TABLE IF EXISTS $Table_Name"
        p0?.execSQL(Drop_Table)
        onCreate(p0)
    }


    @SuppressLint("Range")
    fun getAllRecipe(): List<recipeModal>{
        val recipeList = ArrayList<recipeModal>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $Table_Name"
        val cursor =  db.rawQuery(selectQuery,null)
        if (cursor != null){
            if(cursor.moveToFirst()){
                do {
                    val recipe = recipeModal()
                    recipe.recipeID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(recipeID)))
                    recipe.recipeName = cursor.getString(cursor.getColumnIndex(recipeName))
                    recipe.recipeType = cursor.getString(cursor.getColumnIndex(recipeType))
                    recipe.recipeIngredient = cursor.getString(cursor.getColumnIndex(recipeIngredients))
                    recipe.recipeStep = cursor.getString(cursor.getColumnIndex(recipeStep))
                    recipe.recipeImage = cursor.getString(cursor.getColumnIndex(recipeImage))
                    recipeList.add(recipe)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return recipeList
    }

    //insert
    fun addRecipe(recipe: recipeModal): Boolean{
        val db:SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(recipeName , recipe.recipeName)
        values.put(recipeType , recipe.recipeType)
        values.put(recipeIngredients , recipe.recipeIngredient)
        values.put(recipeStep , recipe.recipeStep)
        values.put(recipeImage , recipe.recipeImage)
        val success: Long =  db.insert(Table_Name,null,values)
        db.close()
        return (Integer.parseInt("$success") != -1)
    }

    //select the data of particular recipetype
    @SuppressLint("Range")

    fun getRecipeType(recipe_Types: String): ArrayList<recipeModal> {
        val recipeList = ArrayList<recipeModal>()
        val db: SQLiteDatabase = writableDatabase
        // Properly quote the string value for recipeType
        val selectQuery = "SELECT * FROM $Table_Name WHERE $recipeType = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(recipe_Types))

        if (cursor.moveToFirst()) {
            do {
                val recipe = recipeModal()
                recipe.recipeID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(recipeID)))
                recipe.recipeName = cursor.getString(cursor.getColumnIndex(recipeName))
                recipe.recipeType = cursor.getString(cursor.getColumnIndex(recipeType))
                recipe.recipeIngredient = cursor.getString(cursor.getColumnIndex(recipeIngredients))
                recipe.recipeStep = cursor.getString(cursor.getColumnIndex(recipeStep))
                recipe.recipeImage = cursor.getString(cursor.getColumnIndex(recipeImage))
                recipeList.add(recipe)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return recipeList
    }


    @SuppressLint("Range")
    fun getRecipeID(recipe_ID: Int): recipeModal{
        val recipe = recipeModal()
        val db:SQLiteDatabase = writableDatabase
        val selectQuery = "SELECT *FROM $Table_Name WHERE $recipeID = $recipe_ID"
        val cursor = db.rawQuery(selectQuery,null)

        cursor?.moveToFirst()
        recipe.recipeID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(recipeID)))
        recipe.recipeName = cursor.getString(cursor.getColumnIndex(recipeName))
        recipe.recipeType = cursor.getString(cursor.getColumnIndex(recipeType))
        recipe.recipeIngredient = cursor.getString(cursor.getColumnIndex(recipeIngredients))
        recipe.recipeStep = cursor.getString(cursor.getColumnIndex(recipeStep))
        recipe.recipeImage = cursor.getString(cursor.getColumnIndex(recipeImage))
        cursor.close()
        return recipe
    }

    //delete
    fun deletedRecipe(recipe_ID: Int): Boolean{
        val db:SQLiteDatabase = this.writableDatabase
        val _success: Long= db.delete(Table_Name, recipeID + "=?", arrayOf(recipe_ID.toString())).toLong()
        return Integer.parseInt("$_success") != -1
    }

    //update
    fun updateRecipe(recipe: recipeModal): Boolean{
        val db:SQLiteDatabase =this.writableDatabase
        val values = ContentValues()
        values.put(recipeName , recipe.recipeName)
        values.put(recipeType , recipe.recipeType)
        values.put(recipeIngredients , recipe.recipeIngredient)
        values.put(recipeStep , recipe.recipeStep)
        values.put(recipeImage , recipe.recipeImage)
        val success: Long =  db.update(Table_Name,values,recipeID + "=?", arrayOf(recipe.recipeID.toString())).toLong()
        db.close()
        return (Integer.parseInt("$success") != -1)
    }


}