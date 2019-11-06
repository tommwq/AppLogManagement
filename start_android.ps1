$IPList=[System.Net.Dns]::GetHostAddresses($ComputerName) | Where-Object {  $_.AddressFamily -eq 'InterNetwork'} |Select-Object -ExpandProperty IPAddressToString
$IP=$IPList[$IPList.length-1]

adb shell am start-activity -S --es HOST $IP --es PORT 50051 com.tq.applogmanagement/com.github.tommwq.applogmanagement.MainActivity 
