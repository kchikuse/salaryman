FROM adoptopenjdk/openjdk8-openj9

MAINTAINER kchikuse

EXPOSE 80

ADD /maven/bot-*.war bot.war

ENTRYPOINT ["java","-jar","/bot.war"]
