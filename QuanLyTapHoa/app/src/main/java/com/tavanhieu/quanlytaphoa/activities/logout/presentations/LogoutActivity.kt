package com.tavanhieu.quanlytaphoa.activities.logout.presentations

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.tavanhieu.quanlytaphoa.activities.logout.domain.use_case.LogoutUseCase

interface LogoutActivity {
    var logoutUseCase: LogoutUseCase
}

fun LogoutActivity.logout(context: Context) {
    AlertDialog.Builder(context)
        .setTitle("Xác nhận")
        .setMessage("Bạn có muốn đăng xuất?")
        .setPositiveButton("Đồng ý") { _, _ ->
            logoutUseCase.logoutCurrentUser()
        }
        .setNegativeButton("Hủy") { dialog, _ -> dialog.dismiss() }
        .show()
}