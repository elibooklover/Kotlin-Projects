package com.example.koreankitchen

import android.os.Parcel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class RecipeTest {
    private lateinit var recipe: Recipe

    @Before
    fun setUp() {
        recipe = Recipe(
            id = 1,
            title = "Test Recipe",
            description = "Test Description",
            foodImage = "test_image.jpg",
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            instructions = listOf("Step 1", "Step 2"),
            totalTime = "30 minutes",
            serving = "2"
        )
    }

    @Test
    fun writeToParcel() {
        val parcel = mock(Parcel::class.java)
        recipe.writeToParcel(parcel, 0)

        val inOrder = inOrder(parcel)
        inOrder.verify(parcel).writeInt(1)
        inOrder.verify(parcel).writeString("Test Recipe")
        inOrder.verify(parcel).writeString("Test Description")
        inOrder.verify(parcel).writeString("test_image.jpg")
        inOrder.verify(parcel).writeStringList(listOf("Ingredient 1", "Ingredient 2"))
        inOrder.verify(parcel).writeStringList(listOf("Step 1", "Step 2"))
        inOrder.verify(parcel).writeString("30 minutes")
        inOrder.verify(parcel).writeString("2")
    }

    @Test
    fun createFromParcel() {
        val parcel = mock(Parcel::class.java)

        `when`(parcel.readInt()).thenReturn(1)
        `when`(parcel.readString()).thenReturn(
            "Test Recipe",
            "Test Description",
            "test_image.jpg",
            "30 minutes",
            "2"
        )
        `when`(parcel.createStringArrayList()).thenReturn(
            arrayListOf("Ingredient 1", "Ingredient 2"),
            arrayListOf("Step 1", "Step 2")
        )

        val createdRecipe = Recipe.CREATOR.createFromParcel(parcel)

        assertEquals(recipe, createdRecipe)
    }
}