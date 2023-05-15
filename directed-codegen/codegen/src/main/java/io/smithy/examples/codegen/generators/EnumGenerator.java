/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.generators;

import io.smithy.examples.codegen.CodeGenContext;
import io.smithy.examples.codegen.CodeGenSettings;
import java.util.Locale;
import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.codegen.core.directed.GenerateEnumDirective;
import software.amazon.smithy.model.shapes.EnumShape;
import software.amazon.smithy.model.shapes.MemberShape;
import software.amazon.smithy.model.traits.EnumValueTrait;

public class EnumGenerator extends AbstractEnumGenerator<GenerateEnumDirective<CodeGenContext, CodeGenSettings>> {
    private static final String VARIANT_TEMPLATE = "$L($S)";
    private static final Symbol VALUE_TYPE = Symbol.builder()
        .name(String.class.getSimpleName())
        .namespace(String.class.getPackageName(), ".")
        .build();

    @Override
    public void accept(GenerateEnumDirective<CodeGenContext, CodeGenSettings> directive) {
        EnumShape enumShape = (EnumShape) directive.shape();
        directive.context().writerDelegator().useShapeWriter(enumShape, writer -> {
            writeEnum(enumShape, directive.symbolProvider(), writer);
        });
    }

    @Override
    String getVariantTemplate() {
        return VARIANT_TEMPLATE;
    }

    @Override
    Symbol getValueType() {
        return VALUE_TYPE;
    }

    @Override
    Object getEnumValue(MemberShape member) {
        return member.getTrait(EnumValueTrait.class)
            .flatMap(EnumValueTrait::getStringValue)
            .orElseGet(() -> member.getMemberName().toUpperCase(Locale.ENGLISH));
    }
}
