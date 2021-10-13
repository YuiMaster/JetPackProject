package com.hd.jetpackproject.data

class UnsplashSearchResponse {
    var data: List<DataDTO>? = null
    var errorCode: Int = 0
    var errorMsg: String? = null

    class DataDTO {
        var children: List<*>? = null
        var courseId: Int? = null
        var id: Int? = null
        var name: String? = null
        var order: Int? = null
        var parentChapterId: Int? = null
        var userControlSetTop: Boolean? = null
        var visible: Int? = null
    }
}