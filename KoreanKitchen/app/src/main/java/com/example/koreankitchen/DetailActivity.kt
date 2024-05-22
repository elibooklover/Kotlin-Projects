package com.example.koreankitchen

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Retrieve the passed recipe data from Intent extras
        val recipe = intent.getParcelableExtra<Recipe>("recipe")

        if (recipe != null) {
            // Set up ActionBar
            setSupportActionBar(findViewById(R.id.toolbar))
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)

            // Update UI with recipe data
            findViewById<TextView>(R.id.tv_dish_name).text = recipe.title
            findViewById<TextView>(R.id.tv_dish_desc).text = recipe.description
            findViewById<TextView>(R.id.tv_ingredients).text = recipe.ingredients.joinToString(", ")
            findViewById<TextView>(R.id.tv_instructions).text = recipe.instructions.joinToString("\n")
            findViewById<TextView>(R.id.tv_total_time).text = recipe.totalTime
            findViewById<TextView>(R.id.tv_serving).text = recipe.serving

            Glide.with(this)
                .load(recipe.foodImage)
                .error(R.drawable.baseline_error_24)
                .into(findViewById(R.id.img_dish))
        } else {
            // Handle case where recipe is null
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Handle back button click
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
