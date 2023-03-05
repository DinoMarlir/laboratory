---
sidebar_position: 1
---

# Laboratory Kotlin API

The laboratory Kotlin API uses the native laboratory-common module. This means the kotlin "driver" will always have the most features but may not be reliable as other drivers! Expect breaking changes

## Setup

Gradle (Kotlin DSL)
```kts
repositories {
    maven("https://repo.obsilabor.me/snapshots")
}

dependencies {
    implementation("me.obsilabor:laboratory-common:LATEST-VERSION")
}
```

You may look up the latest version [here](https://raw.githubusercontent.com/mooziii/laboratory/dev/chemicae/.meta/version)

## Invoke laboratory

As you're using the native project as your API, you have to invoke the mainScope first.
To do this, add the following code to your entrypoint:

```kotlin
import me.obsilabor.laboratory.mainScope

fun main() {
    coroutineScope {
        mainScope = this
        Architecture.setupArchitecture()
    }
}
```

### Agree to the EULA

If you're trying to use laboratory in an isolated container or similar, you have to accept the minecraft EULA first.
Do this by adding this code:

```kotlin
val cfg = Config.userConfig // Get the user config
cfg.acceptedEULA = true // Accept the EULA
Config.writeUserFile(cfg) // Save the user config
```

## Example Code: Create a server

```kotlin
val db = JsonDatabase.db // Receive the database
val platform = FabricPlatform // Select the server platform
val server = Server(
    db.internalCounter,
    "server-name",
    true, // static?
    true, // copyTemplates?
    mutableSetOf(), // list of templates
    platform.name, // platform name
    platform.getBuilds("1.19.3").last(), // platform build
    "1.19.3", // minecraft version
    false, // automatic updates
    2048, // memory
    mutableSetOf("-Dlog4j2.formatMsgNoLookups=true"), // jvm args
    mutableSetOf("nogui"), // process args
    25565 // port
)
db.internalCounter++ // Increase the internalCounter
JsonDatabase.writeFile(db) // Write the database
JsonDatabase.registerServer(server) // Register the server

// Now you can start the server by using
server.start(noScreen = true)
```

## Further help

If you're unsure how to do something, you can always take a look at the CLI source-code to see how it's done there.