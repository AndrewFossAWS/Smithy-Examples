/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.integrations.retype;

import io.smithy.examples.codegen.CodeGenIntegration;
import io.smithy.examples.codegen.CodeGenSettings;
import java.util.UUID;
import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.codegen.core.SymbolProvider;
import software.amazon.smithy.model.Model;

public final class ReTypeIntegration implements CodeGenIntegration {
    private static final String INTEGRATION_NAME = "Annotation-Integration";
    private static final String TEST_TRAIT_NAME = "io.smithy.example.uuid#uuidTrait";
    private static final Symbol UUID_SYMBOL = Symbol.builder()
        .name(UUID.class.getSimpleName())
        .namespace(UUID.class.getPackageName(), ".")
        .build();

    @Override
    public String name() {
        return INTEGRATION_NAME;
    }

    @Override
    public SymbolProvider decorateSymbolProvider(Model model, CodeGenSettings settings, SymbolProvider symbolProvider) {
        return shape -> {
            if (shape.hasTrait(TEST_TRAIT_NAME)) {
                return UUID_SYMBOL;
            } else if (shape.isMemberShape()) {
                var target = model.expectShape(shape.asMemberShape().get().getTarget());
                if (target.hasTrait(TEST_TRAIT_NAME)) {
                    return UUID_SYMBOL;
                }
            }
            return symbolProvider.toSymbol(shape);
        };
    }
}
