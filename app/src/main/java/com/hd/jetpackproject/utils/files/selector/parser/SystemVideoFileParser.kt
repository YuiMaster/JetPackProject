package com.hd.jetpackproject.utils.files.selector.parser

import android.content.Context
import android.content.Intent
import com.hd.jetpackproject.utils.files.selector.EnSystemFileType
import com.hudun.androidrecorder.module.record.util.FileSelectUtil
import com.hd.jetpackproject.utils.files.copyFileFromUri
import java.io.File
import javax.inject.Inject


/**
 * @Description:系统视频文件解析器
 * @Author: liaoyuhuan
 * @CreateDate: 2021/11/18
 */
class SystemVideoFileParser @Inject constructor() : AbsSystemFileParser(EnSystemFileType.VIDEO) {

    /**
     * 解析[data]数据转为 File
     */
    override fun parse(context: Context, data: Intent?): File {
        data?.data?.let { uri ->
            try {
                val filePath = FileSelectUtil.getFilePath(context, uri)
                var file: File? = null
                if (!filePath.isNullOrEmpty()) {
                    file = File(filePath)
                }
                return if (file != null && file.exists()) {
                    file
                } else {
                    uri.copyFileFromUri(context.applicationContext, File(context.cacheDir, "import"))
                }
            } catch (e: Exception) {
                throw Exception("数据解析失败")
            }
        }
        throw Exception("url为空")
    }

}