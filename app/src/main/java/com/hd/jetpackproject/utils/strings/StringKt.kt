package com.hd.jetpackproject.utils.strings


fun String?.toNotNull(): String {
    return this ?: ""
}