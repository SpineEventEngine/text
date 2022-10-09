/*
 * Copyright 2022, TeamDev. All rights reserved.
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

package io.spine.text;

import io.spine.annotation.GeneratedMixin;

import java.util.List;

import static io.spine.text.Factory.SPLITTER;
import static io.spine.text.Factory.checkNoSeparator;

/**
 * Mixin interface for the {@code Text} data type.
 */
@GeneratedMixin
public interface TextMixin extends TextOrBuilder {

    /**
     * Obtains a read-only list of lines of this text.
     */
    default List<String> lines() {
        return SPLITTER.splitToList(getValue());
    }

    /**
     * Tells if this text is empty.
     */
    default boolean isEmpty() {
        return getValue().isEmpty();
    }

    /**
     * Obtains the size of the text in characters, including line separators.
     */
    default int size() {
        return getValue().length();
    }

    /**
     * Tells if this text contains the given sequence.
     */
    default boolean contains(CharSequence sequence) {
        checkNoSeparator(sequence);
        return getValue().contains(sequence);
    }
}
