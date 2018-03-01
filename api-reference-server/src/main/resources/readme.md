Test server configuration
---------------------------

cfsupplier API reference API implementation and state change flow. 


Run by starting HttpMicroservice
--------------------------------

sbt "run-main com.cabforce.service.HttpMicroservice"


Install sbt on linux
--------------------

Help page :  http://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html

echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list

sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823

sudo apt-get update

sudo apt-get install sbt