$InitPwd = $Pwd

Set-Location ~\AppData\Local\laboratory
Remove-Item -Recurse *

git clone https://github.com/DinoMarlir/laboratory install
Set-Location install
git checkout dev/reborn
./gradlew installDist
Copy-Item laboratory-cli\build\install\laboratory-cli\* .. -Recurse
Set-Location ..
Remove-Item install -Recurse -Force
Remove-Item bin/laboratory-cli -Force
Set-Location bin
Rename-Item laboratory-cli.bat laboratory.bat