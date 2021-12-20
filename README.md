# ktor-websocket
A websocket chat built with Ktor.

## Commands
##### ws://{api.url}/chat/{username}
Establishes a websocket connection and makes username available to receive messages.
#### ws://{api.url}/chat/{username}/whisper/{destination}
Establishes a communication channel between username and destination, just like a private chat.
