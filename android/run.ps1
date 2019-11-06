Clear-Host

$IPList=[System.Net.Dns]::GetHostAddresses($ComputerName) | Where-Object {  $_.AddressFamily -eq 'InterNetwork'} |Select-Object -ExpandProperty IPAddressToString
$IP=$IPList[$IPList.length-1]

Set-Location ../sdk
.\gradlew.bat build uploadArchives

Set-Location ../android

if ($LastExitCode -eq 0)
{
    .\gradlew.bat iD
    if ($LastExitCode -eq 0)
    {
        adb shell am start-activity --es HOST $IP --es PORT 50051 com.tq.applogmanagement/com.github.tommwq.applogmanagement.MainActivity 
    }
}
