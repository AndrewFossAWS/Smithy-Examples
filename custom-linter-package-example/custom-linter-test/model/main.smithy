$version: "2"

metadata validators = [
    {
        name: "ForbiddenDocumentation"
        configuration: {
            forbid: ["meow"]
        }
    }
]

namespace example

// This will fail the `ForbiddedDocumentation` validator
@documentation("meow is your time!")
string BadAbbreviationShapeID

