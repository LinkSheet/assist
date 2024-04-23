package fe.linksheet.assist

import android.content.Context
import android.content.Intent
import android.service.voice.VoiceInteractionSession
import android.widget.Toast


class AssistSession(context: Context) : VoiceInteractionSession(context) {
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

    override fun onHandleAssist(state: AssistState) {
        super.onHandleAssist(state)

        val webUri = state.assistContent?.webUri
        if (webUri != null) {
            for (pkg in packages) {
                runCatching {
                    startAssistantActivity(Intent(Intent.ACTION_VIEW, webUri).setPackage(pkg))
                }
            }
        } else {
            Toast.makeText(context, R.string.no_uri_provided, Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}
