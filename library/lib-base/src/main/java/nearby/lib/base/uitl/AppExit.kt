package nearby.lib.base.uitl

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.CallSuper
import nearby.lib.base.log.LogUtil
import nearby.lib.base.R
import kotlin.system.exitProcess

/**
 * APP退出监听
 */
object AppExit {

    private var preExit = false

    private val handler = Handler(Looper.getMainLooper()) {
        preExit = false
        true
    }

    @CallSuper
    fun onBackPressed(
        activity: Activity,
        tipCallback: () -> Unit = {
            Toast.makeText(
                activity,
                activity.getString(R.string.base_app_exit_one_more_press),
                Toast.LENGTH_SHORT
            ).show()
        },
        exitCallback: () -> Unit = {}
    ) {
        if (!preExit) {
            preExit = true
            tipCallback()
            handler.sendEmptyMessageDelayed(0, 2000)
        } else {
            exitCallback()
            LogUtil.flushLog()
            activity.finish()
            exitProcess(0)
        }
    }
}