FROM openjdk:8

RUN \
  curl -L -o sbt-0.13.16.deb http://dl.bintray.com/sbt/debian/sbt-0.13.16.deb && \
  dpkg -i sbt-0.13.16.deb && \
  rm sbt-0.13.16.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion


WORKDIR /cfapiserver

ADD . /cfapiserver

CMD sbt run

