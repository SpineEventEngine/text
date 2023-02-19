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
import io.spine.string.Separator;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.spine.text.Position.BeyondText.NOT_IN_TEXT;
import static io.spine.util.Exceptions.newIllegalArgumentException;

/**
 * Static factories and precondition checks for creating instances of {@link Text}.
 *
 * @apiNote A recommended way for using this class is using its methods statically
 *         imported, so that the creation of {@link Text} objects looks compact:
 *         <pre>{@code
 *         import io.spine.text.TextFactory.text
 *         ...
 *         var twoLines = text("one", "two");
 *         }</pre>
 */
public final class TextFactory {

    private static final String NL = Separator.INSTANCE.getNL();
    private static final Splitter SPLITTER = Splitter.on(NL);
    private static final Joiner JOINER = Joiner.on(NL);

    @VisibleForTesting
    public static final Position NOT_FOUND = Position.newBuilder()
            .setBeyondText(NOT_IN_TEXT)
            .build();

    /**
     * Prevents instantiation of this static factory class.
     */
    private TextFactory() {
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
     * Creates a new instance of text with lines separated by {@linkplain #newLine()
     * line separator}.
     *
     * @throws IllegalArgumentException
     *         if one of the lines
     */
    public static Text text(Iterable<String> lines) {
        checkNotNull(lines);
        checkNoSeparators(lines);
        var joined = JOINER.join(lines);
        return text(joined);
    }

    /**
     * Creates a new multi-line text with the given lines.
     *
     * @throws IllegalArgumentException
     *         if any of the lines contains a
     *         {@linkplain #containsSeparator(CharSequence) line separator}
     */
    @VisibleForTesting
    public static Text createText(String... lines) {
        checkNotNull(lines);
        return text(ImmutableList.copyOf(lines));
    }

    /**
     * Ensures that lines do not contain
     * {@linkplain #containsSeparator(CharSequence) line separators}.
     *
     * @throws IllegalArgumentException
     *         if at least one line contains a
     *         {@linkplain #containsSeparator(CharSequence) line separator}
     */
    public static void checkNoSeparators(Iterable<String> lines) {
        lines.forEach(TextFactory::checkNoSeparator);
    }

    /**
     * Ensures that charter sequence does not contain a
     * {@linkplain #containsSeparator(CharSequence) line separator}.
     *
     * @throws IllegalArgumentException
     *         if the sequence contains a
     *         {@linkplain #containsSeparator(CharSequence) line separator}
     */
    public static void checkNoSeparator(CharSequence line) {
        if (containsSeparator(line)) {
            throw newIllegalArgumentException("The line contains line separator: `%s`.", line);
        }
    }

    /**
     * Tells if the charter sequence contains any of the {@linkplain io.spine.string.Separator
     * line separators}.
     */
    public static boolean containsSeparator(CharSequence s) {
        var str = s.toString();
        return str.contains(NL)
                || str.contains(Separator.CR)
                || str.contains(Separator.CRLF);
    }

    /**
     * Obtains the instance of the joiner on {@linkplain #newLine() line separator}.
     */
    public static Joiner lineJoiner() {
        return JOINER;
    }

    /**
     * Obtains the splitter that breaks the text on lines at {@linkplain #newLine()
     * line separators}.
     */
    public static Splitter lineSplitter() {
        return SPLITTER;
    }

    /**
     * Obtains the instance of {@link Position} which means "not found".
     */
    public static Position positionNotFound() {
        return NOT_FOUND;
    }

    /**
     * Obtains line separator used in the operating system.
     *
     * @apiNote Use this method for brevity of code related to working with lines.
     */
    public static String newLine() {
        return NL;
    }
}
