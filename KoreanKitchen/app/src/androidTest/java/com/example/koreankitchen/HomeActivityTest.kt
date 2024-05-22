package com.example.koreankitchen

import org.junit.Assert.*
import org.junit.Test
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Test
    fun onCreate() {
        ActivityScenario.launch(HomeActivity::class.java)
    }

    @Test
    fun onItemClick() {
        val activityScenario = ActivityScenario.launch(HomeActivity::class.java)
        activityScenario.onActivity { activity ->
            activity.onItemClick(0)
        }
    }
}