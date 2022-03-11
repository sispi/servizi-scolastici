#!/bin/bash

mkdir WAR
cp ../services/wsdocer/webservice/target/WSDocer.war ./WAR/
cp ../services/fascicolazione/docer.fascicolazione.webservices/target/WSFascicolazione.war ./WAR/
cp ../services/protocollazione/docer.protocollazione.webservices/target/WSProtocollazione.war ./WAR/
cp ../core/docersystem/target/docersystemMySql.war ./WAR/docersystem.war

ls -l ./WAR/
