package fe.linksheet.assist

import android.service.voice.VoiceInteractionService
import android.service.voice.VoiceInteractionSession
import java.util.*


class AssistService : VoiceInteractionService() {
    override fun onGetSupportedVoiceActions(voiceActions: MutableSet<String>): MutableSet<String> {
        return super.onGetSupportedVoiceActions(voiceActions)
    }
}
