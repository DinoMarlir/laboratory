---
sidebar_position: 1
---

# Creating a Server

To create a server run `laboratory create <server-name> <server-software>`

Where `server-name` is a name that you can choose for your server, `server-software` defines what software will be used for the server.
You can choose from one of those: 
- [PaperMC](https://papermc.io) -> `papermc`
- [PurpurMC](https://purpurmc.org) -> `purpurmc`
- [QuiltMC](https://quiltmc.org) -> `quiltmc`
- [FabricMC](https://fabricmc.net) -> `fabricmc`
- [LegacyFabric](https://legacyfabric.net) -> `legacyfabric`
- [SpongeVanilla](https://spongepowered.org/downloads/spongevanilla) -> `sponge`
- [Forge](https://minecraftforge.net) -> `forge`
- [Velocity](https://papermc.io/downloads#Velocity) -> `velocity`
- [Waterfall](https://papermc.io/downloads#Waterfall) -> `waterfall`
- [Vanilla](https://minecraft.net) -> `vanilla`
- Mojang Mapped¹ Paper Servers (not listed on paper downloads site as of now) -> `paper-mojmap`
- Laboratory also allows you to import custom jar files, for that use `custom-jar` as server-software.

¹ Mojang Mapped means that the server code has not been re-obfuscated and is using the official class, method and field names from Mojang.

You will be prompted whether you want to configure the server now. If you answer with No, laboratory will go with the defaults (1GB memory, port 25565, automatic updates, static server)
After that, confirm with `y` if you want the server to start now. If not, type `n`.

![server-creation.png](/img/docs/server-creation.png)

## Server directory

If you configured your server as static (which is default), you can find the server directory in `~/laboratory/live/servers/`