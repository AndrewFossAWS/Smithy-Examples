/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen;

import io.smithy.examples.codegen.writer.CodegenWriter;
import software.amazon.smithy.codegen.core.SmithyIntegration;

public interface CodeGenIntegration extends SmithyIntegration<CodeGenSettings, CodegenWriter, CodeGenContext> {
}
