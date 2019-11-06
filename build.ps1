Clear-Host
Set-Location sdk
.\gradlew.bat clean build uploadArchives
Set-Location ..
if ($LastExitCode -eq 0)
{
    return
}

Clear-Host
Set-Location service
gradle clean build
Set-Location ..
if ($LastExitCode -eq 0)
{
    return
}

Clear-Host
Set-Location android
.\gradlew.bat iD
if ($LastExitCode -eq 0)
{
    return
}
