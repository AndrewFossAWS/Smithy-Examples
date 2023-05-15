$version: "2.0"

namespace example.weather

use io.smithy.example.method#specialMethod

apply GetCityInput @specialMethod
apply CitySummary @specialMethod