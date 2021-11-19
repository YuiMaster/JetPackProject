package com.hd.jetpackproject.utils.files.selector

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import com.hd.jetpackproject.utils.LOG
import com.hd.jetpackproject.utils.files.selector.parser.SystemImageFileParser
import java.io.File
import javax.inject.Inject


/**
 * @Description:系统图片选择器
 * @Author: liaoyuhuan
 * @CreateDate: 2021/11/18
 */
class SystemImageSelector @Inject constructor(val parser: SystemImageFileParser) : AbsSystemFileSelector(EnSystemFileType.IMAGE) {
    private companion object {
        const val TAG = "SystemImageSelector"
    }

    override fun select(activity: Activity, requestCode: Int, errorCallback: ((Exception) -> Unit)) {
        this.reqCode = requestCode
        this.errorCallback = errorCallback
        LOG.d(TAG, "select ${type.value}")
        activity.selectSystemImage(reqCode)
    }

    override fun handleSystemFile(requestCode: Int, data: Intent?, activity: Activity, callback: (File) -> Unit): Boolean {
        return if (reqCode == requestCode) {
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

    private fun Activity.selectSystemImage(requestId: Int) {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, requestId)
    }
}