package com.hd.jetpackproject.utils.files.selector.parser

import android.content.Context
import android.content.Intent
import com.hd.jetpackproject.utils.files.parseSysFileManagerUriByDefault
import com.hd.jetpackproject.utils.files.selector.EnSystemFileType
import java.io.File
import javax.inject.Inject


/**
 * @Description: 系统文件解析器
 * @Author: liaoyuhuan
 * @CreateDate: 2021/11/18
 */
class SystemOtherFileParser @Inject constructor() : AbsSystemFileParser(EnSystemFileType.IMAGE) {

    /**
     * 解析[data]数据转为 File
     */
    override fun parse(context: Context, data: Intent?): File {
        data?.data?.let {
            return it.parseSysFileManagerUriByDefault(context)
        }
        throw Exception("url为空")
    }
}