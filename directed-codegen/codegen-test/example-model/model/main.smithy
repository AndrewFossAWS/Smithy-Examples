$version: "2.0"
namespace example.weather


/// Provides weather forecasts.
@fakeProtocol
@paginated(inputToken: "nextToken", outputToken: "nextToken", pageSize: "pageSize")
service Weather {
    version: "2006-03-01",
    resources: [City],
    operations: [GetCurrentTime]
}

resource City {
    identifiers: { cityId: CityId },
    read: GetCity,
    list: ListCities,
    resources: [Forecast, CityImage],
    operations: [GetCityAnnouncements]
}

resource Forecast {
    identifiers: { cityId: CityId },
    read: GetForecast,
}

resource CityImage {
    identifiers: { cityId: CityId },
    read: GetCityImage,
}

// "pattern" is a trait.
string CityId

@trait(selector: "structure")
structure forkable {}

@readonly
@http(method: "GET", uri: "/cities/{cityId}")
operation GetCity {
    input: GetCityInput,
    output: GetCityOutput,
    errors: [NoSuchResource]
}

/// The input used to get a city.
@forkable
structure GetCityInput {
    // "cityId" provides the identifier for the resource and
    // has to be marked as required.
    @required
    @httpLabel
    cityId: CityId
}

structure GetCityOutput {
    // "required" is used on output to indicate if the service
    // will always provide a value for the member.
    @required
    name: String,

    @required
    coordinates: CityCoordinates,

    city: CitySummary,
}

// This structure is nested within GetCityOutput.
structure CityCoordinates {
    @required
    @default(0)
    latitude: PrimitiveFloat,

    @required
    longitude: Float,
}

/// Error encountered when no resource could be found.
@error("client")
@httpError(404)
structure NoSuchResource {
    /// The type of resource that was not found.
    @required
    resourceType: String,

    message: String,
}

// The paginated trait indicates that the operation may
// return truncated results.
@readonly
@paginated(items: "items")
@http(method: "GET", uri: "/cities")
operation ListCities {
    input: ListCitiesInput,
    output: ListCitiesOutput,
    errors: [NoSuchResource]
}

structure ListCitiesInput {
    @httpQuery("nextToken")
    nextToken: String,

    @httpQuery("pageSize")
    pageSize: Integer
}

structure ListCitiesOutput {
    @documentation("Yup... Next token")
    nextToken: String,

    @required
    items: CitySummaries,
}

// CitySummaries is a list of CitySummary structures.
list CitySummaries {
    member: CitySummary
}

// CitySummary contains a reference to a City.
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

@readonly
@http(method: "GET", uri: "/current-time")
operation GetCurrentTime {
    output: GetCurrentTimeOutput
}

structure GetCurrentTimeOutput {
    @required
    time: Timestamp
}

@readonly
@http(method: "GET", uri: "/cities/{cityId}/forecast")
operation GetForecast {
    input: GetForecastInput,
    output: GetForecastOutput
}

// "cityId" provides the only identifier for the resource since
// a Forecast doesn't have its own.
structure GetForecastInput {
    @required
    @httpLabel
    cityId: CityId,
}

structure GetForecastOutput {
    chanceOfRain: Float,
    precipitation: Precipitation,
}

union Precipitation {
    rain: PrimitiveBoolean,
    sleet: PrimitiveBoolean,
    hail: StringMap,
    snow: SimpleYesNo,
    mixed: TypedYesNo,
    other: OtherStructure,
    blob: Blob,
    foo: example.weather.nested#Foo,
    baz: example.weather.nested.more#Baz,
}

structure OtherStructure {}

@documentation("I do the documentation thing")
enum SimpleYesNo {
    @documentation("For Sure")
    YES,
    @documentation("No thanks")
    NO
}

intEnum TypedYesNo {
    YES = 1,
    NO = 2
}

map StringMap {
    key: String,
    value: String,
}

@readonly
@http(method: "GET", uri: "/cities/{cityId}/image")
operation GetCityImage {
    input: GetCityImageInput,
    output: GetCityImageOutput,
    errors: [NoSuchResource]
}

structure GetCityImageInput {
    @required @httpLabel
    cityId: CityId,
}

structure GetCityImageOutput {
    @httpPayload
    @required
    image: CityImageData,
}

@streaming
blob CityImageData

@readonly
@http(method: "GET", uri: "/cities/{cityId}/announcements")
operation GetCityAnnouncements {
    input: GetCityAnnouncementsInput,
    output: GetCityAnnouncementsOutput,
    errors: [NoSuchResource]
}


structure GetCityAnnouncementsInput {
    @required
    @httpLabel
    cityId: CityId,
}

structure GetCityAnnouncementsOutput {
    @httpHeader("x-last-updated")
    lastUpdated: Timestamp,

    @httpPayload
    announcements: Announcements
}

@streaming
union Announcements {
    police: Message,
    fire: Message,
    health: Message
}

structure Message {
    message: String,
    author: String
}

// Define a fake protocol trait for use.
@trait
@protocolDefinition
structure fakeProtocol {}