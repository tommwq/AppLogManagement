# # 停止并移除所有容器
# docker stop $(docker ps -a -q)
# docker rm $(docker ps -a -q)

# # 移除tq/applog:0.0.1
docker rmi tq/applog:0.0.1
docker rmi tq/applogcli:0.0.1

# applog
cp tq-applog.dockerfile ../service/build/libs/Dockerfile
cp application.properties ../service/build/libs/application.properties
docker build -t tq/applog:0.0.1 ../service/build/libs

# applogcli
cp tq-applogcli.dockerfile ../desktop-demo/build/libs/Dockerfile
docker build -t tq/applogcli:0.0.1 ../desktop-demo/build/libs

