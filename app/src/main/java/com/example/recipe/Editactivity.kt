package com.example.recipe

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.github.dhaval2404.imagepicker.ImagePicker

class Editactivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    //declare the component variable
    lateinit var spnEditRecipeType: Spinner
    lateinit var edtRecipeNameEdit: EditText
    lateinit var edtEditStep: EditText
    lateinit var edtEditIngredients: EditText
    lateinit var EditRecipeImage: ImageView
    lateinit var btnUploadRecipeEdit: Button
    lateinit var btnSaveEdit: Button
    lateinit var btnBackEdit: ImageButton
    lateinit var recipeType: String
    var recipeID: Int = 0
    var dbHandler: DatabaseHelper? = null
    lateinit var recipeImageUri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editactivity)

        spnEditRecipeType = findViewById(R.id.spnEditRecipeType)
        edtRecipeNameEdit = findViewById(R.id.edtRecipeNameEdit)
        edtEditStep = findViewById(R.id.edtEditStep)
        edtEditIngredients = findViewById(R.id.edtEditIngredients)
        EditRecipeImage = findViewById(R.id.EditRecipeImage)
        btnUploadRecipeEdit = findViewById(R.id.btnUploadRecipeEdit)
        btnSaveEdit = findViewById(R.id.btnSaveEdit)
        btnBackEdit = findViewById(R.id.btnBackEdit)

        spnEditRecipeType.onItemSelectedListener = this
        val spinnerItems = listOf("Breakfast recipes", "Lunch recipes","Dinner recipes","Appetizer recipes","Salad recipes",)
        val adapter = ArrayAdapter(this, R.layout.spinner_list, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnEditRecipeType.adapter = adapter

        btnUploadRecipeEdit.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        dbHandler = DatabaseHelper(this)
        val recipeIdPast = intent.getIntExtra("EXTRA_RECIPEID", -1)
        if (recipeIdPast != -1) {
            recipeID  = recipeIdPast
            val recipeList: recipeModal = dbHandler!!.getRecipeID(recipeID)

            edtRecipeNameEdit.setText(recipeList.recipeName)
            edtEditIngredients.setText(recipeList.recipeIngredient)
            edtEditStep.setText(recipeList.recipeStep)
            val imageUri = Uri.parse(recipeList.recipeImage)
            EditRecipeImage.setImageURI(imageUri)
            val SpinnerPosition= adapter.getPosition(recipeList.recipeType)
            spnEditRecipeType.setSelection(SpinnerPosition)
        } else {
            Log.d("error", "emptyID")
        }

        btnSaveEdit.setOnClickListener {
            var success: Boolean = false
            val recipe: recipeModal = recipeModal()

            recipe.recipeName = edtRecipeNameEdit.text.toString().trim()
            recipe.recipeType = recipeType
            recipe.recipeIngredient = edtEditIngredients.text.toString().trim()
            recipe.recipeStep = edtEditStep.text.toString().trim()
            recipe.recipeImage = recipeImageUri

            success = dbHandler?.updateRecipe(recipe) as Boolean
            if (success) {
                Toast.makeText(this, "Successfully Edited!!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed To Edit!!", Toast.LENGTH_SHORT).show()
            }
        }

        btnBackEdit.setOnClickListener {
            val intentView = Intent(this, viewRecipe::class.java)
            startActivity(intent)
        }

    }



    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        recipeType = p0?.getItemAtPosition(p2) as String
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            recipeImageUri = data?.data.toString()

            // Use Uri object instead of File to avoid storage permissions
            EditRecipeImage.setImageURI(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}

