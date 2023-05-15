/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.generators;

import io.smithy.examples.codegen.CodeGenContext;
import io.smithy.examples.codegen.CodeGenSettings;
import io.smithy.examples.codegen.writer.CodegenWriter;
import io.smithy.examples.codegen.writer.sections.ClassSection;
import io.smithy.examples.codegen.writer.sections.CodeHeaderSection;
import io.smithy.examples.codegen.writer.sections.ConstructorSection;
import io.smithy.examples.codegen.writer.sections.MemberSection;
import io.smithy.examples.codegen.writer.sections.MethodSection;
import io.smithy.examples.codegen.writer.sections.PropertiesSection;
import java.util.function.Consumer;
import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.codegen.core.SymbolProvider;
import software.amazon.smithy.codegen.core.directed.ShapeDirective;
import software.amazon.smithy.model.shapes.StructureShape;
import software.amazon.smithy.utils.StringUtils;

public class StructureGenerator<T extends ShapeDirective<StructureShape, CodeGenContext, CodeGenSettings>>
    implements Consumer<T> {
    private static final String BASE_CLASS_TEMPLATE_STRING = "public final class $L ";
    private static final String PROPERTY_TEMPLATE = "private $T $L;";

    @Override
    public void accept(T directive) {
        var structureShape = directive.shape();
        var structureSymbol = directive.symbol();

        directive.context().writerDelegator().useShapeWriter(structureShape, writer -> {
            writer.injectSection(new CodeHeaderSection(structureSymbol));
            writer.pushState(new ClassSection(structureShape))
                .openBlock(getTemplateString(structureSymbol), "}", structureSymbol.getName(), () -> {
                    writer.pushState(new PropertiesSection());
                    writeProperties(structureShape, directive.symbolProvider(), writer);
                    writer.popState();

                    writer.pushState(new ConstructorSection());
                    writeConstructor(structureShape, directive.symbolProvider(), writer);
                    writer.popState();

                    writer.pushState(new MethodSection(structureShape));
                    writeGetters(structureShape, directive.symbolProvider(), writer);
                    writeSetters(structureShape, directive.symbolProvider(), writer);
                    writer.popState();
                })
                .popState();
        });
    }

    private void writeProperties(StructureShape shape, SymbolProvider symbolProvider, CodegenWriter writer) {
        for (var member : shape.members()) {
            var memberName = member.getMemberName();
            var memberType = symbolProvider.toSymbol(member);
            writer.pushState(new MemberSection(member))
                .write(PROPERTY_TEMPLATE, memberType, memberName)
                .popState();
        }
        writer.write("\n");
    }

    private void writeConstructor(StructureShape shape, SymbolProvider symbolProvider, CodegenWriter writer) {
        var structureSymbol = symbolProvider.toSymbol(shape);
        writer.disableNewlines();
        writer.openBlock("public $T(", "){", structureSymbol, () -> {
            var iterator = shape.members().iterator();
            while (iterator.hasNext()) {
                var member = iterator.next();
                var template = "$T $L";
                if (iterator.hasNext()) {
                    template = template + ",";
                }
                writer.write(template + "\n", symbolProvider.toSymbol(member), member.getMemberName());
            }
            writer.write("\n");
        });
        writer.enableNewlines();
        writer.openBlock("", "}", () -> {
            for (var member : shape.members()) {
                writer.write("this.$1L = $1L;", member.getMemberName());
            }
        });
    }

    private String getTemplateString(Symbol symbol) {
        if (symbol.getProperty("extends").isPresent()) {
            return BASE_CLASS_TEMPLATE_STRING
                + "extends "
                + ((Symbol) symbol.getProperty("extends").get()).getName()
                + "{";
        } else {
            return BASE_CLASS_TEMPLATE_STRING + "{";
        }
    }

    private void writeGetters(StructureShape shape, SymbolProvider symbolProvider, CodegenWriter writer) {
        for (var memberEntry : shape.members()) {
            var name = memberEntry.getMemberName();
            writer.openBlock("public $T get$L() {", "}",
                symbolProvider.toSymbol(memberEntry), StringUtils.capitalize(name),
                () -> writer.write("return $L;", name)
            );
            writer.write("\n");
        }
    }

    private void writeSetters(StructureShape shape, SymbolProvider symbolProvider, CodegenWriter writer) {
        for (var memberEntry : shape.members()) {
            var name = memberEntry.getMemberName();
            writer.openBlock("public void set$L($T $L) {", "}",
                StringUtils.capitalize(name), symbolProvider.toSymbol(memberEntry), name,
                () -> writer.write("this.$1L = $1L;", name)
            );
            writer.write("\n");
        }
    }
}
