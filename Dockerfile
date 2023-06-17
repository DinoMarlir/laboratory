FROM openjdk:17-jdk-buster
RUN useradd -s /bin/sh -m chemicae
RUN apt update
RUN apt install sudo unzip bash curl git screen -y
RUN curl -sS https://raw.githubusercontent.com/DinoMarlir/laboratory/dev/reborn/packages/autoinstall.sh | bash
USER chemicae
WORKDIR /home/chemicae
RUN git clone https://github.com/mooziii/laboratory.git install
WORKDIR /home/chemicae/install
RUN git checkout dev/chemicae
RUN chmod +x gradlew
RUN ./gradlew laboratory-server:shadowJar
CMD ["java", "-jar", "laboratory-server/build/libs/laboratory-server-jvm-all.jar"]