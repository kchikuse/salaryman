FROM anapsix/alpine-java

MAINTAINER kchikuse

EXPOSE 80

VOLUME /tmp

ADD /maven/bot-*.war bot.war

RUN sh -c 'touch /bot.war'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/bot.war"]