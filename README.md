
1.Download the project
2.open the terminal in the project folder
3.execute the follwoing commands
  1.cd proxy-server/
  2.mvn clean package
  3.cd ..
  4.cd ProxyClient/
  5.mvn clean package
  6.cd ..
  7.docker-compose up -d
  8.cd
4.now we can try  curl -x http://localhost:8080 http://httpforever.com/ in the terminal
