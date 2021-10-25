package org.acme.event_sourcing

import com.mongodb.client.model.Filters.eq
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MongoDBEventStore(
    client: ReactiveMongoClient
) {

    private val collection = client
        .getDatabase("test")
        .getCollection("event_store", MongoEvent::class.java)

    fun save(aggregateId: UUID, events: List<Event>): Uni<Void> {
        return collection.insertMany(events.map { MongoEvent(aggregateId = aggregateId, event = it) })
            .onItem()
            .ignore().andContinueWithNull()
    }

    fun loadEvents(aggregateId: UUID): Multi<Event> {
        return collection.find(eq("aggregateId", aggregateId))
            .map { it.event }
    }
}