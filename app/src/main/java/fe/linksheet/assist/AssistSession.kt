package fe.linksheet.assist

import android.app.assist.AssistContent
import android.app.assist.AssistStructure.ViewNode
import android.app.assist.AssistStructure.WindowNode
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import android.text.TextUtils
import android.util.Log


class AssistSession(context: Context) : VoiceInteractionSession(context) {
    companion object {
        const val TAG: String = "MainInteractionSession"
    }

    override fun onShow(args: Bundle?, showFlags: Int) {
        super.onShow(args, showFlags)

        Log.i(TAG, "onShow: flags=0x" + Integer.toHexString(showFlags) + " args=" + args)
        if (args != null) {
            for (key in args.keySet()) {
                Log.i(TAG, "\tkey=${args.get(key)}")
            }
        }
//        onHandleScreenshot(null)
    }


    //    override fun onHandleAssistSecondary(
//        data: Bundle?,
//        structure: AssistStructure?,
//        content: AssistContent?,
//        index: Int,
//        count: Int
//    ) {
//        super.onHandleAssistSecondary(data, structure, content, index, count)
//    }
    override fun onHandleAssist(state: AssistState) {
        super.onHandleAssist(state)

        val x = state.activityId

        val webUri = state.assistContent?.webUri
        if (webUri != null) {
            for (pkg in MainActivity.packages) {
                runCatching {
                    startAssistantActivity(Intent(Intent.ACTION_VIEW, webUri).setPackage(pkg))
                    finish()
                }
            }


        }



        Log.i(TAG, "onHandleAssist: ${state.activityId}")

//        logAssistContentAndData(state.assistContent, state.assistData)
//        try {
//            val structuredData = JSONObject(state.assistContent!!.structuredData)
//            Toast.makeText(context, structuredData.optString("description"), Toast.LENGTH_LONG).show()
//        } catch (e: JSONException) {
//            e.printStackTrace()

//        }

        Log.i(TAG, "assistStructure=${state.assistStructure}")
//        if (state.assistStructure != null) {
//            val structure = state.assistStructure!!
//            val componentName = structure.activityComponent
//            val nodeCount = structure.getWindowNodeCount()
//            Log.i(TAG, "nodeCount=${nodeCount}")
//
//            for (i in 0 until nodeCount) {
//                val windowNode: WindowNode = structure.getWindowNodeAt(i)
//                getAllContentText(windowNode.rootViewNode, 5)
//            }
//        }
    }


    private fun getAllContentText(node: ViewNode?, levelMax: Int) {
        if (node != null) {
            val nodeCount = node.childCount
            val webDomain = node.webDomain
            if (!webDomain.isNullOrEmpty()) {
                Log.i(TAG, "webDomain=$webDomain")
            }

            val text = node.text
            val description = node.contentDescription

            if (!text.isNullOrEmpty()) {
                Log.i(TAG, "text=$text")
            }

            if (!description.isNullOrEmpty()) {
                Log.i(TAG, "description=$description")
            }

            val tag = node.htmlInfo?.tag
            if (!tag.isNullOrEmpty()) {
                Log.i(TAG, "tag=$tag")
            }


//            if (!TextUtils.isEmpty(node.text)) {
////                mContent += node.text.toString() + " "
//            }
//
//            if (!TextUtils.isEmpty(node.contentDescription)) {
////                mDescription += node.contentDescription.toString() + " "
//            }
            for (i in 0 until nodeCount) {
                val childNode = node.getChildAt(i)
                getAllContentText(childNode, levelMax)
            }
        }
    }


    private fun logAssistContentAndData(content: AssistContent?, data: Bundle?) {
        if (content != null) {
            Log.i(TAG, "Assist intent: " + content.intent)
            Log.i(TAG, "Assist intent from app: " + content.isAppProvidedIntent)
            Log.i(TAG, "Assist clipdata: " + content.clipData)
            Log.i(TAG, "Assist structured data: " + content.structuredData)
            Log.i(TAG, "Assist web uri: " + content.webUri)
            Log.i(TAG, "Assist web uri from app: " + content.isAppProvidedWebUri)
            Log.i(TAG, "Assist extras: " + content.extras)
        }
        if (data != null) {

            Log.i(TAG, "Data bundle: $data")
            val referrer = data.getParcelable<Uri>(Intent.EXTRA_REFERRER)
            if (referrer != null) {
                Log.i(TAG, "Referrer: $referrer")
            }

            for (key in data.keySet()) {
                Log.i(TAG, "\tkey=${data.get(key)}")
            }
        }
    }


    override fun onHandleScreenshot(screenshot: Bitmap?) {

    }


    override fun onTaskStarted(intent: Intent, taskId: Int) {
        super.onTaskStarted(intent, taskId)
        Log.i(TAG, "onTaskStarted $intent $taskId")
    }

    override fun onTaskFinished(intent: Intent, taskId: Int) {
        super.onTaskFinished(intent, taskId)
        Log.i(TAG, "onTaskFinished $intent $taskId")
    }

    override fun onLockscreenShown() {

    }


    override fun onGetSupportedCommands(commands: Array<String>): BooleanArray {
//        val res = BooleanArray(commands.size)
//        for (i in commands.indices) {
//            if ("com.android.test.voiceinteraction.COMMAND" == commands[i]) {
//                res[i] = true
//            }
//        }
        return commands.map { true }.toBooleanArray()
    }


    override fun onRequestConfirmation(request: ConfirmationRequest) {
        Log.i(
            TAG, "onConfirm: prompt=${request.voicePrompt} extras=${request.extras}"
        )
    }

    override fun onRequestPickOption(request: PickOptionRequest) {
        Log.i(
            TAG,
            "onPickOption: prompt=${request.voicePrompt} options=${request.options.contentToString()} extras=${request.extras}"
        )

    }


    override fun onRequestCompleteVoice(request: CompleteVoiceRequest) {
        Log.i(
            TAG, "onCompleteVoice: message=${request.voicePrompt} extras=${request.extras}"
        )

    }

    override fun onRequestAbortVoice(request: AbortVoiceRequest) {
        Log.i(TAG, "onAbortVoice: message=${request.voicePrompt} extras=${request.extras}")
//        setPrompt(request.voicePrompt)

    }

    override fun onRequestCommand(request: CommandRequest) {
        val extras = request.extras
        extras?.getString("arg")
        Log.i(TAG, "onCommand: command=" + request.command + " extras=" + extras)

    }

    override fun onCancelRequest(request: Request) {
        Log.i(TAG, "onCancel")
//
//        request.cancel()
    }
}
