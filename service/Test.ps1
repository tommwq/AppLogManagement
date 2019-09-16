$ok = 0

Set-Location ..\sdk
gradle clean build
if ($LastExitCode -eq 0) {
    $ok = 1
}
Set-Location ..\service

if ($ok -eq 1) {
    Clear-Host
    gradle clean bootRun
}
