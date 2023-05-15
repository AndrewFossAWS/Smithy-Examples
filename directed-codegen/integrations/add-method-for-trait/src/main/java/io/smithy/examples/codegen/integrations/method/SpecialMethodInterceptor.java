/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.integrations.method;

import io.smithy.examples.codegen.writer.CodegenWriter;
import io.smithy.examples.codegen.writer.sections.MethodSection;
import software.amazon.smithy.utils.CodeInterceptor;

final class SpecialMethodInterceptor implements CodeInterceptor.Appender<MethodSection, CodegenWriter> {
    private static final String TRAIT_NAME = "io.smithy.example.method#specialMethod";

    @Override
    public void append(CodegenWriter writer, MethodSection methodSection) {
        if (methodSection.shape().hasTrait(TRAIT_NAME)) {
            writer.openBlock("public String specialMethod() {", "}", () -> {
                writer.write("return $S;", "SPECIAL");
            });
        }
    }

    @Override
    public Class<MethodSection> sectionType() {
        return MethodSection.class;
    }
}
