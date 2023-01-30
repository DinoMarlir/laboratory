echo "unzip is required to run this script, also this script must be executed with root privileges"

unzip -q laboratory-cli-jvm.zip

echo "#!/usr/bin/bash" > /usr/bin/laboratory
echo "/usr/share/laboratory/laboratory-cli-jvm/bin/laboratory-cli \"\$@\"" >> /usr/bin/laboratory
chmod +x /usr/bin/laboratory
mkdir /usr/share/laboratory/
cp -r laboratory-cli-jvm /usr/share/laboratory/
chmod +x /usr/share/laboratory/laboratory-cli-jvm/bin/laboratory-cli
rm -rf laboratory-cli-jvm
