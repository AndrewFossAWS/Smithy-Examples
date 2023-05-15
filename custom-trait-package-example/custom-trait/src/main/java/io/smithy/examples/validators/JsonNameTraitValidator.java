/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.smithy.examples.validators;

import io.smithy.examples.traits.JsonNameTrait;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.loader.Prelude;
import software.amazon.smithy.model.shapes.Shape;
import software.amazon.smithy.model.validation.AbstractValidator;
import software.amazon.smithy.model.validation.ValidationEvent;

public class JsonNameTraitValidator extends AbstractValidator {
    @Override
    public List<ValidationEvent> validate(Model model) {
        return model.shapes()
            .filter(shape -> !Prelude.isPreludeShape(shape.getId()))
            .filter(shape -> !shape.hasTrait(JsonNameTrait.class))
            .filter(shape -> shape.isMemberShape())
            .map(Shape::asMemberShape)
            .flatMap(Optional::stream)
            .filter(memberShape -> model.expectShape(memberShape.getId())
                .hasTrait("smithy.api#jsonName"))
            .map(shape -> error(shape,
                "This shape cannot apply both the root jsonName trait and the custom jsonName trait!"))
            .collect(Collectors.toList());
    }
}
