package com.example.koreankitchen

import android.content.Intent
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailActivityTest {
    private lateinit var recipe: Recipe

    @Test
    fun onCreate() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        recipe = Recipe(
            title = "Spaghetti",
            description = "Delicious pasta",
            foodImage = "https://revupware.files.wordpress.com/2023/04/pexels-photo-2313686.jpeg",
            ingredients = listOf("Test water", "Test eggs", "Test bacon", "Test cheese"),
            instructions = listOf("Test", "Test", "Mix food", "Enjoy"),
            totalTime = "30 minutes",
            serving = "2 servings"
        )

        val intent = Intent(context, DetailActivity::class.java).apply {
            putExtra("recipe", recipe)
        }

        ActivityScenario.launch<DetailActivity>(intent).onActivity { activity ->
            assertNotNull(activity.findViewById<TextView>(R.id.tv_dish_name))
            assertNotNull(activity.findViewById<TextView>(R.id.tv_dish_desc))
        }
    }
}