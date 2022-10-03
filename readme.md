# laboratory

laboratory is the next generation [Minecraft](https://minecraft.net) server management tool fully written in [Kotlin](https://kotlinlang.org)

### Installation

**Linux:**

1. Clone this repository using `git clone https://github.com/mooziii/laboratory.git laboratory-installation`
2. cd into the folder using `cd laboratory-installation`
3. Checkout the development branch using `git checkout dev/chemicae`
4. cd into the packages directory using `cd packages`
5. Give permissions to the compile script file using `chmod +x compile.sh`
6. Run the compile-script using `./install.sh` (you'll be prompted for the sudo password)
7. Complete the installation process by running `laboratory`

**Windows:**
Don't use this on Windows

**macOS:**
It may work, why would you run a minecraft server on macOS

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
