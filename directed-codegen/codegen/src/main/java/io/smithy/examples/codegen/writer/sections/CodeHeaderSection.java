/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.writer.sections;

import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.utils.CodeSection;

public record CodeHeaderSection(Symbol symbol) implements CodeSection {
}
