package com.hd.jetpackproject.utils.files.selector.parser

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.hd.jetpackproject.utils.files.selector.EnSystemFileType
import java.io.File
import javax.inject.Inject


/**
 * @Description: 系统文件解析器
 * @Author: liaoyuhuan
 * @CreateDate: 2021/11/18
 */
class SystemImageFileParser @Inject constructor() : AbsSystemFileParser(EnSystemFileType.IMAGE) {

    /**
     * 解析[data]数据转为 File
     */
    override fun parse(context: Context, data: Intent?): File {
        data?.data?.let {
            return context.handleSystemFileImage(data)
        }
        throw Exception("url为空")
    }

    @Throws(Exception::class)
    private fun Context.handleSystemFileImage(data: Intent): File {
        val path = handleImageOnKitKat(data)
        return if (path.isNullOrEmpty()) {
            throw Exception("文件解析失败")
        } else {
            File(path)
        }
    }

    @Throws(Exception::class)
    private fun Context.handleImageOnKitKat(data: Intent): String? {
        var imagePath: String? = null
        val uri: Uri = data.data ?: return null

        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果document类型的Uri，则通过document id处理
            val docId: String = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri.authority) {
                val id = docId.split(":").toTypedArray()[1]
                // 解析出数字格式的id
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri: Uri =
                    ContentUris.withAppendedId(Uri.parse("content://" + "downloads//public_downloads"), java.lang.Long.valueOf(docId))
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // 如果是普通content类型的uri，则使用普通的方法处理
            imagePath = getImagePath(uri, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            // 如果使用file类型的uri，直接获取图片的路径即可
            imagePath = uri.path
        }
        return imagePath
    }


    @Throws(Exception::class)
    private fun Context.getImagePath(externalContentUri: Uri, selection: String?): String? {
        var path: String? = null
        // 通过Uri和selection来获取真实的图片路径
        val cursor: Cursor? = contentResolver.query(externalContentUri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                if (index == -1) {
                    return null
                } else {
                    path = cursor.getString(index)
                }
            }
            cursor.close()
        }
        return path
    }

}