package apps.user.repair.http

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import nearby.lib.mvvm.vm.BaseAppViewModel

/**
 * @author: lr
 * @created on: 2023/12/18 9:14 PM
 * @description:
 */
class IndexViewModel : BaseAppViewModel() {

    val start = MutableLiveData<Boolean>()
    fun start() {
        launchOnUI {
            delay(2000)
            start.value = true
        }
    }

}