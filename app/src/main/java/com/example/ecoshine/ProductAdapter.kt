package com.example.ecoshine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    private val list: ArrayList<ProductModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_house, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    fun updateProductList(list1:ArrayList<ProductModel>){
        list.clear()
        list.addAll(list1)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(list[position])
        when {
            position%2==0 -> holder.itemView.setPadding(4,4,2,4)
            else -> holder.itemView.setPadding(2,4,4,4)
        }
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val houseItem: TextView = view.findViewById(R.id.houseItem)
        private val cardView: CardView = view.findViewById(R.id.card_view)

        fun bindData(data: ProductModel) {
            houseItem.text = data.house_number
            cardView.preventCornerOverlap = true
            cardView.useCompatPadding = true
        }
    }
}