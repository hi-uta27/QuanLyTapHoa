package com.tavanhieu.quanlytaphoa.activities.notifications.adapter

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
import com.tavanhieu.quanlytaphoa.activities.detail_product.presentations.DetailProductActivity
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.formatCurrency
import com.tavanhieu.quanlytaphoa.commons.formatDate
import com.tavanhieu.quanlytaphoa.commons.formatDateAndTime
import com.tavanhieu.quanlytaphoa.commons.formatTime
import com.tavanhieu.quanlytaphoa.commons.models.Employee
import com.tavanhieu.quanlytaphoa.commons.models.Notification
import com.tavanhieu.quanlytaphoa.commons.models.Order
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import java.text.SimpleDateFormat

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationAdapter>(){
    private lateinit var arr: ArrayList<Notification>
    private lateinit var context: BaseActivity

    @SuppressLint("NotifyDataSetChanged")
    fun setData(context: BaseActivity, arr: ArrayList<Notification>) {
        this.context = context
        this.arr = arr
        notifyDataSetChanged()
    }

    inner class NotificationAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private var messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        private var dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun binding(notification: Notification) {
            titleTextView.text = notification.title
            messageTextView.text = notification.message
            dateTextView.text = notification.date.formatDateAndTime()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationAdapter(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter, position: Int) {
        val notification = arr[position]
        holder.binding(notification)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailProductActivity::class.java)
            intent.putExtra("IdProduct", notification.idProduct)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}