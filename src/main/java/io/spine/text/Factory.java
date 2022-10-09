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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.spine.util.Exceptions.newIllegalArgumentException;
import static java.lang.System.lineSeparator;

/**
 * Static utilities for creating instances of {@link Text}.
 */
public final class Factory {

    static final Splitter SPLITTER = Splitter.on(lineSeparator());
    static final Joiner JOINER = Joiner.on(lineSeparator());

    private Factory() {
    }

    /**
     * Creates a new instance with the given value.
     */
    public static Text text(String value) {
        checkNotNull(value);
        return Text.newBuilder()
                .setValue(value)
                .build();
    }

    /**
     * Creates a new instance of text with lines separated by {@linkplain System#lineSeparator()
     * line separator}.
     */
    public static Text text(Iterable<String> lines) {
        checkNotNull(lines);
        checkNoSeparators(lines);
        var joined = JOINER.join(lines);
        return text(joined);
    }

    /**
     * Creates a new instance of text with lines separated by {@linkplain System#lineSeparator()
     * line separator}.
     */
    public static Text text(String[] lines) {
        checkNotNull(lines);
        return text(ImmutableList.copyOf(lines));
    }

    /**
     * Creates a new list with the given lines.
     *
     * @throws IllegalArgumentException
     *          if any of the lines contains the {@linkplain System#lineSeparator()
     *          line separator}
     */
    @VisibleForTesting
    public static Text createText(String... lines) {
        checkNotNull(lines);
        return text(lines);
    }

    static void checkNoSeparators(Iterable<String> lines) {
        lines.forEach(Factory::checkNoSeparator);
    }

    static void checkNoSeparator(CharSequence s) {
        if (containsSeparator(s)) {
            throw newIllegalArgumentException("The line contains line separator: `%s`.", s);
        }
    }

    static boolean containsSeparator(CharSequence s) {
        return s.toString().contains(lineSeparator());
    }
}
