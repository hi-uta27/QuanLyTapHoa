package com.tavanhieu.quanlytaphoa.activities

import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.home.fragments.HomeFragment
import com.tavanhieu.quanlytaphoa.activities.setting.fragments.SettingsFragment
import com.tavanhieu.quanlytaphoa.activities.statistics.fragments.StatisticsFragment
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

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
                R.id.menu_statistics -> setCurrentFragment(StatisticsFragment())
                R.id.menu_setting -> setCurrentFragment(SettingsFragment())
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, fragment)
            commit()
        }
    }
}