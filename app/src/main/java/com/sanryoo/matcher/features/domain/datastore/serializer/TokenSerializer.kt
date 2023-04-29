package com.sanryoo.matcher.features.domain.datastore.serializer

import androidx.datastore.core.Serializer
import com.sanryoo.matcher.features.domain.datastore.key_store.CryptoManager
import com.sanryoo.matcher.features.domain.model.Token
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class TokenSerializer(private val cryptoManager: CryptoManager) : Serializer<Token> {

    override val defaultValue: Token
        get() = Token()

    override suspend fun readFrom(input: InputStream): Token {
        return Json.decodeFromString(
            deserializer = Token.serializer(),
            string = cryptoManager.decrypt(input).decodeToString()
        )
    }

    override suspend fun writeTo(t: Token, output: OutputStream) {
        cryptoManager.encrypt(
            iv = Json.encodeToString(
                serializer = Token.serializer(), value = t
            ).encodeToByteArray(),
            outputStream = output
        )
    }

}