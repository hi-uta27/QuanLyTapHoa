package com.tavanhieu.quanlytaphoa.activities.home.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.detail_product.presentations.DetailProductActivity
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.formatCurrency
import com.tavanhieu.quanlytaphoa.commons.models.Product
import java.text.SimpleDateFormat

class SalesProductAdapter : RecyclerView.Adapter<SalesProductAdapter.AdapterSalesProductViewHolder>() {
    private lateinit var arr: ArrayList<Product>
    private lateinit var context: BaseActivity

    @SuppressLint("NotifyDataSetChanged")
    fun setData(context: BaseActivity, arr: ArrayList<Product>) {
        this.context = context
        this.arr = arr
        notifyDataSetChanged()
    }

    inner class AdapterSalesProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image: ImageView = itemView.findViewById(R.id.imageView)
        private var name: TextView = itemView.findViewById(R.id.nameTextView)
        private var price: TextView = itemView.findViewById(R.id.priceTextView)
        private var quality: TextView = itemView.findViewById(R.id.quantityTextView)

        @SuppressLint("SetTextI18n", "SimpleDateFormat", "ResourceType")
        fun binding(product: Product) {
            if (product.image != null) {
                Picasso.get().load(product.image)
                    .placeholder(R.drawable.ic_photo)
                    .resize(200, 200).into(image)
            }
            name.text = product.name
            price.text = "${context.getResourceText(R.string.price)}: ${product.price.formatCurrency()}"
            quality.text = "${context.getResourceText(R.string.remaining)}: ${product.quantity} ${product.type}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSalesProductViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_product_home, parent, false)
        return AdapterSalesProductViewHolder(mView)
    }

    override fun onBindViewHolder(holder: AdapterSalesProductViewHolder, position: Int) {
        holder.binding(arr[position])

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailProductActivity::class.java)
            intent.putExtra("IdProduct", arr[position].id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}