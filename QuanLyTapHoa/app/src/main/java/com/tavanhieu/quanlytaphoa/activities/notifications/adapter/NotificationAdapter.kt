package com.tavanhieu.quanlytaphoa.activities.notifications.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.formatDateAndTime
import com.tavanhieu.quanlytaphoa.commons.models.Notification

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationAdapter>(){
    private lateinit var arr: ArrayList<Notification>
    private lateinit var context: BaseActivity

    var touchUpInsideItemView: ((Notification) -> Unit)? = null
    var touchUpInsideDeleteImageView: ((Notification) -> Unit)? = null

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
        private var deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun binding(notification: Notification) {
            titleTextView.text = notification.title
            messageTextView.text = notification.message
            dateTextView.text = notification.date.formatDateAndTime()

            if (notification.isRead) {
                titleTextView.typeface = Typeface.DEFAULT
                messageTextView.typeface = Typeface.DEFAULT
                dateTextView.typeface = Typeface.DEFAULT
            } else {
                titleTextView.typeface = Typeface.DEFAULT_BOLD
                messageTextView.typeface = Typeface.DEFAULT_BOLD
                dateTextView.typeface = Typeface.DEFAULT_BOLD
            }
        }

        fun handleClickOnView(notification: Notification) {
            itemView.setOnClickListener {
                touchUpInsideItemView?.invoke(notification)
            }

            deleteImageView.setOnClickListener {
                touchUpInsideDeleteImageView?.invoke(notification)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationAdapter(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter, position: Int) {
        val notification = arr[position]
        holder.binding(notification)
        holder.handleClickOnView(notification)
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}