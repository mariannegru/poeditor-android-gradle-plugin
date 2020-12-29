/*
 * Copyright 2020 BQ
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

package com.bq.poeditor.gradle.ktx

private val UNESCAPED_HTML_TAGS_REGEX = Regex("""&lt;([^.]*?)&gt;""")

/**
 * Unescapes HTML tags from string.
 */
fun String.unescapeHtmlTags() = this.replace(UNESCAPED_HTML_TAGS_REGEX, "<$1>")
