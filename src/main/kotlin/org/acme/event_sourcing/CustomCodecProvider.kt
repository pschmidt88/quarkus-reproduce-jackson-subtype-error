package org.acme.event_sourcing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry
import javax.enterprise.context.ApplicationScoped

// will be auto registered by quarkus
@Suppress("unused")
@ApplicationScoped
class CustomCodecProvider() : CodecProvider {

    val mapper: ObjectMapper = jacksonObjectMapper().apply {
        registerModules(Jdk8Module(), JavaTimeModule())
    }

    override fun <T> get(clazz: Class<T>, registry: CodecRegistry): Codec<T>? {
        if (clazz == Event::class.java) {
            return EventCodec(objectMapper = mapper) as Codec<T>
        }

        return null
    }
}