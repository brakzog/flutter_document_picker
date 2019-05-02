package com.sidlatau.flutterdocumentpicker

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterDocumentPickerPlugin(
        private val delegate: FlutterDocumentPickerDelegate
) : MethodCallHandler {
    companion object {
        const val TAG = "flutter_document_picker"
        private const val ARG_ALLOWED_FILE_EXTENSIONS = "allowedFileExtensions"
        private const val ARG_ALLOWED_MIME_TYPE = "allowedMimeType"
        private const val ARG_INVALID_FILENAME_SYMBOLS = "invalidFileNameSymbols"
        private const val ARGS_MIME_TYPES = "mimeTypes"

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "flutter_document_picker")

            val delegate = FlutterDocumentPickerDelegate(
                    activity = registrar.activity()
            )

            registrar.addActivityResultListener(delegate)

            channel.setMethodCallHandler(
                    FlutterDocumentPickerPlugin(delegate)
            )
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
       /* val test = parseList(call, ARGS_MIME_TYPES)

        val array = arrayOf<String>()
        test?.toArray(array)*/

        if (call.method == "pickDocument") {

            delegate.pickDocument(
                    result,
                    allowedFileExtensions = parseList(call, ARG_ALLOWED_FILE_EXTENSIONS),
                    invalidFileNameSymbols = parseList(call, ARG_INVALID_FILENAME_SYMBOLS),
                    mimeTypes =  parseArray(call, ARGS_MIME_TYPES)
            )
        } else {
            result.notImplemented()
        }
    }
    private fun parseList(call: MethodCall, arg: String): ArrayList<String>? {
        if (call.hasArgument(arg)) {
            return call.argument<ArrayList<String>>(arg)
        }
        return null
    }

    private fun parseString(call: MethodCall, arg: String): String? {
        if (call.hasArgument(arg)) {
            return call.argument<String>(arg)
        }
        return null
    }

    private fun parseArray(call: MethodCall, arg: String): Array<String>? {
        if (call.hasArgument(arg)) {
            val list = call.argument<ArrayList<String>>(arg)
           val result = list!!.toTypedArray()
            return result
        }
        return null
    }
}
