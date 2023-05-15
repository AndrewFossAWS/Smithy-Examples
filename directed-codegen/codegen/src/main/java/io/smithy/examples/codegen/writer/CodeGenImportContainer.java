/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.writer;

import java.util.HashSet;
import java.util.Set;
import software.amazon.smithy.codegen.core.ImportContainer;
import software.amazon.smithy.codegen.core.Symbol;

public class CodeGenImportContainer implements ImportContainer {
    Set<Symbol> imports = new HashSet<>();

    @Override
    public void importSymbol(Symbol symbol, String s) {
        // Java doesnt support aliasing.
        importSymbol(symbol);
    }

    @Override
    public void importSymbol(Symbol symbol) {
        if (symbol.getNamespace().startsWith("java.lang")) {
            return;
        }
        imports.add(symbol);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (var importSymbol : imports) {
            result.append("import ");
            result.append(importSymbol.getFullName());
            result.append(";\n");
        }
        result.append("\n");
        return result.toString();
    }
}
