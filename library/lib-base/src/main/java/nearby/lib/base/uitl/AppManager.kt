package nearby.lib.base.uitl

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference
import java.util.*
import kotlin.system.exitProcess


/**
 * @author:
 * @created on: 2022/10/25 11:40
 * @description:
 */
/**
 * Activity管理类
 */
class AppManager private constructor() {

    private var sCurrentActivityWeakRef: WeakReference<AppCompatActivity>? = null
    private val activityUpdateLock = Any()

    fun getCurrentActivity(): AppCompatActivity? {
        var currentActivity: AppCompatActivity? = null
        synchronized(activityUpdateLock) {
            if (sCurrentActivityWeakRef != null) {
                currentActivity = sCurrentActivityWeakRef!!.get()
            }
        }
        return currentActivity
    }

    fun setCurrentActivity(activity: AppCompatActivity) {
        synchronized(activityUpdateLock) {
            sCurrentActivityWeakRef = WeakReference(activity)
        }
    }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: AppCompatActivity?) {
        activityStack.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): AppCompatActivity {
        return activityStack.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity: AppCompatActivity? = activityStack.lastElement()
        if (activity != null) {
            activity.finish()
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: AppCompatActivity?) {
        if (activity != null) {
            activityStack.remove(activity)
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size: Int = activityStack.size
        val popStack: Stack<AppCompatActivity> = Stack()
        while (i < size) {
            val clays = activityStack[i]
            if (null != clays) {
                val simpleName = activityStack[i].javaClass.simpleName
                if (simpleName != "HomeMainActivity") {
                    activityStack[i].finish()
                } else {
                    popStack.add(clays)
                }
            }
            i++
        }
        activityStack.removeAllElements()
        activityStack.addAll(popStack)
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            val activityMgr =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(context.packageName)
            exitProcess(0)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private var activityStack: Stack<AppCompatActivity> = Stack()
        fun getInstance() = Helper.instance

        /**
         * 单一实例
         */

        private object Helper {
            val instance = AppManager()
        }
    }
}