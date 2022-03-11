Il certificato da utilizzare è: pes-app.crt

Affinché il certificato sia riconosciuto fidato dai componenti chiamanti, esso deve essere importato 
nel file cacerts, utilizzato da java per censire le autorità di certificazione riconosciute. 
Per svolgere questa operazione, eseguire il seguente comando

keytool -import -file <CATALINA_HOME>/conf/tomcat.crt -keystore <JAVA_HOME>/jre/lib/security/cacerts

Sarà richiesta la password del keystore cacerts; per default, la password di questo keystore è changeit 