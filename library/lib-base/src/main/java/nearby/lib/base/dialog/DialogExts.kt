package nearby.lib.base.dialog

import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * @description: Dialog相关扩展
 * <p>
 * 1、DialogFragment是继承于Fragment，在内部创建了一个Dialog对象
 * 2、在show和dismiss方法中通过对Fragment的add和remove来显示Dialog
 * 3、内部Dialog通过onCreateDialog方法自动创建。Dialog在onActivityCreated方法中setContentView()
 * 4、每次dismiss时，Fragment会一直执行完整个生命周期（onDetach），所以每次show的时候，生命周期会重新走一遍
 * 5、生命周期执行顺序：onAttach-->onCreate-->onCreateDialog-->onCreateView-->onViewCreated-->onActivityCreated-->onStart
 * </p>
 * @since: 1.0.0
 */


/**
 * DialogFragment扩展方法，
 * 以commitNowAllowingStateLoss方式提交，防止出现 can not perform this action after onSaveInstance  DialogFragment 异常
 */
fun DialogFragment.showAllowStateLoss(manager: FragmentManager, tag: String) {
    try {
        val mDismissed = DialogFragment::class.java.getDeclaredField("mDismissed")
        mDismissed.isAccessible = true
        mDismissed.set(this, false)
        val mShownByMe = DialogFragment::class.java.getDeclaredField("mShownByMe")
        mShownByMe.isAccessible = true
        mShownByMe.set(this, true)
        manager.beginTransaction().add(this, tag).commitNowAllowingStateLoss()
    } catch (e: Exception) {
        try {
            manager.beginTransaction().add(this, tag).commitNowAllowingStateLoss()
        } catch (exception: Exception) {
            Log.i("DialogFragment", "showAllowStateLoss error")
        }
        Log.i("DialogFragment", "reflect showAllowStateLoss error")
    }
}
