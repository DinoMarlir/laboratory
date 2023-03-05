---
sidebar_position: 1
---

# Chemicae in Docker

## Prerequisites

- Install Docker following the [official guide](https://docs.docker.com/engine/install/)
- Install Java 17 or higher. You may get it from [Adoptium](https://adoptium.net/)
- Install git

## Build the Image

To build the Docker image you have to clone laboratory via git:

1. To clone the repository run `git clone https://github.com/mooziii/laboratory.git chemicae`
2. cd into the directory: `cd chemicae`
3. Build the image by running `docker build -t chemicae .`. This may take a while depending on your system.

## Run chemicae

To run chemicae just execute this command:

`docker run -itd -p 25565:25565 -v /home/$(whoami)/laboratory:/home/chemicae/laboratory -v /usr/lib/jvm:/usr/lib/jvm --name=chemicae --restart=always chemicae`

This will expose the port 25565 to the outside and will always restart the container.
It also passes your `/usr/lib/jvm/` folder to the container to ensure your servers run on the correct java version

If you want to handle port management by your own, you can use the `host` network:

`docker run -itd --network=host -v /home/$(whoami)/laboratory:/home/chemicae/laboratory -v /usr/lib/jvm:/usr/lib/jvm --name=chemicae --restart=always chemicae`

## Manage servers from the outside

To manage servers from the outside of the container, I recommend to add an alias to your `.bashrc`.
Do this by running the following command:
`echo alias 'laboratory'='docker exec -it chemicae bash laboratory' >> ~/.bashrc`
