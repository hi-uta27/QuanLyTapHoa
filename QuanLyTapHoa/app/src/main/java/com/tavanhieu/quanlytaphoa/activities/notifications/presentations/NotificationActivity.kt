package com.tavanhieu.quanlytaphoa.activities.notifications.presentations

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.depot.presentations.DepotActivity
import com.tavanhieu.quanlytaphoa.activities.notifications.domain.use_cases.NotificationsUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.models.Product

interface NotificationActivity {
    val notificationsUseCase: NotificationsUseCase
    val context: BaseActivity
//    private lateinit var notificationManager: NotificationManager
//    private lateinit var notificationChannel: NotificationChannel
//    private lateinit var builder: NotificationCompat.Builder

    // ---------------------------------------------------------------------

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun displayNotification(message: String) {
        val intent = Intent(context, DepotActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // checking if android version is greater than oreo(API 26) or not
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationChannel = NotificationChannel(chanelId, chanelId, NotificationManager.IMPORTANCE_HIGH)
//            notificationChannel.enableLights(true)
//            notificationChannel.enableVibration(false)
//            notificationManager.createNotificationChannel(notificationChannel)
//        }

        val chanelId = "NOTIFICATIONS_QUAN_LY_BAN_HANG"
        val builder = NotificationCompat.Builder(context, chanelId)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(context.getResourceText(R.string.title_app))
            .setContentText(message)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_logo))
            .setContentIntent(pendingIntent)

        NotificationManagerCompat.from(context).notify(1234, builder.build())
    }

    // ---------------------------------------------------------------------

    fun checkExpiredDateOfProduct() {
        notificationsUseCase.checkExpiredDateOfProduct {
            checkExpiredDateOfProductSuccess(it)
        }
    }

    private fun checkExpiredDateOfProductSuccess(products: ArrayList<Product>) {
        if (products.size != 0) {
            displayNotification(context.getResourceText(R.string.productExpiredDate))
        }
    }
}