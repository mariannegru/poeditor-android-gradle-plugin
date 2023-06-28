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

package com.hyperdevs.poeditor.gradle.tasks

import com.hyperdevs.poeditor.gradle.PoEditorPluginExtension
import com.hyperdevs.poeditor.gradle.PoEditorStringsUploader
import com.hyperdevs.poeditor.gradle.utils.DEFAULT_PLUGIN_NAME
import com.hyperdevs.poeditor.gradle.utils.POEDITOR_CONFIG_NAME
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

/**
 * Task that:
 * 1. Uploads default language terms to POEditor
 */
abstract class UploadPoEditorStringsTask
@Inject constructor(private val extension: PoEditorPluginExtension) : DefaultTask() {

    /**
     * Main task entrypoint.
     */
    @TaskAction
    @Suppress("ThrowsCount")
    fun uploadPoEditorStrings() {
        val apiToken: String
        val projectId: Int
        val defaultLang: String
        val defaultResPath: String
        val tags: List<String>
        val languageOverridePathMap: Map<String, String>
        val resFileName: String

        try {
            apiToken = extension.apiToken.get()
            projectId = extension.projectId.get()
            defaultLang = extension.defaultLang.get()
            defaultResPath = extension.defaultResPath.get()
            tags = extension.tags.get()
            languageOverridePathMap = extension.languageValuesOverridePathMap.get()
            resFileName = extension.resFileName.get()
        } catch (e: Exception) {
            logger.error("Upload configuration failed", e)

            throw IllegalArgumentException(
                "You don't have the config '${extension.name}' properly set-up in your '$POEDITOR_CONFIG_NAME' block " +
                "or you don't have your main '$DEFAULT_PLUGIN_NAME' config properly set-up.\n" +
                "Please review the input parameters of both blocks and try again.")
        }

        PoEditorStringsUploader.uploadPoEditorStrings(
            apiToken,
            projectId,
            defaultLang,
            defaultLang,
            defaultResPath,
            tags,
            languageOverridePathMap,
            resFileName
        )
    }
}
