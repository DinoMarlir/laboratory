#!/usr/bin/bash
cd ..
chmod +x gradlew
./gradlew distZip
cp laboratory-cli/build/distributions/laboratory-cli-jvm.zip packages/laboratory-cli-jvm.zip
cd packages
chmod +x copyfiles.sh
sudo ./copyfiles.sh
if [[ $SHELL == *"bash"*]] then
    _LABORATORY_COMPLETE=bash laboratory > ~/laboratory/bash-completion.sh
    echo "#Tab completion for laboratory. Do not remove this" >> ~/.bashrc
    echo "source ~/laboratory/bash-completion.sh" >> ~/.bashrc
fi
if [[ $SHELL == *"zsh"*]] then
    _LABORATORY_COMPLETE=bash laboratory > ~/laboratory/zsh-completion.sh
    echo "#Tab completion for laboratory. Do not remove this" >> ~/.zshrc
    echo "source ~/laboratory/zsh-completion.sh" >> ~/.zshrc
fi