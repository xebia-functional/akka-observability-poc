package com.xebia.prodpoc

import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, NewTopic}

import java.util.Properties
import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


class TopicCreator(servers: String, topic: String, ec: ExecutionContext) {

  def running = {
    val props = new Properties()
    props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, servers)

    val adminClient = AdminClient.create(props)
    val numPartitions = 3
    val replicationFactor: Short = 3 // Set replication factor as per your requirement

    val newTopic = new NewTopic(topic, numPartitions, replicationFactor)

    // Asynchronously create a topic
    val createTopicResult = adminClient.createTopics(List(newTopic).asJava)

    // Handle the result
    Future {
      createTopicResult.all().get()
    }(ec).onComplete {
      case Success(_) =>
        println(s"Topic $topic created successfully")
        adminClient.close()
      case Failure(exception) =>
        println(s"Error creating topic: ${exception.getMessage}")
        adminClient.close()
    }(ec)



  }

}
