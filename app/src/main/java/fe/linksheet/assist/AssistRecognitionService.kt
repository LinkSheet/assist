package fe.linksheet.assist

import android.content.Intent
import android.speech.RecognitionService
import android.util.Log

/**
 * Stub recognition service needed to be a complete voice interactor.
 */
class AssistRecognitionService : RecognitionService() {
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
    }

    override fun onStartListening(recognizerIntent: Intent, listener: Callback) {
        Log.d(TAG, "onStartListening")
    }

    override fun onCancel(listener: Callback) {
        Log.d(TAG, "onCancel")
    }

    override fun onStopListening(listener: Callback) {
        Log.d(TAG, "onStopListening")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    companion object {
        private const val TAG = "MainRecognitionService"
    }
}
