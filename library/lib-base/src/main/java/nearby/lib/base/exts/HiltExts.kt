//package nearby.lib.base.exts
//
//import android.content.Context
//import nearby.lib.base.uitl.AppUtils
//import nearby.lib.base.uitl.ToastUtils
//
//
//inline fun <reified T> fromApplicationSafe(context: Context): T? {
//    return try {
//        EntryPointAccessors.fromApplication(context, T::class.java)
//    } catch (e: IllegalStateException) {
//        if (AppUtils.isDebug()) {
//            ToastUtils.showToast(
//                context,
//                "IllegalStateException:The implementation of ${T::class.simpleName} cannot be found",
//                true
//            )
//        }
//        LogUtils.e("IllegalStateException:The implementation of ${T::class.simpleName} cannot be found \n ${e.stackTrace}")
//        null
//    } catch (e: Exception) {
//        if (AppUtils.isDebug()) {
//            ToastUtils.showToast(
//                context,
//                "Exception:The implementation of ${T::class.simpleName} cannot be found",
//                true
//            )
//        }
//        LogUtils.e("Exception:The implementation of ${T::class.simpleName} cannot be found \n ${e.stackTrace}")
//        null
//    }
//}