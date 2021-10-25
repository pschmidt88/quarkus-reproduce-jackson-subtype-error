package org.acme.event_sourcing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mongodb.MongoClientSettings
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.Document
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

class EventCodec(
    private val documentCodec: Codec<Document> = MongoClientSettings.getDefaultCodecRegistry().get(Document::class.java),
    private val objectMapper: ObjectMapper
) : Codec<Event> {

    override fun encode(writer: BsonWriter?, value: Event?, encoderContext: EncoderContext?) {
        val document = Document.parse(objectMapper.writeValueAsString(value))
        documentCodec.encode(writer, document, encoderContext)
    }

    override fun getEncoderClass(): Class<Event> {
        return Event::class.java
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): Event {
        val document = documentCodec.decode(reader, decoderContext)
        return objectMapper.readValue(document.toJson())
    }
}