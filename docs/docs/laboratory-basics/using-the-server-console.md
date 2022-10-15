---
sidebar_position: 4
---

# Using the server console

:::caution
This only works on unix systems (Linux and macOS). Windows is not supported
:::

To attach to the server console run `laboratory attach <server-name>`.

If you want to run a command on the server from the outside, run `laboratory execute <server-name> <command>`. You may need to put the command in quotes.

### Example

Lets say we want to say Hello to all players on the server without attaching to the console of our "survival" server. 
To do that, we simply can run `laboratory execute survival "say Hello!"`. If we check the ingame-chat, we can see that the command was executed correctly:

![say-hello.png](/img/docs/say-hello.png)