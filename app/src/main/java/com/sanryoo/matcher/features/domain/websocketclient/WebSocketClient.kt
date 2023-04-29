package com.sanryoo.matcher.features.domain.websocketclient

import com.sanryoo.matcher.features.data.api.ApiToken
//import io.reactivex.Observable
//import io.reactivex.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.Observable
import okhttp3.*
import okio.ByteString
import java.io.StringReader
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level
import java.util.logging.Logger

class WebSocketClient(
    private val url: String,
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val reconnectAfter: Long = 5000L
) : WebSocketListener() {

    private val logger = Logger.getLogger(javaClass.name)

    private val topics = HashMap<String, String>()
    private val emitters = ConcurrentHashMap<String, ObservableEmitter<String>>()

    private var shouldBeConnected: Boolean = false
    private var connected = false

    private lateinit var webSocket: WebSocket

    private lateinit var emitter: ObservableEmitter<Event>

    companion object {
        private const val DEFAULT_ACK = "auto"
        private const val SUPPORTED_VERSIONS = "1.1,1.2"
    }

    fun connect(): Observable<Event> {
        return Observable.create {
            emitter = it
            shouldBeConnected = true
            open()
        }.doOnDispose {
            close()
            shouldBeConnected = false
        }
    }

    fun join(topic: String): Observable<String> {
        return Observable.create {

            val topicId = UUID.randomUUID().toString()

            val headers = HashMap<String, String>()
            headers[Headers.ID] = topicId
            headers[Headers.DESTINATION] = topic
            headers[Headers.ACK] = DEFAULT_ACK
            headers[Headers.ACK] = DEFAULT_ACK
            headers[Headers.AUTHORIZATION] = "Bearer ${ApiToken.token.value}"
            webSocket.send(compileMessage(Message(command = Commands.SUBSCRIBE, headers = headers)))

            emitters[topic] = it
            topics[topic] = topicId

            logger.log(Level.INFO, "Subscribed to: $topic id: $topicId")

        }.doOnDispose {

            val topicId = topics[topic]

            val headers = HashMap<String, String>()
            headers[Headers.ID] = topicId!!
            webSocket.send(
                compileMessage(Message(command = Commands.UNSUBSCRIBE, headers = headers))
            )

            emitters.remove(topic)
            topics.remove(topicId)

            logger.log(Level.INFO, "Unsubscribed from: $topic id: $topicId")

        }
    }

    fun send(topic: String, msg: String): Observable<Boolean> {
        return Observable.create {
            val headers = HashMap<String, String>()
            headers[Headers.DESTINATION] = topic
            headers[Headers.AUTHORIZATION] = "Bearer ${ApiToken.token.value}"
            it.onNext(
                webSocket.send(
                    compileMessage(
                        Message(
                            command = Commands.SEND,
                            headers = headers,
                            payload = msg
                        )
                    )
                )
            )
            it.onComplete()
        }
    }

    private fun open() {
        if (!connected) {
            logger.log(Level.INFO, "Connecting...")
            val request = Request.Builder()
                .url(url)
                .addHeader(Headers.AUTHORIZATION, "Bearer ${ApiToken.token.value}")
                .build()
            webSocket = okHttpClient.newWebSocket(request, this)
            connected = true
        } else {
            logger.log(Level.INFO, "Already connected")
        }
    }

    private fun reconnect() {
        if (shouldBeConnected) {
            close()
            Thread.sleep(reconnectAfter)
            open()
        }
    }

    private fun close() {
        if (connected) {
            logger.log(Level.INFO, "Disconnecting...")
            webSocket.close(1000, "")
            connected = false
        } else {
            logger.log(Level.INFO, "Already disconnected")
        }
    }

    private fun parseMessage(data: String?): Message {
        if (data.isNullOrBlank()) return Message(command = Commands.UNKNOWN)

        val reader = Scanner(StringReader(data))
        reader.useDelimiter("\\n")
        val command = reader.next()
        val headers = HashMap<String, String>()

        while (reader.hasNext(Message.PATTERN_HEADER)) {
            val matcher = Message.PATTERN_HEADER.matcher(reader.next())
            matcher.find()
            headers[matcher.group(1) as String] = matcher.group(2) as String
        }

        reader.skip("\\s")

        reader.useDelimiter(Message.TERMINATE_MESSAGE_SYMBOL)
        val payload = if (reader.hasNext()) reader.next() else null

        return Message(command, headers, payload!!)
    }

    private fun compileMessage(message: Message): String {
        val builder = StringBuilder()

        if (message.command != null) builder.append(message.command).append('\n')

        for ((key, value) in message.headers) builder.append(key).append(':').append(value)
            .append('\n')
        builder.append('\n')

        if (message.payload != null) builder.append(message.payload).append("\n\n")

        builder.append(Message.TERMINATE_MESSAGE_SYMBOL)
        return builder.toString()
    }

    // from WebSocketListener listener

    override fun onOpen(webSocket: WebSocket, response: Response) {
        val headers = HashMap<String, String>()
        headers[Headers.VERSION] = SUPPORTED_VERSIONS
        this.webSocket.send(compileMessage(Message(Commands.CONNECT, headers)))
        logger.log(Level.INFO, "onOpen")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        emitter.onNext(Event.CLOSED)
        logger.log(Level.INFO, "onClosed reason: $reason, code: $code")
        reconnect()
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        handleMessage(parseMessage(bytes.toString()))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        handleMessage(parseMessage(text))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(code, reason)
        logger.log(Level.INFO, "onClosing reason: $reason, code: $code")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        emitter.onNext(Event.ERROR)
        logger.log(Level.INFO, "onFailure", t)
        reconnect()
    }

    private fun handleMessage(message: Message) {
        when (message.command) {
            Commands.CONNECTED -> {
                emitter.onNext(Event.OPENED)
            }
            Commands.MESSAGE -> {
                val dest = message.headers[Headers.DESTINATION]
                if (dest != null) {
                    emitters[dest]?.onNext(message.payload!!)
                }
            }
        }
        logger.log(
            Level.INFO,
            "onMessage payload: ${message.payload}, headers:${message.headers}, command: ${message.command}"
        )
    }

}