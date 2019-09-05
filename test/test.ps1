Remove-Item -Force node1/*.war
Remove-Item -Force node2/*.war

Set-Location ../service
gradle clean bootWar

Set-Location ../test
Copy-Item -Force ../service/build/libs/applogmanagment-0.0.1-SNAPSHOT.war node1/
Copy-Item -Force ../service/build/libs/applogmanagment-0.0.1-SNAPSHOT.war node2/

Start-Process java.exe -ArgumentList "-jar","applogmanagment-0.0.1-SNAPSHOT.war" -WorkingDirectory node1

Start-Process java.exe -ArgumentList "-jar","applogmanagment-0.0.1-SNAPSHOT.war" -WorkingDirectory node2

Start-Process gradle -ArgumentList "bootRun" -WorkingDirectory "../desktop-demo/"

