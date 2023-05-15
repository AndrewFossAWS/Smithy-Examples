/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.generators;

import io.smithy.examples.codegen.writer.CodegenWriter;
import io.smithy.examples.codegen.writer.sections.ClassSection;
import io.smithy.examples.codegen.writer.sections.CodeHeaderSection;
import io.smithy.examples.codegen.writer.sections.ConstructorSection;
import io.smithy.examples.codegen.writer.sections.MemberSection;
import io.smithy.examples.codegen.writer.sections.MethodSection;
import java.util.function.Consumer;
import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.codegen.core.SymbolProvider;
import software.amazon.smithy.model.shapes.MemberShape;
import software.amazon.smithy.model.shapes.Shape;

abstract class AbstractEnumGenerator<T> implements Consumer<T> {
    private static final String VALUE_FIELD_TEMPLATE = "private final $T value;";

    protected void writeEnum(Shape enumShape, SymbolProvider symbolProvider, CodegenWriter writer) {
        var enumSymbol = symbolProvider.toSymbol(enumShape);
        writer.injectSection(new CodeHeaderSection(enumSymbol));

        writer.pushState(new ClassSection(enumShape))
            .openBlock("public enum $L {", "}", enumSymbol.getName(), () -> {
                writeVariants(enumShape, symbolProvider, writer);

                writer.write("\n");
                writeValueField(writer);
                writer.write("\n");

                writer.pushState(new ConstructorSection());
                writeConstructor(enumSymbol, writer);
                writer.popState();

                writer.pushState(new MethodSection(enumShape));
                writeValueGetter(writer);
                writer.popState();
            })
            .popState();
    }

    abstract String getVariantTemplate();

    abstract Symbol getValueType();

    abstract Object getEnumValue(MemberShape member);

    private void writeVariants(Shape shape, SymbolProvider symbolProvider, CodegenWriter writer) {
        var memberIterator = shape.members().iterator();
        var template = getVariantTemplate();
        while (memberIterator.hasNext()) {
            var member = memberIterator.next();
            var name = symbolProvider.toMemberName(member);
            if (memberIterator.hasNext()) {
                writer.pushState(new MemberSection(member))
                    .write(template + ",", name, getEnumValue(member))
                    .popState();
            } else {
                writer.pushState(new MemberSection(member))
                    .write(template + ";", name, getEnumValue(member))
                    .popState();
            }
        }
    }

    private void writeValueField(CodegenWriter writer) {
        writer.write(VALUE_FIELD_TEMPLATE, getValueType());
    }

    private void writeValueGetter(CodegenWriter writer) {
        writer.openBlock("public $T getValue() {", "}", getValueType(), () -> {
            writer.write("return value;");
        });
    }

    private void writeConstructor(Symbol enumSymbol, CodegenWriter writer) {
        writer.openBlock("public $L($T value) {", "}",
            enumSymbol.getName(), getValueType(), () -> {
                writer.write("this.value = value;");
            }
        );
        writer.write("\n");
    }
}
