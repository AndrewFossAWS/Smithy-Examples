/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen.writer;

import java.util.function.BiFunction;
import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.codegen.core.SymbolWriter;
import software.amazon.smithy.utils.StringUtils;

public class CodegenWriter extends SymbolWriter<CodegenWriter, CodeGenImportContainer> {
    private static final int MAX_LINE_LENGTH = 80;
    private static final String PACKAGE_TEMPLATE = "package %s;%n";

    private final String packageName;

    public CodegenWriter(String packageName) {
        super(new CodeGenImportContainer());
        this.packageName = packageName;
        putFormatter('T', new JavaSymbolFormatter());
    }

    /**
     * Writes documentation strings from a string.
     *
     * @param docs Documentation to write.
     */
    public void writeDocs(String docs) {
        pushState();
        write("/**");
        writeInline(" * ");
        write(formatDocs(docs));
        write(" */");
        popState();
    }

    public String formatDocs(String docs) {
        return StringUtils.wrap(docs, MAX_LINE_LENGTH - 8, "\n *", false);
    }

    /**
     * Writes documentation comments from a string.
     *
     * @param comment comment to write.
     */
    public void writeComment(String comment) {
        pushState();
        writeInline("// ");
        write(formatComment(comment));
        popState();
    }

    public String formatComment(String comment) {
        return StringUtils.wrap(comment, MAX_LINE_LENGTH - 8, "\n//", false);
    }

    @Override
    public String toString() {
        return getAttribution()
            + "\n"
            + getPackageString()
            + "\n"
            + getImportContainer().toString()
            + super.toString();
    }

    private String getPackageString() {
        return String.format(PACKAGE_TEMPLATE, packageName);
    }

    private String getAttribution() {
        return """
            /*
             * Copyright 2022 example.com, Inc. or its affiliates. All Rights Reserved.
             */
            """;
    }

    public static final class Factory implements SymbolWriter.Factory<CodegenWriter> {
        @Override
        public CodegenWriter apply(String file, String nameSpace) {
            return new CodegenWriter(nameSpace);
        }
    }

    /**
     * Implements Java symbol formatting for the {@code $T} formatter.
     */
    private final class JavaSymbolFormatter implements BiFunction<Object, String, String> {
        private static final String JAVA_STD_LIB_NAMESPACE = "java.lang";

        @Override
        public String apply(Object type, String indent) {
            if (!(type instanceof Symbol typeSymbol)) {
                throw new RuntimeException("Invalid type provided to $T. Expected a Symbol, but found `" + type + "`");
            }

            addImport(typeSymbol, typeSymbol.getName());
            if (typeSymbol.getReferences().isEmpty()) {
                return typeSymbol.getName();
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append(typeSymbol.getName());
                builder.append("<");
                var iterator = typeSymbol.getReferences().iterator();
                while (iterator.hasNext()) {
                    Symbol refSymbol = iterator.next().getSymbol();
                    addImport(refSymbol, refSymbol.getName());
                    builder.append(refSymbol.getName());
                    if (iterator.hasNext()) {
                        builder.append(",");
                    }
                }
                builder.append(">");
                return builder.toString();
            }
        }
    }

}
