#!/usr/bin/bash

#delete servers
#https://stackoverflow.com/questions/1885525/how-do-i-prompt-a-user-for-confirmation-in-bash-script

read -p "Should all data and servers be deleted? [~/laboratory] (y/N) " -n 1 -r
echo    
if [[ $REPLY =~ ^[Yy]$ ]]
then
    rm -rf ~/laboratory
fi 

sudo rm /usr/bin/laboratory
sudo rm -rf /usr/share/laboratory/

echo "laboratory successfully uninstalled"
