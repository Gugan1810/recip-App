package com.example.recipe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class recipeAdapter(recipeList: List<recipeModal>, internal var context: Context)
    : RecyclerView.Adapter<recipeAdapter.RecipeViewHolder>() {

    internal  var recipeList: List<recipeModal> = ArrayList()
    init {
        this.recipeList = recipeList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_view_recipe,parent,false)
        return RecipeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipes = recipeList[position]
        holder.recipeNameVw.text = recipes.recipeName
        holder.recipeTypeVw.text = recipes.recipeType
        var imageRecipe: String? = recipes.recipeImage
        if (!imageRecipe.isNullOrEmpty()) {
            Picasso.get()
                .load(imageRecipe)
                .placeholder(R.drawable.baseline_fastfood_24) // optional placeholder
                .into(holder.recipeImageVw)
        } else {
            holder.recipeImageVw.setImageResource(R.drawable.baseline_fastfood_24) // optional placeholder for empty URL
        }

        holder.recipeCardVw.setOnClickListener {
            var recipeId: Int? = recipes.recipeID

            val intent = Intent(context, viewRecipe::class.java).apply {
                // Put the data into the Intent
                putExtra("EXTRA_RECIPEID", recipeId)
            }
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            // Start the second Activity
            context.startActivity(intent)
        }
    }



    class RecipeViewHolder(view: View): RecyclerView.ViewHolder(view){
        var recipeNameVw: TextView
        var recipeTypeVw: TextView
        var recipeImageVw: ImageView
        var recipeCardVw: CardView

        init {
            recipeNameVw= view.findViewById(R.id.recipeNameView)
            recipeTypeVw = view.findViewById(R.id.recipeTypeView)
            recipeImageVw = view.findViewById(R.id.recipeImageView)
            recipeCardVw = view.findViewById(R.id.recipeCard)
        }

    }

}




