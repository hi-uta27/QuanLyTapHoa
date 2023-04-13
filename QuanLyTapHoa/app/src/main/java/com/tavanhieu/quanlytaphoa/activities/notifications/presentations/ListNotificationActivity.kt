package com.tavanhieu.quanlytaphoa.activities.notifications.presentations

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.notifications.adapter.NotificationAdapter
import com.tavanhieu.quanlytaphoa.activities.notifications.domain.infra.NotificationUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.notifications.domain.use_cases.NotificationsUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.models.Notification

class ListNotificationActivity : BaseActivity() {
    private lateinit var recycleView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyTextView: TextView

    private val notificationsUseCase: NotificationsUseCase by lazy { NotificationUseCaseImpl() }
    private val adapter: NotificationAdapter by lazy { NotificationAdapter() }

    override fun setContentView() {
        setContentView(R.layout.activity_list_notification)
    }

    override fun mappingViewId() {
        recycleView = findViewById(R.id.recycleView)
        progressBar = findViewById(R.id.progressBar)
        emptyTextView = findViewById(R.id.emptyTextView)
    }

    override fun configLayout() {
        readNotification()
    }

    private fun readNotification() {
        progressBar.visibility = View.VISIBLE
        notificationsUseCase.readNotification({
            readNotificationSuccess(it)
        }, {
            readNotificationFailure()
        })
    }

    private fun readNotificationSuccess(notifications: ArrayList<Notification>) {
        progressBar.visibility = View.GONE
        if (notifications.size == 0) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.GONE
        }
        adapter.setData(this, notifications)
        recycleView.adapter = adapter
    }

    private fun readNotificationFailure() {
        progressBar.visibility = View.GONE
        showAlertDialog(getResourceText(R.string.error),
            getResourceText(R.string.readDepotFailure),
            getResourceText(R.string.tryAgain)
        ) {
            readNotification()
        }
    }
}