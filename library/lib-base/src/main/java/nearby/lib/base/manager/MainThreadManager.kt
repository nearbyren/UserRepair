package nearby.lib.base.manager

import android.os.Handler
import android.os.Looper

class MainThreadManager(onCommonSuccessCallback: OnCommonSuccessCallback) {

    init {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            // If we finish marking off of the main thread, we need to
            // actually do it on the main thread to ensure correct ordering.
            val mainThreadHandler = Handler(Looper.getMainLooper())
            mainThreadHandler.post {
                onCommonSuccessCallback.onSuccess()
                mainThreadHandler.removeCallbacksAndMessages(null)
            }
        } else {
            onCommonSuccessCallback.onSuccess()
        }
    }

}