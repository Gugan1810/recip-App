package com.example.recipe

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import com.example.recipe.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.ByteArrayOutputStream
import kotlin.math.log

class AddRecipe : AppCompatActivity() ,AdapterView.OnItemSelectedListener {

    //declare the component variable
    lateinit var addRecipeType: Spinner
    lateinit var addRecipeName: EditText
    lateinit var addRecipeStep: EditText
    lateinit var addButtonIngredients: Button
    lateinit var addRecipeIngredient: EditText
    lateinit var viewIngredients: TextView
    lateinit var addImage: ImageView
    lateinit var uploadImage: Button
    lateinit var btnSave: Button
    lateinit var btnBack: ImageButton

    //declare the variable
    lateinit var recipeType: String
    var  ingredients = ArrayList<String>()
    lateinit var recipeImageUri: String
    var dbHandler: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        //extract the id
        addRecipeName = findViewById(R.id.edtRecipeName)
        addRecipeType = findViewById(R.id.spnAddRecipeType)
        addImage = findViewById(R.id.addRecipeImage)
        uploadImage = findViewById(R.id.btnUploadRecipe)
        addRecipeIngredient = findViewById(R.id.edtIngredients)
        addButtonIngredients = findViewById(R.id.btnAddIngredient)
        viewIngredients = findViewById(R.id.txtIngredient)
        addRecipeStep = findViewById(R.id.edtAddStep)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)
        dbHandler = DatabaseHelper(this)

        ArrayAdapter.createFromResource(this,R.array.add_recipe_type,R.layout.spinner_list).
        also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            addRecipeType.adapter = adapter
        }

        addRecipeType.onItemSelectedListener = this

        uploadImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        addButtonIngredients.setOnClickListener {
            var ingredientItems: String? = addRecipeIngredient.text.toString().trim()

            if (ingredientItems != null) {
                ingredients.add(ingredientItems)
                addRecipeIngredient.text.clear()
                val numberedIngredients = ingredients.mapIndexed { index, item -> "${index + 1}. $item" }.joinToString("\n")
                viewIngredients.text = numberedIngredients
            } else {
                // Show a toast message indicating that the input is empty
                Toast.makeText(this, "Please enter an ingredient", Toast.LENGTH_SHORT).show()
            }
        }

        btnSave.setOnClickListener {
            var success: Boolean = false
            val recipe: recipeModal = recipeModal()

            recipe.recipeName = addRecipeName.text.toString().trim()
            recipe.recipeType = recipeType
            recipe.recipeIngredient = ingredients.joinToString(separator = ",")
            recipe.recipeStep = addRecipeStep.text.toString().trim()
            recipe.recipeImage = recipeImageUri

            success = dbHandler?.addRecipe(recipe) as Boolean
            if (success) {
                Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to add the recipe", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            recipeImageUri = data?.data.toString()

            // Use Uri object instead of File to avoid storage permissions
            addImage.setImageURI(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemSelected(parentRecipeType: AdapterView<*>?, p1: View?, positionRecipeType: Int, p3: Long) {
        recipeType = parentRecipeType?.getItemAtPosition(positionRecipeType) as String


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}