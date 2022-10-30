FROM ubuntu:latest
RUN useradd -s /bin/sh -m chemicae
RUN apt update
RUN apt install sudo curl git screen openjdk-17-jre -y
RUN curl -sS https://raw.githubusercontent.com/mooziii/laboratory/dev/chemicae/packages/autoinstall.sh | bash # Install normal laboratory as it is required by chemicae
USER chemicae
WORKDIR /home/chemicae
RUN git clone https://github.com/mooziii/laboratory.git install
WORKDIR /home/chemicae/install
RUN git checkout dev/chemicae
RUN chmod +x gradlew
RUN ./gradlew laboratory-server:shadowJar
CMD ["java", "-jar", "laboratory-server/build/libs/laboratory-server-jvm-all.jar"]