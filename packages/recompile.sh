git pull
cd ..
chmod +x gradlew
./gradlew distZip
cp laboratory-cli/build/distributions/laboratory-cli-jvm.zip packages/laboratory-cli-jvm.zip
cd packages
chmod +x install.sh
sudo ./install.sh
