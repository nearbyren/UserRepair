package nearby.lib.web.ui

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.ProgressBar
import nearby.lib.web.base.BaseAppWebBindActivity
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.web.R
import nearby.lib.web.databinding.WebStaticActivityBinding

/**
 * @author:
 * @created on: 2022/8/25 16:06
 * @description:共用常用
 */
class WebStaticActivity : BaseAppWebBindActivity<WebStaticActivityBinding>() {


    var webTitle: String? = null

    override fun layoutRes(): Int {
        return R.layout.web_static_activity
    }

    private val webViewContainer by lazy {
        findViewById<ViewGroup>(R.id.webViewContainer)
    }

    override fun addProgress(): ProgressBar {
        return binding.progress
    }


    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        initWebViewType(currentRoutine)
        var webUrl: String = "https://nearby.ren"
        webUrl?.let { mWebRoutine.loadUrl(it) }
        webViewContainer.addView(
            mWebRoutine, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }


    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(true).setTitle(title = "").setBgColor(nearby.lib.base.R.color.white).build()
    }

    override fun webPageTitle(view: WebView?, url: String?) {
        super.webPageTitle(view, url)
        webTitle?.let {
            barHelper.util().setTitle(it)
        } ?: view?.let {
            val title = view.title ?: ""
            barHelper.util().setTitle(title)
        }
    }

    override fun onDestroy() {
        webViewContainer.removeAllViews()
        super.onDestroy()
    }
}