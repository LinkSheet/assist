package fe.linksheet.assist

import android.service.voice.VoiceInteractionService


class AssistService : VoiceInteractionService() {
    override fun onGetSupportedVoiceActions(voiceActions: MutableSet<String>): MutableSet<String> {
        return super.onGetSupportedVoiceActions(voiceActions)
    }
}
