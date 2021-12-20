package jacaceresf.dev

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import java.util.*

//import jacaceresf.dev.plugins.configureRouting
//import jacaceresf.dev.plugins.configureSockets

fun Application.module() {
    install(WebSockets)
    routing {
        val connections = Collections.synchronizedSet<Connection>(LinkedHashSet())
        webSocket("/chat/{username}") {
            val username = call.parameters["username"]

            val currentConnection = Connection(session = this, username = username!!)
            connections += currentConnection
            try {
                send("You are connected! There are ${connections.size} users here")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = String(frame.readBytes())
                    val textWithUserName = "[${currentConnection.username}]: $receivedText"
                    connections.forEach { it.session.send(textWithUserName) }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $currentConnection")
                connections -= currentConnection
            }

        }

        webSocket("/chat/{username}/whisper/{destination}") {

            val userName = call.parameters["username"]
            val destination = call.parameters["destination"]

            val currentConnection = Connection(session = this, username = userName!!)
            connections += currentConnection
            try {
                send("You are connected! There are ${connections.size} users here")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = String(frame.readBytes())
                    val textWithUserName = "[${currentConnection.username}]: $receivedText"
                    connections.find { it.username == destination }?.session?.send(textWithUserName)
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $currentConnection")
                connections -= currentConnection
            }

        }
    }

}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}
