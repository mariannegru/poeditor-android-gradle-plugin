/*
 * Copyright 2021 HyperDevs
 *
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

package com.hyperdevs.poeditor.gradle.network.api

import java.lang.IllegalArgumentException
import java.util.Date

/**
 * Basic PoEditor response. Contains common fields for all responses in the API.
 */
open class PoEditorResponse<T : Any>(open val response: ResponseStatus, open val result: T?)

/**
 * Basic response data.
 */
data class ResponseStatus(val status: String,
                          val code: String,
                          val message: String) {
    companion object {
        const val STATUS_SUCCESS = "success"
        const val STATUS_FAIL = "fail"
    }
}

/**
 * Result of a "list language" call.
 */
data class ListLanguagesResult(val languages: List<ProjectLanguage>)

/**
 * Result of a "list language" call.
 */
data class ExportResult(val url: String)

/**
 * Information about a language in PoEditor.
 */
data class ProjectLanguage(val name: String,
                           val code: String,
                           val translations: Int,
                           val percentage: Double,
                           val updated: Date?)

/**
 * Types of file export allowed in PoEditor.
 */
enum class ExportType {
    PO,
    POT,
    MO,
    XLS,
    XLSX,
    CSV,
    INI,
    RESW,
    RESX,
    ANDROID_STRINGS,
    APPLE_STRINGS,
    XLIFF,
    PROPERTIES,
    KEY_VALUE_JSON,
    JSON,
    YML,
    XLF,
    XMB,
    XTB,
    ARB,
    RISE_360_XLIFF;

    companion object {
        /** Returns the enum value associated to a string value. */
        fun from(value: String) =
            try {
                valueOf(value.toUpperCase())
            } catch (e: Exception) {
                val message = "Value \"$value\" is not a valid ${this::class.java.declaringClass.simpleName}; " +
                              "allowed values are: ${values().joinToString(", ") { "\"${it.name.toLowerCase()}\"" }}"
                throw IllegalArgumentException(message)
            }
    }
}

/**
 * Filter types to use in file exports.
 */
enum class FilterType {
    TRANSLATED,
    UNTRANSLATED,
    FUZZY,
    NOT_FUZZY,
    AUTOMATIC,
    NOT_AUTOMATIC,
    PROOFREAD,
    NOT_PROOFREAD;

    companion object {
        /** Returns the enum value associated to a string value. */
        fun from(value: String) =
            try {
                valueOf(value.toUpperCase())
            } catch (e: Exception) {
                val message = "Value \"$value\" is not a valid ${this::class.java.declaringClass.simpleName}; " +
                              "allowed values are: ${values().joinToString(", ") { "\"${it.name.toLowerCase()}\"" }}"
                throw IllegalArgumentException(message)
            }
    }
}

/**
 * Order types to use in file exports.
 */
enum class OrderType {
    NONE,
    TERMS;

    companion object {
        /** Returns the enum value associated to a string value. */
        fun from(value: String) =
            try {
                valueOf(value.toUpperCase())
            } catch (e: Exception) {
                val message = "Value \"$value\" is not a valid ${this::class.java.declaringClass.simpleName}; " +
                              "allowed values are: ${values().joinToString(", ") { "\"${it.name.toLowerCase()}\"" }}"
                throw IllegalArgumentException(message)
            }
    }
}

/**
 * Object passed as export options.
 */
data class Options(val unquoted: Int) {
    init {
        require(unquoted in 0..1) { "unquoted value must be 0 or 1" }
    }
}

/**
 * Project upload updating type.
 */
enum class UpdatingType {
    TERMS,
    TERMS_TRANSLATIONS,
    TRANSLATIONS;

    companion object {
        /** Returns the enum value associated to a string value. */
        fun from(value: String) =
            try {
                UpdatingType.valueOf(value.toUpperCase())
            } catch (e: Exception) {
                val message = "Value \"$value\" is not a valid ${this::class.java.declaringClass.simpleName}; " +
                              "allowed values are: ${UpdatingType.values().joinToString(", ") { "\"${it.name.toLowerCase()}\"" }}"
                throw IllegalArgumentException(message)
            }
    }
}

/**
 * Project upload terms response
 */
data class ProjectUploadTerms(
    val parsed: Int,
    val added: Int,
    val deleted: Int
)

/**
 * Project upload translations response
 */
data class ProjectUploadTranslations(
    val parsed: Int,
    val added: Int,
    val updated: Int
)
/**
 * Project upload result
 */
data class ProjectUploadResult(
    val terms: ProjectUploadTerms?,
    val translations: ProjectUploadTranslations
)
