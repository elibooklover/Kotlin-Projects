package com.example.koreankitchen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecipeAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    private var arrRecipe = ArrayList<Recipe>()

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishName: TextView = itemView.findViewById(R.id.tv_dish_name)
        val dishDesc: TextView = itemView.findViewById(R.id.tv_dish_desc)
        val dishImage: ImageView = itemView.findViewById(R.id.img_dish)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = arrRecipe[position]
        holder.dishName.text = currentRecipe.title
        holder.dishDesc.text = currentRecipe.description

        // Load image using Glide library
        Glide.with(holder.itemView.context)
            .load(currentRecipe.foodImage)
            .error(R.drawable.baseline_error_24)
            .into(holder.dishImage)
    }

    override fun getItemCount() = arrRecipe.size

    fun setData(data: List<Recipe>) {
        arrRecipe.clear()
        arrRecipe.addAll(data)
        notifyDataSetChanged()
    }
}