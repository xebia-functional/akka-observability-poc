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

Run the UserActor app:

```bash
sbt "runMain com.xebia.useractorpoc.UserApp"
```

And see the logs:

```bash
[INFO] [com.xebia.useractorpoc.UserActor$] - Got command Add(1,Actor[akka://UserActorPoC/deadLetters#0])
[INFO] [com.xebia.useractorpoc.UserActor$] - Handling event Added(1) 
```

Run the UserActor tests:

```bash
sbt "testOnly com.xebia.useractorpoc.UserActorSpec" 
```
