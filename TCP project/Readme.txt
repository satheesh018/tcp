cd proxy-server/
mvn clean package
cd ..
cd ProxyClient/
mvn clean package
cd ..
sudo docker-compose up -d


 curl -x http://localhost:8080 http://httpforever.com/
