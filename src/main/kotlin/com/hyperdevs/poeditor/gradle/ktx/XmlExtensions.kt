/*
 * Copyright 2023 HyperDevs
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

package com.hyperdevs.poeditor.gradle.ktx

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.util.stream.Collectors
import java.util.stream.IntStream

/**
 * Converts a NodeList to List<Node>.
 */
fun NodeList.asList(): List<Node> =
    IntStream.range(0, length).mapToObj { item(it) }.collect(Collectors.toList())
