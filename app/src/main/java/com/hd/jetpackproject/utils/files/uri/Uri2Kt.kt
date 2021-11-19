package com.hd.jetpackproject.utils.files.uri

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.hd.jetpackproject.utils.LOG

/**
 * description: 从Uri获取文件绝对路径，以前的方法已不好使
 * author: liaoyuhuan
 * createDate: 2019/12/16 0016
 */

fun Uri.uriToFilePath(context: Context): String? {
    // DocumentProvider
    if (DocumentsContract.isDocumentUri(context, this)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument()) {
            val docId: String = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument()) {
            val id: String = DocumentsContract.getDocumentId(this)
            val contentUri: Uri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
            )
            return contentUri.getDataColumn(context, null, null)
        } else if (isMediaDocument()) {
            val docId: String = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])
            return contentUri?.getDataColumn(context, selection, selectionArgs)
        }
    } else if ("content".equals(scheme, ignoreCase = true)) {
        return this.getDataColumn(context, null, null)
    } else if ("file".equals(scheme, ignoreCase = true)) {
        return path
    }
    return null
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param uri           The Uri to query.
 * @param selection     (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 */
private fun Uri.getDataColumn(context: Context, selection: String?, selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(this, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndexOrThrow(column))
        }
    } catch (e: Exception) {
        LOG.d("FileSelectUtil", "" + e)
    } finally {
        cursor?.close()
    }
    return null
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun Uri.isExternalStorageDocument(): Boolean {
    return "com.android.externalstorage.documents" == authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun Uri.isDownloadsDocument(): Boolean {
    return "com.android.providers.downloads.documents" == authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
fun Uri.isMediaDocument(): Boolean {
    return "com.android.providers.media.documents" == authority
}