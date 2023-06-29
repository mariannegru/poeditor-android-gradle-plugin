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

package com.hyperdevs.poeditor.gradle.network

import com.google.gson.Gson
import com.hyperdevs.poeditor.gradle.network.api.*
import com.hyperdevs.poeditor.gradle.utils.logger
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

/**
 * Basic interface used to implement API calls to the PoEditor service.
 */
interface PoEditorApiController {
    /**
     * Retrieves the available languages for a given project.
     */
    fun getProjectLanguages(projectId: Int): List<ProjectLanguage>

    /**
     * Retrieves the translation file URL for a given project, language code, and export type.
     * Also supports a series of options.
     */
    @Suppress("LongParameterList")
    fun getTranslationFileUrl(projectId: Int,
                              code: String,
                              type: ExportType,
                              filters: List<FilterType>?,
                              order: OrderType,
                              tags: List<String>?,
                              unquoted: Boolean): String

    /**
     * Updates the default language translations from project to backend.
     */
    @Suppress("LongParameterList")
    fun uploadProjectLanguage(projectId: Int,
                              code: String,
                              updating: UpdatingType,
                              file: File,
                              overwrite: Boolean,
                              syncTerms: Boolean,
                              fuzzyTrigger: Boolean,
                              tags: List<String>?): String

    /**
     * Get a list of terms
     */
    @Suppress("LongParameterList")
    fun getTerms(projectId: Int): List<Term>

    /**
     * Upsert a list of terms
     */
    @Suppress("LongParameterList")
    fun upsertTerms(projectId: Int, fuzzyTrigger: Boolean, terms: List<Term>): String

    /**
     * Delete a list of terms
     */
    @Suppress("LongParameterList")
    fun deleteTerms(projectId: Int, terms: List<Term>): String
}

/**
 * Implementation of [PoEditorApiController] using Retrofit.
 */
class PoEditorApiControllerImpl(private val apiToken: String,
                                private val moshi: Moshi,
                                private val poEditorApi: PoEditorApi) : PoEditorApiController {

    private val optionsAdapter: JsonAdapter<List<Options>> =
        moshi.adapter(Types.newParameterizedType(List::class.java, Options::class.java))

    override fun getProjectLanguages(projectId: Int): List<ProjectLanguage> {
        val response = poEditorApi.getProjectLanguages(
            apiToken = apiToken,
            id = projectId).execute()
        return response.onSuccessful { it.languages }
    }

    @Suppress("LongParameterList")
    override fun getTranslationFileUrl(projectId: Int,
                                       code: String,
                                       type: ExportType,
                                       filters: List<FilterType>?,
                                       order: OrderType,
                                       tags: List<String>?,
                                       unquoted: Boolean): String {
        val options = listOf(
            Options(unquoted = if (unquoted) 1 else 0)
        ).let {
            optionsAdapter.toJson(it)
        }

        val response = poEditorApi.getExportFileInfo(
            apiToken = apiToken,
            id = projectId,
            type = type.toString().toLowerCase(),
            filters = filters?.map { it.name.toLowerCase() },
            language = code,
            order = order.name.toLowerCase(),
            tags = tags,
            options = options
        ).execute()

        return response.onSuccessful { it.url }
    }

    override fun uploadProjectLanguage(projectId: Int,
                                       code: String,
                                       updating: UpdatingType,
                                       file: File,
                                       overwrite: Boolean,
                                       syncTerms: Boolean,
                                       fuzzyTrigger: Boolean,
                                       tags: List<String>?): String {
        val response = poEditorApi.uploadProjectLanguage(
            apiToken = apiToken.toRequestBody(),
            id = projectId.toString().toRequestBody(),
            language = code.toRequestBody(),
            updating = updating.toString().toLowerCase().toRequestBody(),
            overwrite = (if(overwrite) 1 else 0).toString().toRequestBody(),
            syncTerms = (if(syncTerms) 1 else 0).toString().toRequestBody(),
            fuzzyTrigger = (if(fuzzyTrigger) 1 else 0).toString().toRequestBody(),
            file = MultipartBody.Part.createFormData("file", "strings.xml", file.asRequestBody("text/xml".toMediaTypeOrNull())),
            tags = tags?.map { it.toRequestBody() }
        ).execute()

        return response.onSuccessful { it.toString() }
    }

    override fun getTerms(projectId: Int): List<Term> {
        val response = poEditorApi.getTerms(
            apiToken = apiToken,
            id = projectId
        ).execute()

        return response.onSuccessful { it.terms ?: emptyList() }
    }

    override fun upsertTerms(projectId: Int, fuzzyTrigger: Boolean, terms: List<Term>): String {
        val data = Gson().toJson(terms)
        logger.lifecycle("Updating: $data")
        val response = poEditorApi.upsertTerms(
            apiToken = apiToken,
            id = projectId,
            fuzzyTrigger = if (fuzzyTrigger) 1 else 0,
            data = data
        ).execute()

        return response.onSuccessful { it.terms.toString() }
    }

    override fun deleteTerms(projectId: Int, terms: List<Term>): String {
        val data = Gson().toJson(terms)
        logger.lifecycle("Deliting: $data")
        val response = poEditorApi.deleteTerms(
            apiToken = apiToken,
            id = projectId,
            data = data
        ).execute()

        return response.onSuccessful { it.terms.toString() }
    }

    private inline fun <T, U : PoEditorResponse<T>, V> Response<U>.onSuccessful(func: (T) -> V): V {
        if (isSuccessful && body()?.response?.status == ResponseStatus.STATUS_SUCCESS) {
            body()?.let { return func(it.result!!) }
        }

        throw IllegalStateException(
            "An error occurred while trying to retrieve data from PoEditor API: \n" +
            body()?.response?.let { "Error code: ${it.code}, error message: ${it.message}" })
    }
}
