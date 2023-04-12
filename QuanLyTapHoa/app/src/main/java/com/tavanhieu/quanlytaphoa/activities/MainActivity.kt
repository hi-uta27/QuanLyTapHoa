package com.tavanhieu.quanlytaphoa.activities

import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.home.presentations.HomeFragment
import com.tavanhieu.quanlytaphoa.activities.notifications.domain.infra.NotificationUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.notifications.domain.use_cases.NotificationsUseCase
import com.tavanhieu.quanlytaphoa.activities.notifications.presentations.NotificationActivity
import com.tavanhieu.quanlytaphoa.activities.setting.SettingsFragment
import com.tavanhieu.quanlytaphoa.activities.statistics.presentations.StatisticsFragment
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity

class MainActivity : BaseActivity(), NotificationActivity {
    private lateinit var bottomNavigationView: BottomNavigationView

    override val notificationsUseCase: NotificationsUseCase by lazy { NotificationUseCaseImpl() }
    override val context: BaseActivity = this

    override fun setContentView() {
        setContentView(R.layout.activity_main)
    }

    override fun mappingViewId() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
    }

    override fun configLayout() {
        setCurrentFragment(HomeFragment())
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_home -> setCurrentFragment(HomeFragment())
//                R.id.menu_qr -> TODO: I'll update it after done orther menu
                R.id.menu_statistics -> setCurrentFragment(StatisticsFragment(this))
                R.id.menu_setting -> setCurrentFragment(SettingsFragment())
            }
            true
        }

        // TODO: I'll update it run in background
        checkComingExpiredDateOfProduct()
        checkOutExpiredDateOfProduct()
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, fragment)
            commit()
        }
    }
}