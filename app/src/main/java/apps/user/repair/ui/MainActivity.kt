package apps.user.repair.ui

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import apps.user.repair.R
import apps.user.repair.databinding.ActivityMainBinding
import apps.user.repair.fragment.IndexFragment1
import apps.user.repair.fragment.IndexFragment2
import apps.user.repair.fragment.IndexFragment3
import apps.user.repair.fragment.IndexFragment4
import nearby.lib.base.uitl.AppManager
import nearby.lib.base.uitl.ToastUtils
import nearby.lib.mvvm.activity.BaseAppBindActivity


class MainActivity : BaseAppBindActivity<ActivityMainBinding>() {

    private val index1 by lazy { IndexFragment1() }
    private val index2 by lazy { IndexFragment2() }
    private val index3 by lazy { IndexFragment3() }
    private val index4 by lazy { IndexFragment4() }


    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_item1 -> {
                    switchFragment(index1)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_item2 -> {
                    switchFragment(index2)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_item3 -> {
                    switchFragment(index3)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_item4 -> {
                    switchFragment(index4)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun initialize(savedInstanceState: Bundle?) {

        binding.bottomNavigation.background = ContextCompat.getDrawable(
            this,
            R.drawable.index_index_bg_white_main
        )
        binding.bottomNavigation.itemIconTintList = null
        binding.bottomNavigation.setOnNavigationItemSelectedListener(
            onNavigationItemSelectedListener
        )
        // 初始显示第一个 Fragment
        switchFragment(index1)
        initBadge()

    }

    private fun initBadge() {
        //获取底部菜单view
        val menuView = binding.bottomNavigation.getChildAt(0) as BottomNavigationMenuView
        //获取第2个itemView
        menuView?.forEach {
            if (it.id == R.id.navigation_item2) {
                val itemView = it as BottomNavigationItemView
                //引入badgeView
                val badgeView = layoutInflater.inflate(R.layout.menu_badge, menuView, false)
                val ivBadge = badgeView.findViewById<ImageView>(R.id.iv_badge)
                //把badgeView添加到itemView中
                itemView.addView(badgeView)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                AppManager.getInstance().finishAllActivity()
            } else {
                ToastUtils.showToast("再按一次退出！")
                isExit = true
                Handler().postDelayed({
                    isExit = false
                },2000)

            }
            return true;
        }
        return super.onKeyDown(keyCode, event)
    }

    var isExit: Boolean = false
}