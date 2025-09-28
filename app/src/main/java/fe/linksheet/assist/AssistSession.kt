package fe.linksheet.assist

import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.service.voice.VoiceInteractionSession
import android.widget.Toast
import androidx.core.content.getSystemService
import app.linksheet.lib.flavors.LinkSheetApp
import fe.composekit.core.AndroidVersion
import fe.composekit.extension.componentName
import fe.std.result.isSuccess
import fe.std.result.tryCatch


class AssistSession(context: Context) : VoiceInteractionSession(context) {
    private val clipboardManager by lazy {
        context.applicationContext.getSystemService<ClipboardManager>()
    }

    override fun onHandleAssist(state: AssistState) {
        super.onHandleAssist(state)

        if (AndroidVersion.isAtLeastApi29Q()) {
            val webUri = state.assistContent?.webUri
            if (webUri != null) {
                LinkSheetApp.LinkSheet.start(webUri)
            } else {
                Toast.makeText(context, R.string.no_uri_provided, Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }

    private fun LinkSheetApp.LinkSheet.start(uri: Uri): Boolean {
        val infos = find(context)
        for (info in infos) {
            val intent = createIntent(uri, info.componentName)
            val result = tryCatch { startAssistantActivity(intent) }
            if (result.isSuccess()) return true
        }

        return false
    }
}
