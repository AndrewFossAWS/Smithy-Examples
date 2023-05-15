# Demo Codegen Project

### What does this project do
- Generation of *Enum*, *IntEnum*, *Structure* and *Error Structure* shapes
- Handling of simple (i.e. non-third-party) imports
- Use of `CodeSection`s for triggering integrations
- Use of Symbol decoration


## What does this project not do
- Does not Generate Operation, Union, Resource, Or service code
- Does not generate 3rd party dependencies
- Does not generate a working client or server, just shape files
- Every class file in this project is created at the same level in a `models/` directory. In a real generator the
  class files should be able to generate in nested packages based on the relative model namespaces.
- (TODO) Does not handle field initialization
- Does not have adders/putters for lists and maps (i.e. `add(item)`, `put(item)`, `addAll(items)`)

---
### Example Generated Structure
*Smithy Model*
```smithy
@trait(selector: "string")
structure uuidTrait {}

// "pattern" is a trait.
@uuidTrait
string CityId

// CitySummary contains a reference to a City.
@references([{resource: City}])

@trait(selector: "structure")
structure forkable {}

@references([{resource: City}])
@forkable
structure CitySummary {
@required
cityId: CityId,

@required
name: String,

number: BigDecimal,
yesNo: SimpleYesNo,
}
```

*Generated Java Code*
```java
/*
 * Copyright 2022 example.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.example.weather;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;
import com.example.weather.CitySummary;
import java.util.UUID;
import com.example.weather.models.SimpleYesNo;

@Generated("io.smithy.examples.codegen.codegen.SmithyCodegenPlugin")
public final class CitySummary {
    private UUID cityId;
    private String name;
    private BigDecimal number;
    private SimpleYesNo yesNo;


    public CitySummary(UUID cityId,
                       String name,
                       BigDecimal number,
                       SimpleYesNo yesNo
    ){
        this.cityId = cityId;
        this.name = name;
        this.number = number;
        this.yesNo = yesNo;
    }


    public String fork() {
        return "FORK";
    }
    public UUID getCityId() {
        return cityId;
    }


    public String getName() {
        return name;
    }


    public BigDecimal getNumber() {
        return number;
    }


    public SimpleYesNo getYesNo() {
        return yesNo;
    }


    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setNumber(BigDecimal number) {
        this.number = number;
    }


    public void setYesNo(SimpleYesNo yesNo) {
        this.yesNo = yesNo;
    }
}
```

### Example Generated Enum
*Smithy Model*
```smithy
@documentation("A simple enum representing the answer to a yes/no question")
enum SimpleYesNo {
    @documentation("For Sure")
    YES,
    @documentation("No thanks")
    NO
}
```

*Generated Java Code*
```java
/*
 * Copyright 2022 example.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.example.weather.models;

import javax.annotation.processing.Generated;

/**
 * A simple enum representing the answer to a yes/no question
 */
@Generated("io.smithy.examples.codegen.SmithyCodegenPlugin")
public enum SimpleYesNo {
    // For Sure
    YES("YES"),
    // No thanks
    NO("NO");
    
    
    private final String value;
    
    
    public SimpleYesNo(String value) {
        this.value = value;
    }
    
    
    public String getValue() {
        return value;
    }
}
```