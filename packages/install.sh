#!/usr/bin/bash
cd ..
chmod +x gradlew
./gradlew distZip
cp laboratory-cli/build/distributions/laboratory-cli-jvm.zip packages/laboratory-cli-jvm.zip
cd packages
chmod +x copyfiles.sh
clear
echo "########################################################################"
echo "# You'll be prompted for your password in order to install laboratory. #"
echo "########################################################################"
sudo ./copyfiles.sh
clear
if [[ $SHELL == *"bash"* ]]
then
    _LABORATORY_COMPLETE=bash laboratory > ~/laboratory/bash-completion.sh
    echo "#Tab completion for laboratory. Do not remove this" >> ~/.bashrc
    echo "source ~/laboratory/bash-completion.sh" >> ~/.bashrc
    source ~/laboratory/bash-completion.sh
fi
if [[ $SHELL == *"zsh"* ]]
then
    _LABORATORY_COMPLETE=bash laboratory > ~/laboratory/zsh-completion.sh
    echo "#Tab completion for laboratory. Do not remove this" >> ~/.zshrc
    echo "source ~/laboratory/zsh-completion.sh" >> ~/.zshrc
    source ~/laboratory/zsh-completion.sh
fi
echo "laboratory was installed successfully"