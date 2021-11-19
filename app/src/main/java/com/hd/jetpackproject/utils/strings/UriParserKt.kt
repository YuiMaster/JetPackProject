package com.hd.jetpackproject.utils.strings

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files

/**
 * description: 外部音频导入 Uri
 * author: liaoyuhuan
 * createDate: 2020/8/24 0024
 */

/**
 * 获取真实路径
 *
 * @param context
 */
@Throws(SecurityException::class)
fun Context.getFileFromUri(uri: Uri?): String? {
    return if (uri == null) {
        null
    } else when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT ->                 //Android7.0之后的uri content:// URI
            getFilePathFromContentUri(uri)
        ContentResolver.SCHEME_FILE ->                 //Android7.0之前的uri file://
            File(uri.path).absolutePath
        else -> File(uri.path).absolutePath
    }
}

/**
 * 从uri获取path
 *
 * @param uri content://media/external/file/109009
 *
 *
 * FileProvider适配
 * content://com.tencent.mobileqq.fileprovider/external_files/storage/emulated/0/Tencent/QQfile_recv/
 * content://com.tencent.mm.external.fileprovider/external/tencent/MicroMsg/Download/
 */
@Throws(SecurityException::class)
private fun Context.getFilePathFromContentUri(uri: Uri?): String? {
    if (null == uri) return null
    var data: String? = null
    val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME)
    val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
    if (null != cursor) {
        if (cursor.moveToFirst()) {
            val index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
            data = if (index > -1) {
                cursor.getString(index)
            } else {
                val nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                val fileName = cursor.getString(nameIndex)
                getPathFromInputStreamUri(uri, fileName)
            }
        }
        cursor.close()
    }
    return data
}

/**
 * 用流拷贝文件一份到自己APP私有目录下
 * @param uri
 * @param fileName
 */
private fun Context.getPathFromInputStreamUri(uri: Uri, fileName: String): String? {
    var filePath: String? = null
    if (uri.authority != null) {
        val contentResolver = contentResolver
        if (contentResolver != null) {
            try {
                contentResolver.openInputStream(uri).use { inputStream ->
                    val file = createTemporalFileFrom(inputStream, fileName)
                    if (file != null) {
                        filePath = file.path
                    }
                }
            } catch (e: Exception) {
                // do nothing
            }
        }
    }
    return filePath
}

private fun Context.createTemporalFileFrom(inputStream: InputStream?, fileName: String): File? {
    var targetFile: File? = null
    if (inputStream != null) {
        var read: Int
        val buffer = ByteArray(8 * 1024)
        // 自己定义拷贝文件路径
        targetFile = File(externalCacheDir, fileName)
        if (targetFile.exists()) {
            targetFile.deleteFileByNio()
        }
        try {
            FileOutputStream(targetFile).use { outputStream ->
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
            }
        } catch (e: Exception) {
            // do nothing
        }
    }
    return targetFile
}


/**
 * Path就是取代File的
 * 使用java最小nio文件库删除文件
 */
fun File.deleteFileByNio(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            Files.delete(toPath())
        } catch (e: java.lang.Exception) {
            return false
        }
    } else {
        return delete()
    }
    return true
}