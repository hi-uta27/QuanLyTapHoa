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
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.formatCurrency
import com.tavanhieu.quanlytaphoa.commons.models.Cart
import com.tavanhieu.quanlytaphoa.commons.models.Product

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){
    private lateinit var arr: ArrayList<Cart>
    private lateinit var context: BaseActivity
    var deleteCartWith: ((id: String) -> Unit)? = null
    var minusQuantity: ((Cart) -> Unit)? = null
    var plusQuantity: ((Cart) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(context: BaseActivity, arr: ArrayList<Cart>) {
        this.context = context
        this.arr = arr
        notifyDataSetChanged()
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imageView: ImageView = itemView.findViewById(R.id.imageView)
        private var titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private var priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private var quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        private var typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
        private var totalPriceTextView: TextView = itemView.findViewById(R.id.totalPriceTextView)
        private var cancelImageView: ImageView = itemView.findViewById(R.id.cancelImageView)
        private var minusImageView: ImageView = itemView.findViewById(R.id.minusImageView)
        private var plusImageView: ImageView = itemView.findViewById(R.id.plusImageView)

        @SuppressLint("SetTextI18n")
        fun binding(cart: Cart) {
            if (cart.product.image != null) {
                Picasso.get().load(cart.product.image)
                    .placeholder(R.drawable.ic_photo)
                    .resize(200, 200).into(imageView)
            }
            titleTextView.text = cart.product.name
            quantityTextView.text = cart.quantity.toString()
//            typeTextView.text = cart.product.type
            priceTextView.text = "${context.getResourceText(R.string.price)}: " +
                    cart.product.price.formatCurrency()
            totalPriceTextView.text = "${context.getResourceText(R.string.totalPrice)}: " +
                    (cart.product.price * cart.quantity).formatCurrency()
        }

        fun handleClickOnCancelButton(id: String) {
            cancelImageView.setOnClickListener { deleteCartWith?.invoke(id) }
        }

        fun updateQuantity(cart: Cart) {
            minusImageView.setOnClickListener { minusQuantity?.invoke(cart) }
            plusImageView.setOnClickListener { plusQuantity?.invoke(cart) }
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
        holder.updateQuantity(cart)
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}