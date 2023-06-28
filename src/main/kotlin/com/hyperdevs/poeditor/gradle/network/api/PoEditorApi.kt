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

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * API declaration of PoEditor endpoints used in the app.
 */
interface PoEditorApi {

    /**
     * Returns a list of languages that the current PoEditor project contains.
     */
    @FormUrlEncoded
    @POST("languages/list")
    fun getProjectLanguages(@Field("api_token") apiToken: String,
                            @Field("id") id: Int): Call<PoEditorResponse<ListLanguagesResult>>

    /**
     * Returns the exportables ready to retrieve from the current PoEditor project.
     */
    @Suppress("LongParameterList")
    @FormUrlEncoded
    @JvmSuppressWildcards
    @POST("projects/export")
    fun getExportFileInfo(@Field("api_token") apiToken: String,
                          @Field("id") id: Int,
                          @Field("language") language: String,
                          @Field("type") type: String,
                          @Field("filters") filters: List<String>? = null,
                          @Field("order") order: String? = null,
                          @Field("tags") tags: List<String>? = null,
                          @Field("options") options: String? = null): Call<PoEditorResponse<ExportResult>>

    /**
     * Updates language translations
     */
    @Suppress("LongParameterList")
    @Multipart
    @JvmSuppressWildcards
    @POST("projects/upload")
    fun uploadProjectLanguage(@Part("api_token") apiToken: RequestBody,
                              @Part("id") id: RequestBody,
                              @Part("updating") updating: RequestBody,
                              @Part("language") language: RequestBody,
                              @Part file: MultipartBody.Part,
                              @Part("overwrite") overwrite: RequestBody,
                              @Part("sync_terms") syncTerms: RequestBody,
                              @Part("fuzzy_trigger") fuzzyTrigger: RequestBody,
                              @Part("tags") tags: List<RequestBody>? = null): Call<PoEditorResponse<ProjectUploadResult>>
}
