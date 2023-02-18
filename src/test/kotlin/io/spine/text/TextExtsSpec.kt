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

package io.spine.text

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.spine.string.Separator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Extension functions for `Text` should")
internal class TextExtsSpec {

    private lateinit var text: Text

    @BeforeEach
    fun createText() {
        val str = """
            1
            2
        """.withNonSystemSeparators()

        text = text { value = str }
    }

    @Test
    fun `trim indentations preserving system line separators`() {
        text.trimIndent().value.containsNonSystemSeparators() shouldBe false
    }

    @Test
    fun `prepend indentation preserving line separators`() {
        text.prependIndent().value.containsNonSystemSeparators() shouldBe false
    }

    @Test
    fun `ensure system line separators`() {
        text.ensureSystemLineSeparators().value.containsNonSystemSeparators() shouldBe false
    }

    @Test
    fun `return the same instance if separators are system`() {
        val goodSeparators = text.ensureSystemLineSeparators().value

        val withGoodSeparators = text { value = goodSeparators }

        withGoodSeparators.ensureSystemLineSeparators() shouldBeSameInstanceAs withGoodSeparators
    }
}

private fun String.withNonSystemSeparators(): String {
    return replace(Separator.NL, nonSystemSeparators().last())
}