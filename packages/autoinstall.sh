cd ~
rm -rf laboratory-installation
git clone https://github.com/DinoMarlir/laboratory.git laboratory-installation
cd laboratory-installation
git checkout dev/reborn
cd packages
chmod +x install.sh
./install.sh