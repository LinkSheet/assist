package fe.linksheet.assist

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat


class MainActivity : ComponentActivity() {

    companion object {
        private val basePkg = "fe.linksheet"

        val packages = setOf(
            // Prefer nightly for now
            "$basePkg.nightly",
            "$basePkg.pro.nightly",
            basePkg,
            "$basePkg.pro",
            "$basePkg.debug",
            "$basePkg.pro.debug",
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            v.setPadding(0, 0, 0, 0)

            insets
        }

        window.setBackgroundDrawable(ColorDrawable(0))
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

        window.setType(type)

//        if (intent != null) {
//            val baseIntent = Intent(Intent.ACTION_VIEW, intent.data).addCategory(Intent.CATEGORY_BROWSABLE)
//            if (tryStart(baseIntent)) {
//                finish()
//            }
//        }
    }

//    private fun tryStart(baseIntent: Intent): Boolean {
//        for (pkg in packages) {
//            runCatching {
//                startActivity(baseIntent.setPackage(pkg))
//                return true
//            }
//        }
//
//        return false
//    }
}
