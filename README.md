# PoC: Akka Telemetry vs OpenTelemetry

Run Kafka:

```bash
docker-compose up
```

Run the producer:

```bash
sbt "runMain com.xebia.prodpoc.ProdPOC"
```

Run the consumer:

```bash
sbt "runMain com.xebia.conspoc.ConsPOC"
```

