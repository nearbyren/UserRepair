package nearby.lib.base.state.util

import android.os.Looper

object StateBoxUtil {
    val isMainThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()

    @SafeVarargs
    fun <T> checkNotNull(vararg arg: T): Boolean {
        if (arg == null || arg.size == 0) {
            return false
        }
        for (t in arg) {
            if (t == null) {
                return false
            }
        }
        return true
    }
}

