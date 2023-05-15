/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.codegen;

import software.amazon.smithy.model.node.ObjectNode;
import software.amazon.smithy.model.shapes.ShapeId;

public record CodeGenSettings(ShapeId service, String packageName, String packageVersion) {
    public static CodeGenSettings from(ObjectNode pluginSettings) {
        return new CodeGenSettings(
            pluginSettings.expectStringMember("service").expectShapeId(),
            pluginSettings.expectStringMember("package").getValue(),
            pluginSettings.expectStringMember("packageVersion").getValue()
        );
    }
}
