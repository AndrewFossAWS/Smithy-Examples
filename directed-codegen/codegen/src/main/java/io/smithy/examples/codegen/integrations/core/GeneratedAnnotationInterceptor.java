/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.integrations.core;

import io.smithy.examples.codegen.SmithyCodegenPlugin;
import io.smithy.examples.codegen.writer.CodegenWriter;
import io.smithy.examples.codegen.writer.sections.ClassSection;
import javax.annotation.processing.Generated;
import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.utils.CodeInterceptor;

final class GeneratedAnnotationInterceptor implements CodeInterceptor.Prepender<ClassSection, CodegenWriter> {
    private static final String CODE_GENERATOR_NAME = SmithyCodegenPlugin.class.getName();
    private final Symbol generatedAnnotationSymbol = Symbol.builder()
        .name(Generated.class.getSimpleName())
        .namespace(Generated.class.getPackageName(), ".")
        .build();

    @Override
    public Class<ClassSection> sectionType() {
        return ClassSection.class;
    }

    @Override
    public void prepend(CodegenWriter writer, ClassSection section) {
        writer.addImport(generatedAnnotationSymbol, generatedAnnotationSymbol.getName());
        writer.write("@$T($S)", generatedAnnotationSymbol, CODE_GENERATOR_NAME);
    }
}
