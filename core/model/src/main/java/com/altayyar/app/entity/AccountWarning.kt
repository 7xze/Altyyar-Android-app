package com.altayyar.app.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountWarning(
    val id: String,
    val action: Action
) {
    @JsonClass(generateAdapter = false)
    enum class Action {
        @Json(name = "none")
        NONE,

        @Json(name = "disable")
        DISABLE,

        @Json(name = "mark_statuses_as_sensitive")
        MARK_STATUSES_AS_SENSITIVE,

        @Json(name = "delete_statuses")
        DELETE_STATUSES,

        @Json(name = "sensitive")
        SENSITIVE,

        @Json(name = "silence")
        SILENCE,

        @Json(name = "suspend")
        SUSPEND,
    }
}
