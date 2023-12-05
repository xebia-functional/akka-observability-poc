# Akka Service

Akka service description

## Running locally
From the application root folder:
```shell
sbt run
```

## Testing

To run all tests run
```
sbt test
```

To run tests with coverage and scalastyle (great way to validate before a commit)
```
sbt clean coverage test coverageReport explodeCoverage scalastyle
```

To explore the resulting coverage report run
```
open target/scala-2.13/scoverage-report/index.html
```
