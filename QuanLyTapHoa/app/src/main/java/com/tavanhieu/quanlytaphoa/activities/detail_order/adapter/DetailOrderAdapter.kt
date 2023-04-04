package com.tavanhieu.quanlytaphoa.activities.detail_order.adapter

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
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class DetailOrderAdapter : RecyclerView.Adapter<DetailOrderAdapter.DetailOrderViewHolder>(){
    private lateinit var arr: ArrayList<Cart>
    private lateinit var context: BaseActivity

    @SuppressLint("NotifyDataSetChanged")
    fun setData(context: BaseActivity, arr: ArrayList<Cart>) {
        this.arr = arr
        this.context = context
        notifyDataSetChanged()
    }

    inner class DetailOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imageView: ImageView = itemView.findViewById(R.id.imageView)
        private var titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private var priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private var quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)

        @SuppressLint("SetTextI18n")
        fun binding(cart: Cart) {
            cart.product.image?.let {
                Picasso.get().load(it).placeholder(R.drawable.ic_photo).resize(200, 200).into(imageView)
            }
            titleTextView.text = cart.product.name
            priceTextView.text = "${context.getResourceText(R.string.price)}: " + cart.product.price.formatCurrency()
            quantityTextView.text = "${context.getResourceText(R.string.quantity)}: x${cart.quantity} ${cart.product.type}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailOrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_order, parent, false)
        return DetailOrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailOrderViewHolder, position: Int) {
        val cart = arr[position]
        holder.binding(cart)
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}