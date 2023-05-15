/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen;

import io.smithy.examples.codegen.generators.EnumGenerator;
import io.smithy.examples.codegen.generators.IntEnumGenerator;
import io.smithy.examples.codegen.generators.StructureGenerator;
import software.amazon.smithy.codegen.core.SymbolProvider;
import software.amazon.smithy.codegen.core.directed.CreateContextDirective;
import software.amazon.smithy.codegen.core.directed.CreateSymbolProviderDirective;
import software.amazon.smithy.codegen.core.directed.DirectedCodegen;
import software.amazon.smithy.codegen.core.directed.GenerateEnumDirective;
import software.amazon.smithy.codegen.core.directed.GenerateErrorDirective;
import software.amazon.smithy.codegen.core.directed.GenerateIntEnumDirective;
import software.amazon.smithy.codegen.core.directed.GenerateServiceDirective;
import software.amazon.smithy.codegen.core.directed.GenerateStructureDirective;
import software.amazon.smithy.codegen.core.directed.GenerateUnionDirective;


public class CodeGenDirectedCodeGen implements DirectedCodegen<CodeGenContext, CodeGenSettings, CodeGenIntegration> {

    @Override
    public SymbolProvider createSymbolProvider(
        CreateSymbolProviderDirective<CodeGenSettings> createSymbolProviderDirective
    ) {
        return CodeGenSymbolProvider.fromDirective(createSymbolProviderDirective);
    }

    @Override
    public CodeGenContext createContext(CreateContextDirective<CodeGenSettings,
        CodeGenIntegration> createContextDirective
    ) {
        return CodeGenContext.fromContextDirective(createContextDirective);
    }

    @Override
    public void generateService(GenerateServiceDirective<CodeGenContext, CodeGenSettings> generateServiceDirective) {
        // This demo plugin does not generate a service/client
    }

    @Override
    public void generateStructure(
        GenerateStructureDirective<CodeGenContext, CodeGenSettings> generateStructureDirective
    ) {
        new StructureGenerator<GenerateStructureDirective<CodeGenContext, CodeGenSettings>>()
            .accept(generateStructureDirective);
    }

    @Override
    public void generateError(GenerateErrorDirective<CodeGenContext, CodeGenSettings> generateErrorDirective) {
        new StructureGenerator<GenerateErrorDirective<CodeGenContext, CodeGenSettings>>()
            .accept(generateErrorDirective);
    }

    @Override
    public void generateUnion(GenerateUnionDirective<CodeGenContext, CodeGenSettings> generateUnionDirective) {
        // Unions are not currently supported by this generator
    }

    @Override
    public void generateEnumShape(GenerateEnumDirective<CodeGenContext, CodeGenSettings> generateEnumDirective) {
        new EnumGenerator().accept(generateEnumDirective);
    }

    @Override
    public void generateIntEnumShape(
        GenerateIntEnumDirective<CodeGenContext, CodeGenSettings> generateIntEnumDirective
    ) {
        new IntEnumGenerator().accept(generateIntEnumDirective);
    }
}
