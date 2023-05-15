/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen;

import static java.lang.String.format;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.codegen.core.SymbolProvider;
import software.amazon.smithy.codegen.core.directed.CreateSymbolProviderDirective;
import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.BigDecimalShape;
import software.amazon.smithy.model.shapes.BigIntegerShape;
import software.amazon.smithy.model.shapes.BlobShape;
import software.amazon.smithy.model.shapes.BooleanShape;
import software.amazon.smithy.model.shapes.ByteShape;
import software.amazon.smithy.model.shapes.DocumentShape;
import software.amazon.smithy.model.shapes.DoubleShape;
import software.amazon.smithy.model.shapes.EnumShape;
import software.amazon.smithy.model.shapes.FloatShape;
import software.amazon.smithy.model.shapes.IntEnumShape;
import software.amazon.smithy.model.shapes.IntegerShape;
import software.amazon.smithy.model.shapes.ListShape;
import software.amazon.smithy.model.shapes.LongShape;
import software.amazon.smithy.model.shapes.MapShape;
import software.amazon.smithy.model.shapes.MemberShape;
import software.amazon.smithy.model.shapes.OperationShape;
import software.amazon.smithy.model.shapes.ResourceShape;
import software.amazon.smithy.model.shapes.ServiceShape;
import software.amazon.smithy.model.shapes.Shape;
import software.amazon.smithy.model.shapes.ShapeVisitor;
import software.amazon.smithy.model.shapes.ShortShape;
import software.amazon.smithy.model.shapes.StringShape;
import software.amazon.smithy.model.shapes.StructureShape;
import software.amazon.smithy.model.shapes.TimestampShape;
import software.amazon.smithy.model.shapes.UnionShape;
import software.amazon.smithy.model.traits.ErrorTrait;
import software.amazon.smithy.utils.StringUtils;

public final class CodeGenSymbolProvider implements SymbolProvider, ShapeVisitor<Symbol> {
    private final CodeGenSettings settings;
    private final Model model;
    private final ServiceShape service;

    private CodeGenSymbolProvider(Model model, CodeGenSettings settings) {
        this.settings = settings;
        this.model = model;
        this.service = model.expectShape(settings.service(), ServiceShape.class);
    }

    public static CodeGenSymbolProvider fromDirective(
        CreateSymbolProviderDirective<CodeGenSettings> createSymbolProviderDirective
    ) {
        return new CodeGenSymbolProvider(createSymbolProviderDirective.model(),
            createSymbolProviderDirective.settings());
    }

    private static String getDefaultShapeName(Shape shape, ServiceShape service) {
        // Use the service-aliased name
        return StringUtils.capitalize(shape.getId().getName(service));
    }

    private static Symbol.Builder createSymbolBuilder(Shape shape, String typeName, String namespace) {
        return createSymbolBuilder(shape, typeName).namespace(namespace, ".");
    }

    private static Symbol.Builder createSymbolBuilder(Shape shape, String typeName) {
        return Symbol.builder().putProperty("shape", shape).name(typeName);
    }

    private static Symbol fromClass(Class<?> clazz) {
        return Symbol.builder()
            .name(clazz.getSimpleName())
            .namespace(clazz.getPackageName(), ".")
            .build();
    }

    @Override
    public Symbol toSymbol(Shape shape) {
        return shape.accept(this);
    }

    @Override
    public Symbol blobShape(BlobShape blobShape) {
        return fromClass(ByteBuffer.class);
    }

    @Override
    public Symbol booleanShape(BooleanShape booleanShape) {
        return fromClass(Boolean.class);
    }

    @Override
    public Symbol listShape(ListShape listShape) {
        return fromClass(List.class).toBuilder()
            .addReference(listShape.getMember().accept(this))
            .build();
    }

    @Override
    public Symbol mapShape(MapShape mapShape) {
        return fromClass(Map.class).toBuilder()
            .addReference(mapShape.getKey().accept(this))
            .addReference(mapShape.getValue().accept(this))
            .build();
    }

    @Override
    public Symbol byteShape(ByteShape byteShape) {
        return fromClass(Byte.class);
    }

    @Override
    public Symbol shortShape(ShortShape shortShape) {
        return fromClass(Short.class);
    }

    @Override
    public Symbol integerShape(IntegerShape integerShape) {
        return fromClass(Integer.class);
    }

    @Override
    public Symbol longShape(LongShape longShape) {
        return fromClass(Long.class);
    }

    @Override
    public Symbol floatShape(FloatShape floatShape) {
        return fromClass(Float.class);
    }

    @Override
    public Symbol documentShape(DocumentShape documentShape) {
        return null;
    }

    @Override
    public Symbol doubleShape(DoubleShape doubleShape) {
        return fromClass(Double.class);
    }

    @Override
    public Symbol bigIntegerShape(BigIntegerShape bigIntegerShape) {
        return fromClass(BigInteger.class);
    }

    @Override
    public Symbol bigDecimalShape(BigDecimalShape bigDecimalShape) {
        return fromClass(BigDecimal.class);
    }

    @Override
    public Symbol operationShape(OperationShape operationShape) {
        return Symbol.builder()
            .name(getDefaultShapeName(operationShape, service))
            .namespace(settings.packageName(), ".")
            .build();
    }

    @Override
    public Symbol resourceShape(ResourceShape resourceShape) {
        return null;
    }

    @Override
    public Symbol serviceShape(ServiceShape serviceShape) {
        return null;
    }

    @Override
    public Symbol stringShape(StringShape stringShape) {
        return fromClass(String.class);
    }

    @Override
    public Symbol structureShape(StructureShape structureShape) {
        var name = getDefaultShapeName(structureShape, service);
        var builder = Symbol.builder()
            .name(name)
            .namespace(settings.packageName(), ".")
            .definitionFile(format("./%s/models/%s.java", settings.packageName(), name));
        if (structureShape.hasTrait(ErrorTrait.class)) {
            builder.putProperty("extends", fromClass(RuntimeException.class));
        }
        return builder.build();
    }

    @Override
    public Symbol unionShape(UnionShape unionShape) {
        String name = getDefaultShapeName(unionShape, service);
        return createSymbolBuilder(unionShape, name, format("%s.models", settings.packageName()))
            .definitionFile(format("./%s/models/%s.java", settings.packageName(), name))
            .build();
    }

    @Override
    public Symbol memberShape(MemberShape memberShape) {
        return toSymbol(model.expectShape(memberShape.getTarget()));
    }

    @Override
    public Symbol timestampShape(TimestampShape timestampShape) {
        return fromClass(Date.class);
    }

    @Override
    public Symbol enumShape(EnumShape shape) {
        String name = getDefaultShapeName(shape, service);
        return createSymbolBuilder(shape, name, format("%s.models", settings.packageName()))
            .putProperty("enumValueType", fromClass(String.class))
            .definitionFile(format("./%s/models/%s.java", settings.packageName(), name))
            .build();
    }

    @Override
    public Symbol intEnumShape(IntEnumShape shape) {
        String name = getDefaultShapeName(shape, service);
        return createSymbolBuilder(shape, name, format("%s.models", settings.packageName()))
            .putProperty("enumValueType", fromClass(int.class))
            .definitionFile(format("./%s/models/%s.java", settings.packageName(), name))
            .build();
    }
}
