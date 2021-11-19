package com.hd.jetpackproject.utils.files.selector


/**
 * @Description: 选择的文件类型
 * @Author: liaoyuhuan
 * @CreateDate: 2021/11/18
 */
enum class EnSystemFileType(val value: Int) {
    /** 视频 */
    VIDEO(101),

    /** 音频 */
    AUDIO(102),

    /** 图片 */
    IMAGE(103),

    /** 其他 */
    OTHER(104),
}