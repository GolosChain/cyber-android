/**
 * Copyright 2013-present memtrip LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.memtrip.eos.chain.actions.query.producer.bpjson

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BpSocial(
    val steemit: String?,
    val twitter: String?,
    val youtube: String?,
    val facebook: String?,
    val github: String?,
    val reddit: String?,
    val telegram: String?,
    val wechat: String?
)