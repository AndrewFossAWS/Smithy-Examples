smithyVersion = 1.31.0

testFile = testing/expected-spec.openapi.json
outputFile = weather-service/build/smithy/source/openapi/Weather.openapi.json

clean:
	rm -rf weather-service/build/


build: clean
	(cd weather-service; smithy build models)

test: build
	diff -b $(testFile) $(outputFile)