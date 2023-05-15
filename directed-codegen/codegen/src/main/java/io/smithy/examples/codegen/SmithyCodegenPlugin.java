/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen;

import io.smithy.examples.codegen.writer.CodegenWriter;
import software.amazon.smithy.build.PluginContext;
import software.amazon.smithy.build.SmithyBuildPlugin;
import software.amazon.smithy.codegen.core.directed.CodegenDirector;

public class SmithyCodegenPlugin implements SmithyBuildPlugin {
    private final CodegenDirector<CodegenWriter, CodeGenIntegration, CodeGenContext, CodeGenSettings> runner
        = new CodegenDirector<>();

    @Override
    public String getName() {
        return "demo-codegen";
    }

    @Override
    public void execute(PluginContext pluginContext) {
        System.out.println("Lets generate some code!");

        CodeGenSettings codeGenSettings = CodeGenSettings.from(pluginContext.getSettings());

        runner.directedCodegen(new CodeGenDirectedCodeGen());
        runner.integrationClass(CodeGenIntegration.class);
        runner.fileManifest(pluginContext.getFileManifest());
        runner.model(pluginContext.getModel());
        runner.settings(codeGenSettings);
        runner.service(codeGenSettings.service());
        runner.performDefaultCodegenTransforms();
        runner.createDedicatedInputsAndOutputs();
        runner.run();

        System.out.println("Code has been generated!");
    }
}
