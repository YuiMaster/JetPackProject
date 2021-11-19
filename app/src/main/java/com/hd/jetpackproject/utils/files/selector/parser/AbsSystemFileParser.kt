package com.hd.jetpackproject.utils.files.selector.parser

import android.content.Context
import android.content.Intent
import com.hd.jetpackproject.utils.files.selector.EnSystemFileType
import java.io.File


/**
 * @Description: 系统文件解析器
 * @Author: liaoyuhuan
 * @CreateDate: 2021/11/18
 */
abstract class AbsSystemFileParser(val type: EnSystemFileType, var requestCode: Int = type.value) {

    /**
     * 解析[data]数据转为 File
     */
    @Throws(Exception::class)
    abstract fun parse(context: Context, data: Intent?): File
}