/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.integrations.core;

import io.smithy.examples.codegen.writer.CodegenWriter;
import io.smithy.examples.codegen.writer.sections.MemberSection;
import software.amazon.smithy.model.traits.DocumentationTrait;
import software.amazon.smithy.utils.CodeInterceptor;

final class MemberJavaDocInterceptor implements CodeInterceptor.Prepender<MemberSection, CodegenWriter> {

    @Override
    public Class<MemberSection> sectionType() {
        return MemberSection.class;
    }

    @Override
    public void prepend(CodegenWriter writer, MemberSection enumVariantSection) {
        enumVariantSection.shape().getTrait(DocumentationTrait.class).ifPresent(trait -> {
            writer.writeComment(trait.getValue());
        });
    }
}
