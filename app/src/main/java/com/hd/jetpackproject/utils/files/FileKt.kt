package com.hd.jetpackproject.utils.files

import android.os.Build
import java.io.File
import java.nio.file.Files


fun File?.safeDelete(): Boolean {
    try {
        if (this != null && exists()) {
            return deleteFileByNio()
        }
    } catch (e: java.lang.Exception) {
        // 文件权限未授予
    }
    return false
}

private fun File?.deleteFileByNio(): Boolean {
    if (this == null) {
        return true
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            Files.delete(this.toPath())
        } catch (e: java.lang.Exception) {
            return false
        }
    } else {
        return delete()
    }
    return true
}

fun File?.safeMkdirs() {
    if (this == null) {
        return
    }
    if (!exists()) {
        mkdirs()
    }
}


enum class TxtEncode {
    GB2312, UTF_16, Unicoce, Utf_8;
}
