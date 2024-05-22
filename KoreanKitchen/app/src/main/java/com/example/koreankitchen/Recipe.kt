package com.example.koreankitchen

import androidx.room.Entity
import androidx.room.PrimaryKey

import android.os.Parcel
import android.os.Parcelable

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val foodImage: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val totalTime: String,
    val serving: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "", // Read title
        parcel.readString() ?: "", // Read description
        parcel.readString() ?: "", // Read foodImage
        parcel.createStringArrayList() ?: ArrayList(), // Read ingredients
        parcel.createStringArrayList() ?: ArrayList(), // Read instructions
        parcel.readString() ?: "", // Read totalTime
        parcel.readString() ?: "" // Read serving
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(foodImage)
        parcel.writeStringList(ingredients)
        parcel.writeStringList(instructions)
        parcel.writeString(totalTime)
        parcel.writeString(serving)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }
}
