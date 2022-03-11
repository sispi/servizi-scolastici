RELEASE NOTES (3.0)

- upgrade alla versione solr 6.5.0

- gestione della location di default da startup di solr:

La location di default (prefisso di tutti gli id generati) si configura come system property "location" e non più nel local.json
Per le vecchie istallazioni migrate da alfresco la location è DOCAREA
Per le nuove si può specificare DOCER

- supporto "realtime query" :

Un indice in memoria rende interrogabili i documenti non committati su tutti i campi indicizzati (indexed="true" in schema.xml)
Ad ogni commit l'indice viene automaticamente svuotato.
E' possibile verificare lo stato del RTI dalla cache "updateLog" o eseguendo una query q=uncommitted:true

Per attivare le funzionalità di realtime aggiungere le seguenti properties avviando solr:

 -> realtimequerycomponent=it.kdm.solr.realtime.RealTimeQueryComponent
    (attiva l'interrogazione del realtime index)

->  realtimeupdateprocessor=it.kdm.solr.realtime.RealTimeUpdateProcessorFactory
    (attiva la scrittura del realtime index)

Si impostino eventualmente i limiti dei campi nel realtime index (che verranno ignorati in query):

->  maxlengthRT<nome campo>=<max length in bytes>

Ad esempio per imporre un limite a 4096 bytes al campo "fulltext" ed ignorare "ABSTRACT" aggiungere:

->  maxlengthRTfulltext=4096
->  maxlengthRTABSTRACT=0


INTSALLAZIONE

1) scaricare e scompattare solr in una cartella a scelta (ex. /opt/solr-6.5.0)

2) copiare cartella server (distribuzione) sovrascrivendo i file in quella di solr (/opt/solr-6.5.0/server)

3A) nuova istallazione (esempio DOCER)

     1) avviare zk:

      >cd /opt/solr-6.5.0/server/zookeeper-3.4.6/bin
      >./zkServer.sh start

     2) avviare solr:

      >cd /opt/solr-6.5.0/bin
      >./solr start -c -z localhost:9983 -force -a "-Dlocation=DOCER -Drealtimequerycomponent=it.kdm.solr.realtime.RealTimeQueryComponent -Drealtimeupdateprocessor=it.kdm.solr.realtime.RealTimeUpdateProcessorFactory"

     3) creare la collezione DOCER con configset DOCER:

     >./solr create -force -c DOCER -d DOCER -shards 1 -replicationFactor 2

     4) inizializzare la collezione con lo script "init" nel dataimport:

     >curl "http://localhost:8983/solr/DOCER/dih/init?synchronous=true&wt=json&indent=true"

     5) verificare l'inizializzazione: devono essere presenti una location, un ente , una aoo, utente admin ed i tre gruppi di sistema:

     >curl "http://localhost:8983/solr/DOCER/select?q=*:*&ticket=admin&fl=id"

3B) upgrade istallazione solr-4.x (ex. cartella /opt/solr-4.10.3)

    1) fare optimize su solr4 e spegnerlo

    2) spegnere zookeeper:

    >cd /opt/solr-4.10.3/zookeeper-3.4.6/bin
    >./zkServer.sh stop

    2) copiare zookeeper dalla vecchia istallazione nella cartelle solr-6.5.0/server/zookeeper-3.4.6:

    >cp -r /opt/solr-4.10.3/zookeeper-3.4.6 /opt/solr-6.5.0

    3) copiare gli indici in solr-6.5.0/server/IndexUpgrader:

    >cp -r /opt/solr-4.10.3/server/solr/DOCER* /opt/solr-6.5.0/server/solr/

    4) eseguire upgrade da 4.x a 5.x e poi da 5.x a 6.x per ogni cartella/indice (esempio con due indici):

    >cd /opt/solr-6.5.0/server/IndexUpgrader
    >java -cp lucene-core-5.5.4.jar:lucene-backward-codecs-5.5.4.jar org.apache.lucene.index.IndexUpgrader -delete-prior-commits -verbose ../solr/DOCER_shard1_replica1/data/index
    >java -cp lucene-core-5.5.4.jar:lucene-backward-codecs-5.5.4.jar org.apache.lucene.index.IndexUpgrader -delete-prior-commits -verbose ../solr/DOCER_shard1_replica2/data/index
    >java -cp lucene-core-6.5.0.jar:lucene-backward-codecs-6.5.0.jar org.apache.lucene.index.IndexUpgrader -delete-prior-commits -verbose ../solr/DOCER_shard1_replica1/data/index
    >java -cp lucene-core-6.5.0.jar:lucene-backward-codecs-6.5.0.jar org.apache.lucene.index.IndexUpgrader -delete-prior-commits -verbose ../solr/DOCER_shard1_replica2/data/index

    5) avviare zookeeper:

    >cd /opt/solr-6.5.0/server/zookeeper-3.4.6/bin
    >./zkServer.sh start

    6) aggiornare configurazione DOCER:

    >cd /opt/solr-6.5.0/bin
    >./solr zk upconfig -z localhost:9983 -n DOCER -d DOCER

    7) avviare solr 6 specificando la location corretta (ex. DOCAREA) e property _version_=true (per retrocompatibilità) :

    >cd /opt/solr-6.5.0/bin
    >./solr start -c -z localhost:9983 -force -a "-D_version_=true -Dlocation=DOCAREA -Drealtimequerycomponent=it.kdm.solr.realtime.RealTimeQueryComponent -Drealtimeupdateprocessor=it.kdm.solr.realtime.RealTimeUpdateProcessorFactory"

    8) ottimizzare l'indice:

    >curl "http://localhost:8983/solr/DOCER/update?optimize=true"

    Se l'operazione è andata a buon fine si leggerà il nuovo contatore ad un valore corrisponente al numero degli oggetti dell'indice

    9) Opzionalmente si può ora impostare _version_=false in startup riavviando solr causando il campo _version_
    a non essere indicizzato (come consigliato dalla documentazione) e ottimizzare l'indice:

    >cd /opt/solr-6.5.0/bin
    >./solr start -c -z localhost:9983 -force -a "-Dlocation=DOCAREA -Drealtimequerycomponent=it.kdm.solr.realtime.RealTimeQueryComponent -Drealtimeupdateprocessor=it.kdm.solr.realtime.RealTimeUpdateProcessorFactory"
    >curl "http://localhost:8983/solr/DOCER/update?optimize=true"

4) configurare l'autocommit nel solrconfig.xml parametri congrui con l'ambiente
   Il numero massimo dei Docs ed l'intervallo automatico deve essere un compromesso tra risorse macchina e frequenza di update
   Molti update causeranno un aumento della RAM impiegata dal RTI se non si fa autocommit abbastanza spesso
   ma un autocommit troppo frequente rischia overlap e di caricare la CPU del sistema
   Si può diminuire la RAM impegnata configurando il numero massimo dei byte
   per campi specifici (maxlengthRT<nome campo>) o non indicizzarli a monte (schema.xml)

4) verificare nel local.json il puntamento del parametro "store" store (FileSystemProvider) e ricaricare la configurazione su zookeeper (oppure fare RELOAD da UI)

5) restringere la sicurezza di rete verso lo store, la servlet di solr e zookeeper per evitare manipolazioni.
   Si può fare tramite firewall oppure seguendo la documentazione dei due prodotti

