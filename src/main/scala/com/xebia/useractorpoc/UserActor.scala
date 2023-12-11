package com.xebia.useractorpoc

import akka.actor.typed.{ActorRef, ActorSystem, Behavior, SupervisorStrategy}
import akka.actor.typed.scaladsl._
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior, RetentionCriteria}
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.cluster.sharding.typed.scaladsl.Entity
import akka.cluster.sharding.typed.scaladsl.EntityTypeKey

import scala.concurrent.duration._
import akka.pattern.StatusReply


object UserActor {

  val TypeKey: EntityTypeKey[Command] =
    EntityTypeKey[Command]("UserActor")

  def initSharding(system: ActorSystem[_]): ActorRef[ShardingEnvelope[Command]] =
    ClusterSharding(system).init(Entity(TypeKey) { entityContext =>
      UserActor(entityContext.entityId)
    })

  sealed trait Command
  final case class Add(number: String, replyTo: ActorRef[StatusReply[Summary]]) extends Command
  final case class Get(replyTo: ActorRef[Summary]) extends Command
  final case class Summary(count: Int) extends Command
  case object Clear extends Command

  sealed trait Event
  final case class Added(number: String) extends Event
  case object Cleared extends Event

  case class State(history: List[String] = Nil) {
    def toSummary: Summary = Summary(history.size)
  }

  def genericCommandHandler(context: ActorContext[Command], commandHandler: (State, Command) => Effect[Event, State]): (State, Command) => Effect[Event, State] =
    (state, cmd) => {
      context.log.info("Got command {}", cmd)
      commandHandler(state, cmd)
    }

  def genericEventHandler(context: ActorContext[Command], eventHandler: (State, Event) => State): (State, Event) => State =
    (state, event) => {
      context.log.info("Handling event {}", event)
      eventHandler(state, event)
    }

  val myCommandHandler: (State, Command) => Effect[Event, State] = { (state, command) =>
    command match {
      case Add(number, replyTo) =>
        Effect
          .persist(Added(number))
          .thenRun(userState => replyTo ! StatusReply.Success(userState.toSummary))
      case Get(replyTo) => {
        replyTo ! state.toSummary
        Effect.none
      }
      case Clear => Effect.persist(Cleared)
      case Summary(_) => Effect.none
    }
  }


    val myEventHandler: (State, Event) => State = { (state, event) =>
      event match {
        case Added(number) => State(history = number :: state.history)
        case Cleared => State(Nil)
      }
    }

    def apply(userId: String): Behavior[Command] =
      Behaviors.setup { context =>
        EventSourcedBehavior[Command, Event, State](
          persistenceId = PersistenceId.ofUniqueId(userId),
          emptyState = State(Nil),
          commandHandler = genericCommandHandler(context, myCommandHandler),
          eventHandler = genericEventHandler(context, myEventHandler))
          .withRetention(RetentionCriteria.snapshotEvery(numberOfEvents = 100, keepNSnapshots = 3))
          .onPersistFailure(SupervisorStrategy.restartWithBackoff(200.millis, 5.seconds, 0.1))
      }
  }