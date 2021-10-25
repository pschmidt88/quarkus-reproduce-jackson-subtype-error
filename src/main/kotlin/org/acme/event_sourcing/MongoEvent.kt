package org.acme.event_sourcing

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import java.util.*

class MongoEvent @BsonCreator constructor(
    @BsonId val id: ObjectId = ObjectId.get(),
    @BsonProperty("aggregateId") val aggregateId: UUID,
    @BsonProperty("event") val event: Event,
)
