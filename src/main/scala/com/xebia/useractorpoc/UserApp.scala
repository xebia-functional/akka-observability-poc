package com.xebia.useractorpoc

import akka.actor.typed.ActorSystem
import akka.pattern.StatusReply


object UserApp extends App {

  val system: ActorSystem[UserActor.Command] = ActorSystem(UserActor("1"), "UserActorPoC")
  val ref = system.deadLetters[StatusReply[UserActor.Summary]]

  system ! UserActor.Add("1", ref)
}