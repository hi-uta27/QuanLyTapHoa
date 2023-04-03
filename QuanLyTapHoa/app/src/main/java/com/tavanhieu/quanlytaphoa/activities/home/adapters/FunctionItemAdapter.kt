package com.tavanhieu.quanlytaphoa.activities.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.cart.presentations.CartActivity
import com.tavanhieu.quanlytaphoa.activities.depot.presentations.DepotActivity
import com.tavanhieu.quanlytaphoa.activities.home.models.FunctionItem
import com.tavanhieu.quanlytaphoa.activities.order.presentations.OrderActivity

class FunctionItemAdapter: RecyclerView.Adapter<FunctionItemAdapter.FunctionItemViewHolder>() {
    private lateinit var arr: ArrayList<FunctionItem>

    @SuppressLint("NotifyDataSetChanged")
    fun setData(arr: ArrayList<FunctionItem>) {
        this.arr = arr
        notifyDataSetChanged()
    }

    class FunctionItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image = itemView.findViewById<ImageView>(R.id.itemImageView)
        private var title = itemView.findViewById<TextView>(R.id.titleTextView)

        fun binding(item: FunctionItem) {
            title.text = item.name
            image.setImageResource(item.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_function_item, parent, false)
        return FunctionItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: FunctionItemViewHolder, position: Int) {
        holder.binding(arr[position])
        holder.itemView.setOnClickListener {
            openActivity(holder.itemView.context, position)
        }
    }

    private fun openActivity(context: Context, position: Int) {
        when (position) {
            0 -> {
                context.startActivity(Intent(context, DepotActivity::class.java))
            }
            1 -> {
                context.startActivity(Intent(context, OrderActivity::class.java))
            }
            2 -> {
                context.startActivity(Intent(context, CartActivity::class.java))
            }
            3 -> {
                Toast.makeText(context, "Thống Kê", Toast.LENGTH_SHORT).show()
            }
            4 -> {
                Toast.makeText(context, "QR Code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}