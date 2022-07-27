#!/usr/bin/bash
echo "unzip is required to run this script, also this script must be executed with root privileges"

unzip laboratory-jvm.zip
unzip laboratory-jvm.tar.gz

echo "#!/usr/bin/bash" > /usr/bin/laboratory
echo "/usr/share/laboratory/laboratory-jvm/bin/laboratory \"\$@\"" >> /usr/bin/laboratory
chmod +x /usr/bin/laboratory
mkdir /usr/share/laboratory/
cp -r laboratory-jvm /usr/share/laboratory/
chmod +x /usr/share/laboratory/laboratory-jvm/bin/laboratory