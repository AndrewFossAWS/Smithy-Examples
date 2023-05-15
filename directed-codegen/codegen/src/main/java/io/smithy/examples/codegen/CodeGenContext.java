/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen;

import io.smithy.examples.codegen.writer.CodegenWriter;
import java.util.List;
import software.amazon.smithy.build.FileManifest;
import software.amazon.smithy.codegen.core.CodegenContext;
import software.amazon.smithy.codegen.core.SymbolProvider;
import software.amazon.smithy.codegen.core.WriterDelegator;
import software.amazon.smithy.codegen.core.directed.CreateContextDirective;
import software.amazon.smithy.model.Model;

public final class CodeGenContext implements CodegenContext<CodeGenSettings, CodegenWriter, CodeGenIntegration> {

    private final Model model;
    private final CodeGenSettings settings;
    private final SymbolProvider symbolProvider;
    private final FileManifest fileManifest;
    private final WriterDelegator<CodegenWriter> writerDelegator;
    private final List<CodeGenIntegration> integrations;

    public CodeGenContext(Model model,
                          CodeGenSettings settings,
                          SymbolProvider symbolProvider,
                          FileManifest fileManifest,
                          List<CodeGenIntegration> integrations) {
        this.model = model;
        this.settings = settings;
        this.symbolProvider = symbolProvider;
        this.fileManifest = fileManifest;
        this.writerDelegator = new WriterDelegator<>(fileManifest, symbolProvider, new CodegenWriter.Factory());
        this.integrations = integrations;
    }

    public static CodeGenContext fromContextDirective(
        CreateContextDirective<CodeGenSettings, CodeGenIntegration> createContextDirective
    ) {
        return new CodeGenContext(
            createContextDirective.model(),
            createContextDirective.settings(),
            createContextDirective.symbolProvider(),
            createContextDirective.fileManifest(),
            createContextDirective.integrations()
        );
    }

    @Override
    public Model model() {
        return model;
    }

    @Override
    public CodeGenSettings settings() {
        return settings;
    }

    @Override
    public SymbolProvider symbolProvider() {
        return symbolProvider;
    }

    @Override
    public FileManifest fileManifest() {
        return fileManifest;
    }

    @Override
    public WriterDelegator<CodegenWriter> writerDelegator() {
        return writerDelegator;
    }

    @Override
    public List<CodeGenIntegration> integrations() {
        return integrations;
    }
}
