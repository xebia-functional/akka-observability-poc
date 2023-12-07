package com.xebia.useractorpoc

import akka.actor.typed.ActorSystem
import akka.pattern.StatusReply


object UserApp extends App {


  val system: ActorSystem[UserActor.Command] = ActorSystem(UserActor("1"), "UserActorPoC")
  val ref = system.deadLetters[StatusReply[UserActor.Summary]]

  println("entro 33333")
  system ! UserActor.Add("1", ref)

  println("ahora?")
//  println("entro 33333")
//  system ! UserActor.Add("2")
//  println("entro 33333")
//  system ! UserActor.Add("3")
//  println("entro 33333")
//  system ! UserActor.Add("4")


}