/*
 * Copyright 2024 HyperDevs
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

package com.hyperdevs.poeditor.gradle.utils

import com.hyperdevs.poeditor.gradle.ConfigName
import org.gradle.api.Project
import java.io.File

/**
 * Gets the resource directory for a given config name.
 */
fun getResourceDirectory(project: Project, configName: ConfigName) =
    File(File(File(project.layout.projectDirectory.asFile, "src"), configName), "res")
