package com.hd.jetpackproject.utils.files.selector

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import com.hd.jetpackproject.utils.LOG
import com.hd.jetpackproject.utils.files.selector.parser.SystemVideoFileParser
import java.io.File
import javax.inject.Inject


/**
 * @Description:系统视频选择器
 * @Author: liaoyuhuan
 * @CreateDate: 2021/11/18
 */
class SystemVideoSelector @Inject constructor(val parser: SystemVideoFileParser) : AbsSystemFileSelector(EnSystemFileType.VIDEO) {
    private companion object {
        const val TAG = "SystemVideoSelector"
    }

    override fun select(activity: Activity, requestCode: Int, errorCallback: ((Exception) -> Unit)) {
        reqCode = requestCode
        this.errorCallback = errorCallback
        LOG.d("SystemVideoSelector ", "select ${type.value}")
        selectSystemVideoFile(activity, reqCode)
    }

    override fun handleSystemFile(requestCode: Int, data: Intent?, activity: Activity, callback: (File) -> Unit): Boolean {
        LOG.d(TAG, "handleSystemFile  ")
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

    private fun selectSystemVideoFile(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
        activity.startActivityForResult(intent, requestCode)
    }


    private fun selectSystemVideoFile2(activity: Activity, requestCode: Int) {
        // 打开文件管理器选择文件
        val intent = Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType(“image/*”);//选择图片
//        intent.setType(“audio/*”); //选择音频
        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
//        intent.setType(“video/*;image/*”);//同时选择视频和图片
//        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        activity.startActivityForResult(intent, requestCode)
    }

}