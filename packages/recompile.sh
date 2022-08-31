git pull
cd ..
./gradlew distZip
cp laboratory-cli/build/distributions/laboratory-cli-jvm.zip packages/
cd packages
sudo ./install.sh
