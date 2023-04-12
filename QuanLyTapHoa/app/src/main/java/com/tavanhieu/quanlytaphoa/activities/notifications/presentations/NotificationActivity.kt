package com.tavanhieu.quanlytaphoa.activities.notifications.presentations

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.detail_product.presentations.DetailProductActivity
import com.tavanhieu.quanlytaphoa.activities.notifications.domain.use_cases.NotificationsUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.models.Notification
import com.tavanhieu.quanlytaphoa.commons.models.Product
import java.util.Calendar

interface NotificationActivity {
    val notificationsUseCase: NotificationsUseCase
    val context: BaseActivity

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun displayNotification(notification: Notification) {
        val notificationChannel: NotificationChannel
        val builder: NotificationCompat.Builder
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "NOTIFICATIONS_QUAN_LY_BAN_HANG"

        val intent = Intent(context, DetailProductActivity::class.java)
        intent.putExtra("IdProduct", notification.idProduct)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_logo))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        NotificationManagerCompat.from(context).notify(notification.id.toLong().toInt(), builder.build())
    }

    // Coming Expired date ---------------------------------------------------------------------

    fun checkComingExpiredDateOfProduct() {
        notificationsUseCase.checkComingExpiredDateOfProduct {
            checkNotificationUseCaseSuccess(context.getResourceText(R.string.productExpiredDate), it)
        }
    }

    // Out Expired date ---------------------------------------------------------------------

    fun checkOutExpiredDateOfProduct() {
        notificationsUseCase.checkOutExpiredDateOfProduct {
            checkNotificationUseCaseSuccess(context.getResourceText(R.string.productOutExpiredDate), it)
        }
    }

    // Quantity of product ---------------------------------------------------------------------
    fun checkQuantityOfProduct() {
        notificationsUseCase.checkQuantityOfProduct {
            checkNotificationUseCaseSuccess(context.getResourceText(R.string.expiredQuantity), it)
        }
    }

    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------

    private fun checkNotificationUseCaseSuccess(message: String, products: ArrayList<Product>) {
        products.forEach {
            val calendar = Calendar.getInstance()
            val notification = Notification(calendar.timeInMillis.toString(), it.name, message, it.id, calendar.time)
            displayNotification(notification)
            // add notifies to database
            notificationsUseCase.addNotification(notification)
        }
    }
}