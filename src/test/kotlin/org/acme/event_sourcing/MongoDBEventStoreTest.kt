package org.acme.event_sourcing

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isNotEmpty
import java.util.*
import javax.inject.Inject

@QuarkusTest
class MongoDBEventStoreTest {

    @Inject
    lateinit var eventStore: MongoDBEventStore

    @Test
    fun `writing events to mongodb should not result in an error`() {
        eventStore.save(UUID.randomUUID(), listOf(TestEvent("someValue")))
            .await()
            .indefinitely()
    }

    @Test
    fun `it should read saved events`() {
        val aggregateId = UUID.randomUUID()
        eventStore.save(aggregateId, listOf(TestEvent("foo"))).await().indefinitely()

        val events = eventStore.loadEvents(aggregateId).collect().asList().await().indefinitely()

        expectThat(events) {
            isNotEmpty()
        }
        expectThat(events.first()) {
            isA<TestEvent>()
        }
    }

}