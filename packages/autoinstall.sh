git clone https://github.com/mooziii/laboratory.git laboratory-installation
cd laboratory-installation
git checkout dev/chemicae
cd packages
chmod +x install.sh
echo "########################################################################"
echo "# You'll be prompted for your password in order to install laboratory. #"
echo "########################################################################"
sudo ./install.sh
laboratory info