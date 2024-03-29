/*
 * Copyright 2023, TeamDev. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

@file:JvmName("Texts")

package io.spine.text

import io.spine.string.Indent
import io.spine.string.Indent.Companion.DEFAULT_JAVA_INDENT_SIZE
import io.spine.string.Separator
import io.spine.string.containsNonSystemLineSeparator
import io.spine.string.pi
import io.spine.string.ti

/**
 * Trims indentation in this text, preserving system line separators.
 */
public fun Text.trimIndent(): Text = text {
    value = this@trimIndent.value.ti()
}

private val DEFAULT_INDENT = Indent(DEFAULT_JAVA_INDENT_SIZE).value

/**
 * Prepends indentation of this text, preserving system line separators.
 */
public fun Text.prependIndent(indent: String = DEFAULT_INDENT): Text = text {
    value = this@prependIndent.value.pi(indent)
}

/**
 * Ensures that the text does not contain non-system line separators.
 *
 * If this text does not contain such separators, the same instance is returned.
 * Otherwise, new instance is created with the required line separators.
 */
public fun Text.ensureSystemLineSeparators(): Text {
    if (!value.containsNonSystemLineSeparator()) {
        return this
    }
    val rejoined = value.lines().joinToString(separator = Separator.nl())
    return text { value = rejoined }
}

