package com.xebia.useractorpoc

import akka.pattern.StatusReply
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.scalatest.wordspec.AnyWordSpecLike
import java.util.UUID

class UserActorSpec extends ScalaTestWithActorTestKit(s"""
      akka.persistence.journal.plugin = "akka.persistence.journal.inmem"
      akka.persistence.snapshot-store.plugin = "akka.persistence.snapshot-store.local"
      akka.persistence.snapshot-store.local.dir = "target/snapshot-${UUID.randomUUID().toString}"
    """) with AnyWordSpecLike {

  private var counter = 1
  def newUserId(): String = {
    counter += 1
    s"$counter"
  }

  "User Actor" should {

    "add item" in {
      val userId: String = newUserId()
      val user = testKit.spawn(UserActor(userId))
      val probe = testKit.createTestProbe[StatusReply[UserActor.Summary]]
      user ! UserActor.Add(userId, probe.ref)
      probe.expectMessage(StatusReply.Success(UserActor.Summary(1)))
    }

    "add item twice" in {
      val userId: String = newUserId()
      val user = testKit.spawn(UserActor(userId))
      val probe = testKit.createTestProbe[StatusReply[UserActor.Summary]]
      user ! UserActor.Add(userId, probe.ref)
      probe.expectMessage(StatusReply.Success(UserActor.Summary(1)))
      user ! UserActor.Add(userId, probe.ref)
      probe.expectMessage(StatusReply.Success(UserActor.Summary(2)))
    }

    "get count" in {
      val userId: String = newUserId()
      val user = testKit.spawn(UserActor(userId))
      val probe = testKit.createTestProbe[StatusReply[UserActor.Summary]]
      user ! UserActor.Add(userId, probe.ref)
      probe.expectMessage(StatusReply.Success(UserActor.Summary(1)))
      val probe2 = testKit.createTestProbe[UserActor.Summary]
      user ! UserActor.Get(probe2.ref)
      probe2.expectMessage(UserActor.Summary(1))
    }

    "get count and clean" in {
      val userId: String = newUserId()
      val user = testKit.spawn(UserActor(userId))
      val probe = testKit.createTestProbe[StatusReply[UserActor.Summary]]
      val probe2 = testKit.createTestProbe[UserActor.Summary]
      user ! UserActor.Add(userId, probe.ref)
      probe.expectMessage(StatusReply.Success(UserActor.Summary(1)))
      user ! UserActor.Clear
      probe.expectNoMessage()
      user ! UserActor.Get(probe2.ref)
      probe2.expectMessage(UserActor.Summary(0))
    }

    "keep its state" in {
      val userId = newUserId()
      val user = testKit.spawn(UserActor(userId))
      val probe = testKit.createTestProbe[StatusReply[UserActor.Summary]]
      user ! UserActor.Add(userId, probe.ref)
      probe.expectMessage(StatusReply.Success(UserActor.Summary(1)))

      testKit.stop(user)

      // start again with same userId
      val restartedUser = testKit.spawn(UserActor(userId))
      val stateProbe = testKit.createTestProbe[UserActor.Summary]
      restartedUser ! UserActor.Get(stateProbe.ref)
      stateProbe.expectMessage(UserActor.Summary(1))
    }
  }

}
