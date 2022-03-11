FROM tomcat:7
MAINTAINER Alessio Dominici <alessio.dominici@gruppofilippetti.it>

ENV DOCER_CONFIG /opt/docer/configurations/settings
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

RUN mkdir -p /opt/docer/configurations/settings && \
    apt-get update && \
    apt-get install -y vim && \
    apt-get clean && \
    apt-get autoclean && \
    groupadd -r tomcat -g 433 && \
    useradd -u 431 -r -g tomcat -c "Tomcat user" tomcat && \
    chown -R tomcat:tomcat /usr/local/tomcat && \
    chown -R tomcat:tomcat /opt/docer

COPY --chown=tomcat:tomcat ./WAR/. /usr/local/tomcat/webapps
COPY --chown=tomcat:tomcat ./config/. /opt/docer/configurations/settings

USER tomcat
EXPOSE 8080

VOLUME ["/opt/docer", "/usr/local/tomcat/logs"]

ENTRYPOINT ["catalina.sh", "run"]
