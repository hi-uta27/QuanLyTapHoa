package com.tavanhieu.quanlytaphoa.activities.cart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.commons.formatCurrency
import com.tavanhieu.quanlytaphoa.commons.models.Cart

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){
    private lateinit var arr: ArrayList<Cart>
    var deleteCartWith: ((id: String) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(arr: ArrayList<Cart>) {
        this.arr = arr
        notifyDataSetChanged()
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imageView: ImageView = itemView.findViewById(R.id.imageView)
        private var titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private var priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private var quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        private var totalPriceTextView: TextView = itemView.findViewById(R.id.totalPriceTextView)
        private var cancelImageView: ImageView = itemView.findViewById(R.id.cancelImageView)

        @SuppressLint("SetTextI18n")
        fun binding(cart: Cart) {
            if (cart.product.image != null) {
                Picasso.get().load(cart.product.image)
                    .placeholder(R.drawable.ic_photo)
                    .resize(200, 200).into(imageView)
            }
            titleTextView.text = cart.product.name
            priceTextView.text = cart.product.price.formatCurrency()
            quantityTextView.text = "${cart.quantity} x${cart.product.type}"
            totalPriceTextView.text = (cart.product.price * cart.quantity).formatCurrency()
        }

        fun handleClickOnCancelButton(id: String) {
            cancelImageView.setOnClickListener { deleteCartWith?.invoke(id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = arr[position]
        holder.binding(cart)
        holder.handleClickOnCancelButton(cart.product.id)
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}