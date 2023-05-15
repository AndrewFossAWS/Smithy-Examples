/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.integrations.core;

import io.smithy.examples.codegen.writer.CodegenWriter;
import io.smithy.examples.codegen.writer.sections.ClassSection;
import software.amazon.smithy.model.traits.DocumentationTrait;
import software.amazon.smithy.utils.CodeInterceptor;

final class ClassJavaDocInterceptor implements CodeInterceptor.Prepender<ClassSection, CodegenWriter> {

    @Override
    public void prepend(CodegenWriter writer, ClassSection classSection) {
        classSection.classShape().getTrait(DocumentationTrait.class).ifPresent(trait -> {
            writer.writeDocs(trait.getValue());
        });
    }

    @Override
    public Class<ClassSection> sectionType() {
        return ClassSection.class;
    }
}
