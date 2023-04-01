package com.tavanhieu.quanlytaphoa.activities.cart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.commons.formatCurrency
import com.tavanhieu.quanlytaphoa.commons.models.Cart

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){
    private lateinit var arr: ArrayList<Cart>

    @SuppressLint("NotifyDataSetChanged")
    fun setData(arr: ArrayList<Cart>) {
        this.arr = arr
        notifyDataSetChanged()
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var imageView: ImageView
        private lateinit var titleTextView: TextView
        private lateinit var priceTextView: TextView
        private lateinit var quantityTextView: TextView
        private lateinit var totalPriceTextView: TextView

        @SuppressLint("SetTextI18n")
        fun binding(cart: Cart) {
            if (cart.product.image != null) {
                Picasso.get().load(cart.product.image).into(imageView)
            }
            titleTextView.text = cart.product.name
            priceTextView.text = cart.product.price.formatCurrency()
            quantityTextView.text = "${cart.quantity} x${cart.product.type}"
            totalPriceTextView.text = (cart.product.price * cart.quantity).formatCurrency()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = arr[position]
        holder.binding(cart)
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}