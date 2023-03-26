package com.tavanhieu.quanlytaphoa.activities.logout.presentations

import androidx.appcompat.app.AlertDialog
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.logout.domain.use_case.LogoutUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity

interface LogoutActivity {
    var logoutUseCase: LogoutUseCase
}

fun LogoutActivity.logout(context: BaseActivity) {
    AlertDialog.Builder(context)
        .setTitle(context.getResourceText(R.string.notification))
        .setMessage(context.getResourceText(R.string.DoYouWantToSignOut))
        .setPositiveButton(context.getResourceText(R.string.confirm)) { _, _ ->
            logoutUseCase.logoutCurrentUser()
        }
        .setNegativeButton(context.getResourceText(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
        .show()
}