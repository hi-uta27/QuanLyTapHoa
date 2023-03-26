package com.tavanhieu.quanlytaphoa.activities.depot.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
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
        private var entryDate: TextView = itemView.findViewById(R.id.entry_date)
        private var name: TextView = itemView.findViewById(R.id.name_product)
        private var description: TextView = itemView.findViewById(R.id.description_product)
        private var price: TextView = itemView.findViewById(R.id.price_product)
        private var quality: TextView = itemView.findViewById(R.id.number_product)

        @SuppressLint("SetTextI18n", "SimpleDateFormat", "ResourceType")
        fun binding(product: Product) {
            if (product.image != null) {
//                Picasso.get().load(product.image).placeholder(R.drawable.ic_wait).into(image)
            }
            entryDate.text = "${context.getResourceText(R.string.entryDate)}: ${
                SimpleDateFormat("dd/MM/yyyy").format(product.entryDate)}"
            name.text = product.name
            description.text = product.description
            price.text = "${context.getResourceText(R.string.price)}: ${product.price.formatCurrency()}"
            quality.text = "${context.getResourceText(R.string.remaining)}: ${product.quantity} ${product.type} - ${
                    context.getResourceText(R.string.expiredDate)}: ${product.expiredDate}"
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
//            val intent = Intent(context, DetailProductActivity::class.java)
//            intent.putExtra("IdProduct", arr[position].id)
//            intent.putExtra("Product", arr[position])
//            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}