# laboratory

laboratory is the next generation [Minecraft](https://minecraft.net) server management tool fully written in [Kotlin](https://kotlinlang.org)

### Installation

**Linux:**

1. Clone this repository using `git clone https://github.com/mooziii/laboratory.git laboratory-installation`
2. Checkout the development branch using `git checkout dev/chemicae`
3. cd into the folder using `cd laboratory-installation`
4. Give permissions to the gradle file using `chmod +x gradlew`
5. Build a distribution using `./gradlew distZip`
6. Copy the zip file into the packages folder `cp build/distributions/laboratory-cli-jvm.zip packages/laboratory-cli-jvm.zip`
7. cd into the packages directory using `cd packages`
8. Give permissions to the installation script using `chmod +x install.sh`
9. Run the installation script with root privileges: `sudo ./install.sh`
10. Complete installation by running `laboratory`

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
- [QuiltMC](https://quiltmc.org)
- [FabricMC](https://fabricmc.net)
- [LegacyFabric](https://legacyfabric.net)
- [SpongeVanilla](https://spongepowered.org/downloads/spongevanilla)
- [Velocity](https://papermc.io/downloads#Velocity)
- [Waterfall](https://papermc.io/downloads#Waterfall)
- [Vanilla](https://minecraft.net)

~~Support for [Forge](https://minecraftforge.net) is planned.~~ Forge is stupid. See [#10](https://github.com/mooziii/laboratory/issues/10)

This project was inspired by [pacmc](https://github.com/jakobkmar/pacmc) and [CloudNet](https://github.com/CloudNetService/CloudNet-v3)
