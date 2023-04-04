package com.tavanhieu.quanlytaphoa.activities.order.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.detail_order.presentations.DetailOrderActivity
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.formatCurrency
import com.tavanhieu.quanlytaphoa.commons.models.Employee
import com.tavanhieu.quanlytaphoa.commons.models.Order
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import java.text.SimpleDateFormat

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.CartViewHolder>(){
    private lateinit var arr: ArrayList<Order>
    private lateinit var context: BaseActivity

    @SuppressLint("NotifyDataSetChanged")
    fun setData(context: BaseActivity, arr: ArrayList<Order>) {
        this.context = context
        this.arr = arr
        notifyDataSetChanged()
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imageView: ImageView = itemView.findViewById(R.id.imageView)
        private var nameEmployeeTextView: TextView = itemView.findViewById(R.id.nameEmployeeTextView)
        private var entryDateTextView: TextView = itemView.findViewById(R.id.entryDateTextView)
        private var idTextView: TextView = itemView.findViewById(R.id.idTextView)
        private var remainingTextView: TextView = itemView.findViewById(R.id.remainingTextView)

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun binding(order: Order) {
            entryDateTextView.text = "${context.getResourceText(R.string.creationDate)}: " + order.convertDateToString()
            FirebaseNetworkLayer.instance.getRequest("Employee", {
                val employee = it.getValue(Employee::class.java)
                if (employee != null) {
                    nameEmployeeTextView.text = "${context.getResourceText(R.string.employeeCreation)}: ${employee.name}"
                }
            }, {})
            idTextView.text = "ID: ${order.id}"
            remainingTextView.text = "${context.getResourceText(R.string.totalPrice)}: " + order.totalPrice().formatCurrency()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val order = arr[position]
        holder.binding(order)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailOrderActivity::class.java)
            intent.putExtra("IdOrder", order.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}