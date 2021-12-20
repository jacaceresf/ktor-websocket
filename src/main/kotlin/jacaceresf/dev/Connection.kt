package jacaceresf.dev

import io.ktor.http.cio.websocket.*

data class Connection(val session: DefaultWebSocketSession, val username: String)