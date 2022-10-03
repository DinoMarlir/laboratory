#!/usr/bin/bash
read -p "Should all data and servers be deleted? [~/laboratory] (y/N) " -n 1 -r
echo    
if [[ $REPLY =~ ^[Yy]$ ]]
then
    rm -rf ~/laboratory
fi 

sudo rm /usr/bin/laboratory
sudo rm -rf /usr/share/laboratory/

echo "laboratory successfully uninstalled"
