FROM java:8
MAINTAINER ask.dog support@ask.dog

RUN useradd boot
RUN mkdir /data && touch /data/app.jar
RUN chown boot:boot /data
VOLUME /data

ENV BOOT_JAR app.jar
CMD java -jar /data/$BOOT_JAR