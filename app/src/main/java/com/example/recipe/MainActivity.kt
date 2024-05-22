package com.example.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    // Variables
    lateinit var spinner: Spinner
    lateinit var recipeType: String// Default value
    lateinit var addButton: ImageButton
    lateinit var recycler_view: RecyclerView
    var recipeListAdapter: recipeAdapter? = null
    var recyclerTypeAdapter: recyclerTypeAdapter? = null
    var dbHandler: DatabaseHelper? = null
    var recipelist: List<recipeModal> = ArrayList<recipeModal>()
    var recipelistType:  List<recipeModal> = ArrayList<recipeModal>()
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Declare the ID
        addButton = findViewById(R.id.btnAdd)
        spinner = findViewById(R.id.spnRecipeType)
        recycler_view = findViewById(R.id.recylerRecipe)

        ArrayAdapter.createFromResource(this, R.array.recipe_type, R.layout.spinner_list).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        addButton.setOnClickListener {
            val intent = Intent(this, AddRecipe::class.java)
            startActivity(intent)
        }

        dbHandler = DatabaseHelper(this)

        updateRecipeList()
    }

    private fun updateRecipeList() {

    }

    private fun fetchList() {
        recipelist = dbHandler!!.getAllRecipe()
        Log.d("Recipe", recipelist.joinToString("\n"))
        recipeListAdapter = recipeAdapter(recipelist, applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.adapter = recipeListAdapter
        recipeListAdapter?.notifyDataSetChanged()
    }

    // Retrieve the selected spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        recipeType = parent?.getItemAtPosition(position) as String
        if (recipeType == "All Recipe Type") {
            fetchList()
        } else {
            recipelistType = dbHandler!!.getRecipeType(recipeType)
            recyclerTypeAdapter = recyclerTypeAdapter(recipelistType, applicationContext)
            linearLayoutManager = LinearLayoutManager(applicationContext)
            recycler_view.layoutManager = linearLayoutManager
            recycler_view.adapter = recyclerTypeAdapter
            recyclerTypeAdapter?.notifyDataSetChanged()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Handle the case where nothing is selected if needed
    }
}
