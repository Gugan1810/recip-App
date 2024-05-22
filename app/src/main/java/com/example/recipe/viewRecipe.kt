package com.example.recipe

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class viewRecipe : AppCompatActivity() {

    lateinit var imageRecipeView: ImageView
    lateinit var txtRecipeName: TextView
    lateinit var txtRecipeType: TextView
    lateinit var txtRecipeIngredients: TextView
    lateinit var txtRecipeStep: TextView
    lateinit var imageButtonDelete: ImageButton
    lateinit var btnBackVw: ImageButton
    lateinit var imageButtonEdit: ImageButton

    var recipeID: Int = 0
    var dbHandler: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_recipe)

        imageRecipeView = findViewById(R.id.imageRecipeView)
        txtRecipeName = findViewById(R.id.txtRecipeName)
        txtRecipeType = findViewById(R.id.txtRecipeType)
        txtRecipeIngredients = findViewById(R.id.txtRecipeIngredients)
        txtRecipeStep = findViewById(R.id.txtRecipeStep)
        imageButtonDelete = findViewById(R.id.imageButtonDelete)
        btnBackVw = findViewById(R.id.btnBackVw)
        imageButtonEdit = findViewById(R.id.imageButtonEdit)

        dbHandler = DatabaseHelper(this)
        val recipeIdPast = intent.getIntExtra("EXTRA_RECIPEID", -1)
        if (recipeIdPast != -1) {
            recipeID  = recipeIdPast
            Log.d("recipeID", "$recipeIdPast")
            val recipeList: recipeModal = dbHandler!!.getRecipeID(recipeID)

            txtRecipeName.text = recipeList.recipeName
            txtRecipeType.text = recipeList.recipeType
            txtRecipeIngredients.text = recipeList.recipeIngredient
            txtRecipeStep.text = recipeList.recipeStep
            val imageUri = Uri.parse(recipeList.recipeImage)
            imageRecipeView.setImageURI(imageUri)

            imageButtonEdit.setOnClickListener {
                val intent = Intent(this, Editactivity::class.java).apply {
                    // Put the data into the Intent
                    putExtra("EXTRA_RECIPEID", recipeID)
                }
                startActivity(intent)
            }
        } else {
            Log.d("error", "emptyID")
        }

        imageButtonDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(this).setTitle("Delete Reipe").setMessage("Are You Sure To Delete?")
                .setPositiveButton("Yes!",{ dialog,i->
                    val success = dbHandler?.deletedRecipe(recipeID) as Boolean
                    if(success) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                })
                .setNegativeButton("No",{dialog,i->
                    dialog.dismiss()
                })
            dialog.show()
        }

        btnBackVw.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}