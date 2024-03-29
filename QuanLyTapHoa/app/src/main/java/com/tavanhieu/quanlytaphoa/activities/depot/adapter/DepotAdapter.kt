package com.tavanhieu.quanlytaphoa.activities.depot.adapter

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

class DepotAdapter : RecyclerView.Adapter<DepotAdapter.AdapterDepotViewHolder>() {
    private lateinit var arr: ArrayList<Product>
    private lateinit var context: BaseActivity

    @SuppressLint("NotifyDataSetChanged")
    fun setData(context: BaseActivity, arr: ArrayList<Product>) {
        this.context = context
        this.arr = arr
        notifyDataSetChanged()
    }

    inner class AdapterDepotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image: ImageView = itemView.findViewById(R.id.img_product)
        private var entryDate: TextView = itemView.findViewById(R.id.entryDateTextView)
        private var name: TextView = itemView.findViewById(R.id.nameTextView)
        private var description: TextView = itemView.findViewById(R.id.descriptionTextView)
        private var price: TextView = itemView.findViewById(R.id.priceTextView)
        private var quality: TextView = itemView.findViewById(R.id.quantityTextView)

        @SuppressLint("SetTextI18n", "SimpleDateFormat", "ResourceType")
        fun binding(product: Product) {
            if (product.image != null) {
                Picasso.get().load(product.image)
                    .placeholder(R.drawable.ic_photo)
                    .resize(200, 200).into(image)
            }
            entryDate.text = "${context.getResourceText(R.string.entryDate)}: ${
                SimpleDateFormat("dd/MM/yyyy").format(product.entryDate)}"
            name.text = product.name
            if (product.description == "") {
                description.text = context.getResourceText(R.string.noDescription)
            } else {
                description.text = product.description
            }
            price.text = "${context.getResourceText(R.string.price)}: ${product.price.formatCurrency()}"
            quality.text = "${context.getResourceText(R.string.remaining)}: ${product.quantity} ${product.type}\n" +
                    "${context.getResourceText(R.string.expiredDate)}: ${SimpleDateFormat("dd/MM/yyyy").format(product.expiredDate)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterDepotViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_depot, parent, false)
        return AdapterDepotViewHolder(mView)
    }

    override fun onBindViewHolder(holder: AdapterDepotViewHolder, position: Int) {
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