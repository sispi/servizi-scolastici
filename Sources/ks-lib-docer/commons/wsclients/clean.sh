#!/bin/sh

for f in `find -name "WS*Stub.java"`
do
	echo $f
    NAME=`echo $f | perl -p -e "s/.\/WS(.*)Stub\.java/\1/g"`
    ENAME="$NAME""ExceptionException"
    sed -i "s/WS$NAME$ENAME/$ENAME/g" $f
done

for f in `find -name "*ExceptionException.java"`
do
    echo $f
    NAME=`echo $f | perl -p -e "s/.\/(.*)ExceptionException\.java/\1/g"`
    ENAME="$NAME""Exception"
    EENAME="$NAME""ExceptionE"
    sed -i "s/$EENAME /$ENAME /g" $f
done


sed -i "s/WSVerificaDocumentiVerificaDocumentoExceptionException/VerificaDocumentoExceptionException/g" WSVerificaDocumentiStub.java
sed -i "s/DocerServicesDocerExceptionException/DocerExceptionException/g" DocerServicesStub.java