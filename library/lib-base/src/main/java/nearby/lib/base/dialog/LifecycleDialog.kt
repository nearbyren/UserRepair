package nearby.lib.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @description: 感知Activity生命周期变化的Dialog
 * @since: 1.0.0
 */
open class LifecycleDialog : AppCompatDialog, LifecycleObserver {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, theme: Int) : super(context, theme)

    constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    protected val lifecycleOwner by lazy {
        if (context is LifecycleOwner) {
            (context as LifecycleOwner).lifecycle
        } else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleOwner?.addObserver(this)
    }

    override fun dismiss() {
        lifecycleOwner?.removeObserver(this)
        super.dismiss()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onOwnerDestroy(owner: LifecycleOwner) {
        if (isShowing) {
            dismiss()
        }
    }
}