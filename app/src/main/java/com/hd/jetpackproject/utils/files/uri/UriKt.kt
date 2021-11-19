package com.hd.jetpackproject.utils.files.uri

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.hd.jetpackproject.utils.ByteConst
import com.hd.jetpackproject.utils.LOG
import com.hd.jetpackproject.utils.files.safeDelete
import com.hd.jetpackproject.utils.files.safeMkdirs
import com.hd.jetpackproject.utils.strings.getFileFromUri
import com.hd.jetpackproject.utils.strings.toNotNull
import java.io.File
import java.io.FileOutputStream


/**
 * 取得Uri的文件
 */
fun Uri?.toFile(context: Context): DocumentFile? {
    if (this == null) return null
    return DocumentFile.fromSingleUri(context.applicationContext, this)
}

/**
 * 取得文件的名字
 */
fun Uri?.getFileName(context: Context): String? {
    if (this == null) return null
    val documentFile = toFile(context) ?: return null
    return documentFile.name
}


/**
 * 取得Uri的文件大小
 */
fun Uri?.getFileLength(context: Context): Long {
    if (this == null) return 0
    val file = toFile(context)
    return file?.length() ?: 0
}


/**
 * 主要用于适配Android 11
 * copy uri文件到指定文件夹
 * [destFolder]文件夹
 */
@Throws(Exception::class)
fun Uri.copyFileFromUri(context: Context, destFolder: File): File {
    val fileName = getFileName(context)
    LOG.d("UriKt", "getFileRealNameFromUri $fileName")
    val targetFile = File(destFolder, fileName)
    targetFile.safeDelete()
    destFolder.safeMkdirs()
    context.applicationContext.contentResolver.openInputStream(this).use {
        try {
            FileOutputStream(targetFile).use { outStream ->
                val buffer = ByteArray(8 * 1024)
                var bytesRead: Int
                while (it!!.read(buffer).also { bytesRead = it } != -1) {
                    outStream.write(buffer, 0, bytesRead)
                }
            }
        } catch (e: Exception) {
            LOG.e("UriKt", "copyFileFromUri ${e.message}")
            throw e
        }
    }
    LOG.d("UriKt", " copyFileFromUri ${targetFile.absolutePath}")
    return targetFile
}


/**
 * 默认File解析，通过File解析失败使用 contentResolver
 */
@Throws(Exception::class)
fun Uri.parseSysFileManagerUriByDefault(context: Context): File {
    val file1 = File(context.getFileFromUri(this).toNotNull())
    val file2 = File(path.toNotNull())
    LOG.d("UriKt", "parseSysFileManagerUriByDefault uri.encodedPath:${encodedPath} uri.encodedPath:${lastPathSegment}")
    return when {
        file1.exists() -> {
            file1
        }
        file2.exists() -> {
            file2
        }
        else -> {
            parseSysFileManagerUriByUri(context)
        }
    }
}

/**
 * File无法解析，需要借助 contentResolver
 * 影响：会在cache路径生缓存文件
 */
@Throws(Exception::class)
private fun Uri.parseSysFileManagerUriByUri(context: Context): File {
    LOG.d("UriKt", "parseSysFileManagerUriByUri  ")
    if (getFileName(context).isNullOrEmpty()) {
        throw Exception("解析uri失败")
    }
    if (getFileLength(context) > 10 * ByteConst.MB) {
        throw Exception("最多支持10M")
    }
    return copyFileFromUri(context, context.cacheDir)
}
