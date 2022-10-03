# laboratory

laboratory is the next generation [Minecraft](https://minecraft.net) server management tool fully written in [Kotlin](https://kotlinlang.org)

### Installation

**Linux:**

Run this command `curl -sS https://raw.githubusercontent.com/mooziii/laboratory/dev/chemicae/packages/autoinstall.sh | bash`
You will be prompted for your sudo password (user specific installation methods that don't require root access will come soon).

**Windows:**
soon

**macOS:**
Same as linux but didn't test it

### Updating 

To update laboratory, just run the installation steps again (this time do `cd laboratory` first and then do `git pull` instead of clone) and confirm the copy action with `A`. You will get some errors but that is fine as it tries to create folders that already exist. Verify update by running `laboratory info`

### Usage

To create a server run `laboratory create <server-name> <server-software>`

To start an existing server run `laboratory start [server-name]` and select the server.

### Demo 

[![asciicast](https://asciinema.org/a/514193.svg)](https://asciinema.org/a/514193)

### Notes

laboratory currently supports the following server softwares:

- [PaperMC](https://papermc.io)
- [PurpurMC](https://purpurmc.org)
- [QuiltMC](https://quiltmc.org)
- [FabricMC](https://fabricmc.net)
- [LegacyFabric](https://legacyfabric.net)
- [SpongeVanilla](https://spongepowered.org/downloads/spongevanilla)
- [Forge](https://minecraftforge.net)
- [Velocity](https://papermc.io/downloads#Velocity)
- [Waterfall](https://papermc.io/downloads#Waterfall)
- [Vanilla](https://minecraft.net)
- custom jar files
- Mojang Mapped Paper Servers (not on paper downloads site as of now)

This project was inspired by [pacmc](https://github.com/jakobkmar/pacmc) and [CloudNet](https://github.com/CloudNetService/CloudNet-v3)
