/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.integrations.method;

import io.smithy.examples.codegen.CodeGenContext;
import io.smithy.examples.codegen.CodeGenIntegration;
import io.smithy.examples.codegen.writer.CodegenWriter;
import java.util.List;
import software.amazon.smithy.utils.CodeInterceptor;
import software.amazon.smithy.utils.CodeSection;

public class SpecialMethodIntegration implements CodeGenIntegration {
    private static final String INTEGRATION_NAME = "Special-Method-Integration";

    @Override
    public String name() {
        return INTEGRATION_NAME;
    }

    @Override
    public List<? extends CodeInterceptor<? extends CodeSection, CodegenWriter>> interceptors(
        CodeGenContext codegenContext
    ) {
        return List.of(new SpecialMethodInterceptor());
    }
}
