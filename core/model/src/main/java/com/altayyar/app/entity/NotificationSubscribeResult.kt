/* Copyright 2022 Tayyar contributors
 *
 * This file is a part of Tayyar.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Tayyar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Tayyar; if not,
 * see <http://www.gnu.org/licenses>. */

package com.altayyar.app.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationSubscribeResult(
    val id: Int,
    val endpoint: String,
    val alerts: Map<String, Boolean>,
    @Json(name = "server_key") val serverKey: String
)
