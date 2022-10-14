---
sidebar_position: 2
---

# Server Lifecycle

## Starting a Server

To start a server run `laboratory start <server-name>`. To start every server, type `laboratory start "*"`
The start command has the following options available:

- `-a` or `--attach` will instantly attach you to the servers console (on Windows, this is default behaviour. Only works on a single server)
- `-s` or `--shell` will disable any other prompts (Use this for automated shell scripts!). **Warning:** this will confirm every prompt with yes.

## Stopping a Server

To stop a server run `laboratory stop <server-name>`. To stop every server, type `laboratory stop "*"`
The stop command has the following option available:

:::caution
This will corrupt server files. Only use if really needed
:::
- `-f` or `--force` will forcibly terminate the server. 

## Restart a Server

To restart a server run `laboratory restart <server-name>`. To restart every server, type `laboratory restart "*"`
The restart command has the following option available:

:::caution
This will corrupt server files. Only use if really needed
:::
- `-f` or `--force` will forcibly terminate the server.

- `-a` or `--attach` will instantly attach you to the servers console. (on Windows, this is default behaviour. Only works on a single server)