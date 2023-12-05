help:
	@echo "'make build'         (builds the project)"
	@echo "'make test'          (runs project tests)"
	@echo "'make publish'       (publishes pre-release artifacts to Nexus)"
	@echo "'make release'       (triggers the release process)"

.PHONY: test

.DEFAULT_GOAL := help

build:
	sbt clean compile
test:
	sbt clean coverage test coverageReport explodeCoverage scalastyle
publish:
	sbt publish rpm:publish universal:packageZipTarball

release:
	sbt 'release cross with-defaults'