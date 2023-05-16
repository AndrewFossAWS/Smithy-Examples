smithyVersion = 1.31.0

clean:
	rm -rf service-using-abstract-projection/build/


build: clean
	(cd service-using-abstract-projection; smithy build -c abstract-projection.json -c smithy-build.json models)
