package com.hd.jetpackproject.utils.files.selector

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import com.hd.jetpackproject.utils.LOG
import com.hd.jetpackproject.utils.files.selector.parser.SystemOtherFileParser
import java.io.File
import javax.inject.Inject


/**
 * @Description:系统图片选择器
 * @Author: liaoyuhuan
 * @CreateDate: 2021/11/18
 */
class SystemOtherFileSelector @Inject constructor(val parser: SystemOtherFileParser) : AbsSystemFileSelector(EnSystemFileType.OTHER) {
    private companion object {
        const val TAG = "SystemOtherFileSelector"
    }

    override fun select(activity: Activity, requestCode: Int, errorCallback: ((Exception) -> Unit)) {
        reqCode = requestCode
        this.errorCallback = errorCallback
        LOG.d(TAG, "select ${type.value}")
        activity.selectSystemOtherFile(reqCode)
    }

    override fun handleSystemFile(requestCode: Int, data: Intent?, activity: Activity, callback: (File) -> Unit): Boolean {
        return if (requestCode == reqCode) {
            try {
                val file = parser.parse(activity, data)
                LOG.d(TAG, "handleSystemFile $file")
                callback.invoke(file)
            } catch (e: Exception) {
                errorCallback?.invoke(e)
                return false
            }
            true
        } else {
            false
        }
    }

    private fun Activity.selectSystemOtherFile(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        try {
            startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            throw Exception("找不到文件管理器")
        }
    }
}