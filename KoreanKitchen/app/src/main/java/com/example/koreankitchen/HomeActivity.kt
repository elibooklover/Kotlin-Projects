package com.example.koreankitchen

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class HomeActivity : AppCompatActivity(), RecipeAdapter.OnItemClickListener {
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeList: MutableList<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Read JSON data from file
        val json = assets.open("KoreanRecipe.json").reader().readText()
        val recipeData = JSONObject(json)
        val recipesJsonArray = recipeData.optJSONArray("recipe")
        recipeList = mutableListOf()

        // Parse JSON array into list of Recipe objects
        if (recipesJsonArray != null) {
            for (i in 0 until recipesJsonArray.length()) {
                val recipeObject = recipesJsonArray.getJSONObject(i)
                val recipe = Gson().fromJson(recipeObject.toString(), Recipe::class.java)
                recipeList.add(recipe)
            }
        } else {
            // Handle case where 'recipe' array is null
        }

        // Initialize and set up RecyclerView and RecipeAdapter
        recipeAdapter = RecipeAdapter(this)
        recipeAdapter.setData(recipeList)
        val rvFoodItem = findViewById<RecyclerView>(R.id.rv_food_item)
        rvFoodItem.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvFoodItem.adapter = recipeAdapter
    }

    override fun onItemClick(position: Int) {
        // Handle item click
        val intent = Intent(this, DetailActivity::class.java)
        // Pass the selected recipe data to DetailActivity
        intent.putExtra("recipe", recipeList[position])
        val imgDish = findViewById<View>(R.id.img_dish)
        val dishNameText = findViewById<View>(R.id.tv_dish_name)
        val descriptionText = findViewById<View>(R.id.tv_dish_desc)
        var options : ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
            this,
            Pair.create(dishNameText, "dishNameTransition"),
            Pair.create(imgDish, "imageTransition"),
            Pair.create(descriptionText, "descriptionTransition")
        )
        startActivity(intent, options.toBundle())
    }

    private fun saveRecipeToDatabase(recipe: Recipe) {
        CoroutineScope(Dispatchers.IO).launch {
            RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().insertRecipe(recipe)
        }
    }
}
