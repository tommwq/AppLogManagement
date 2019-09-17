cp Dockerfile ../service/build/libs/Dockerfile
cp application.properties ../service/build/libs/application.properties

docker build -t tq/applog:0.0.1 ../service/build/libs/
