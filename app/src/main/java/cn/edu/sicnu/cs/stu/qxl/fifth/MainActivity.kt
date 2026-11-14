package cn.edu.sicnu.cs.stu.qxl.fifth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    // 三个Fragment，使用newInstance工厂方法
    private val infoFragment = InfoFragment.newInstance()
    private val twoCardFragment = TwoCardFragment.newInstance()
    private val threeCardFragment = ThreeCardFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottom_nav)

        // 首次打开默认加载【个人信息】Fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, infoFragment)
                .commit()
        }

        // 底部导航点击切换Fragment
        bottomNav.setOnNavigationItemSelectedListener { item ->
            val transaction = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.nav_info -> transaction.replace(R.id.fragment_container, infoFragment)
                R.id.nav_two_card -> transaction.replace(R.id.fragment_container, twoCardFragment)
                R.id.nav_three_card -> transaction.replace(R.id.fragment_container, threeCardFragment)
            }
            transaction.commit()
            true
        }
    }
}
