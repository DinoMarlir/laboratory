# laboratory

laboratory is the next generation [Minecraft](https://minecraft.net) server management tool fully written in [Kotlin](https://kotlinlang.org)

### Installation

**Linux:**

1. Clone this repository using `git clone https://github.com/mooziii/laboratory.git`
2. cd into the folder using `cd laboratory`
3. Build a distribution using `gradlew distZip` 
4. Copy the zip file into the packages folder `cp build/distributions/laboratory-jvm.zip packages/laboratory-jvm.zip`
5. Run the installation script with root privileges: `sudo ./install.sh`
6. Complete installation by running `laboratory`

**Windows:**
Don't use this on Windows

**macOS:**
It may work, idk just why would you run a minecraft server on macOS

This project was inspired by [pacmc](https://github.com/jakobkmar/pacmc) and [CloudNet](https://github.com/CloudNetService/CloudNet-v3)