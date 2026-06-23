package com.altayyar.app.interfaces

interface HashtagActionListener {
    fun unfollow(tagName: String, position: Int)
    fun viewTag(tagName: String)
    fun copyTagName(tagName: String)
}
