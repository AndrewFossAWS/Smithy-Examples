/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.generators;

import io.smithy.examples.codegen.CodeGenContext;
import io.smithy.examples.codegen.CodeGenSettings;
import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.codegen.core.directed.GenerateIntEnumDirective;
import software.amazon.smithy.model.shapes.MemberShape;
import software.amazon.smithy.model.traits.EnumValueTrait;

public class IntEnumGenerator extends AbstractEnumGenerator<GenerateIntEnumDirective<CodeGenContext, CodeGenSettings>> {
    private static final String VARIANT_TEMPLATE = "$L($L)";
    private static final Symbol VALUE_TYPE = Symbol.builder()
        .name(int.class.getSimpleName())
        .namespace(int.class.getPackageName(), ".")
        .build();

    @Override
    public void accept(GenerateIntEnumDirective<CodeGenContext, CodeGenSettings> directive) {
        var enumShape = directive.shape();
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
            .flatMap(EnumValueTrait::getIntValue)
            .orElseThrow(() -> new RuntimeException("Bad Int Enum shape"));
    }
}
