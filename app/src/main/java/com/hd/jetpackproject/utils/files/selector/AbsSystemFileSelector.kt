package com.hd.jetpackproject.utils.files.selector

import android.app.Activity
import android.content.Intent
import com.hd.jetpackproject.utils.LOG
import java.io.File


/**
 * @Description: 系统文件选择器
 * 设计模式：适配器模式
 * @Author: liaoyuhuana
 * @CreateDate: 2021/11/18
 */
abstract class AbsSystemFileSelector(val type: EnSystemFileType, var reqCode: Int = type.value) {
    /**  [errorCallback]回调 */
    protected var errorCallback: ((Exception) -> Unit)? = null

    /** 选择文件
     * [errorCallback]错误回调
     * */
    fun select(activity: Activity, errorCallback: ((Exception) -> Unit)) {
        LOG.d("AbsSystemFileSelector", "select ${type.value}")
        select(activity, reqCode, errorCallback)
    }

    /** 选择文件
     * [requestCode]请求id
     * [errorCallback]错误回调
     * */
    abstract fun select(activity: Activity, requestCode: Int, errorCallback: ((Exception) -> Unit))

    /**
     * 接受选择的数据
     *  @see Activity.onActivityResult
     * [requestCode]请求id
     * [callback]数据回调
     */
    abstract fun handleSystemFile(requestCode: Int, data: Intent?, activity: Activity, callback: (File) -> Unit): Boolean


}