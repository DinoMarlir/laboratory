$InitPwd = $Pwd

mkdir ~\AppData\Local\laboratory
Set-Location ~\AppData\Local\laboratory
git clone https://github.com/mooziii/laboratory install
Set-Location install
git checkout dev/chemicae
./gradlew installDist
Copy-Item laboratory-cli\build\install\laboratory-cli\* .. -Recurse
Set-Location ..
Remove-Item install -Recurse -Force
Remove-Item bin/laboratory-cli -Force
Set-Location bin
Rename-Item laboratory-cli.bat laboratory.bat

$FinalPath = [Environment]::GetEnvironmentVariable("PATH", "User") + ";" + $Pwd
[Environment]::SetEnvironmentVariable( "Path", $FinalPath, "User" )

Set-Location $InitPwd
