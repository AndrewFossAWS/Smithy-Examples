$version: "2.0"

// The validators used here are either built-in linters (see: https://smithy.io/2.0/guides/model-linters.html#linters-in-smithy-linters)
// or they are from the following guide:https://smithy.io/2.0/guides/model-validation-examples.html
metadata validators = [
    {name: "AbbreviationName"},
    {name: "CamelCase"},
    {name: "MissingSensitiveTrait",
     severity: "DANGER",
     configuration: {
         excludeDefaults: false,
         terms: ["spork"]
     }},
    // Enforce a common naming scheme for operation inputs
    {
        name: "EmitEachSelector",
        id: "OperationInputName",
        configuration: {
            messageTemplate: """
            `@{id}` is bound as an input of `@{var|operation|id}` \
            but does not have a name ending with 'Request'.
            """,
            selector: "$operation(*) -[input]-> :not([id|name$=Request])"
        }
    },
    // Enforce a common naming scheme for errors
    {
        name: "EmitEachSelector",
        id: "OperationInputName",
        configuration: {
            messageTemplate: """
            `@{id}` is bound as an error but does not have a name ending with 'Exception'. \
            Perhaps you should rename this shape to `@{id|name}Exception`.
            """,
            selector: "operation -[error]-> :not([id|name$=Exception])"
        }
    },
    // Require integers to have range min and max specified
    {
        name: "EmitEachSelector",
        id: "RawIntegerWithoutRange",
        configuration: {
            messageTemplate: """
        This number shape in member `@{id}` of the operation input `@{var|structure}` \
        does not have a range constraint on both its minimum or maximum value. \
        Add the `@@range` trait to this integer shape and provide both minimum and maximum values. \
        For example, `@@range(min: 1, max: 500)`.
        """,
            selector: """
            operation -[input]-> $structure(*) > member
            :test(> number:not([trait|range|min]):not([trait|range|max]))
            """
        }
    },
    {
        name: "EmitEachSelector",
        id: "RawIntegerWithoutRangeMin",
        configuration: {
            messageTemplate: """
        This number shape in member `@{id}` of the operation input `@{var|structure}` \
        does not have a maximum range constraint. \
        Add a minimum value to the `@@range` trait on this shape. \
        For example, `@@range(>>> min: 1 <<<, max: 500)`.
        """,
            selector: """
            operation -[input]-> $structure(*) > member
            :test(> number[trait|range]:not([trait|range|min]))
            """
        }
    },
    {
        name: "EmitEachSelector",
        id: "RawIntegerWithoutRangeMax",
        configuration: {
            messageTemplate: """
        This number shape in member `@{id}` of the operation input `@{var|structure}` \
        does not have a maximum range constraint. \
        Add a maximum value to the `@@range` trait on this shape. \
        For example, `@@range(min: 1, >>> max: 500 <<<)`.
        """,
            selector: """
            operation -[input]-> $structure(*) > member
            :test(> number[trait|range]:not([trait|range|max]))
            """
        }
    }
]
