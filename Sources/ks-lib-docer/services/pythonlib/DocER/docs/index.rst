.. DocER documentation master file, created by
   sphinx-quickstart on Tue Nov  8 17:04:26 2011.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Benvenuti alla documentazione di DocER
======================================

La libreria DocER fornisce un insieme di metodi per semplificare la creazione
di documenti all'interno del sistema documentale.

Esempio
-------

Un breve esempio puo' essere d'aiuto::

    # sample.py
    import datetime
    
    import docer


    docer.set_docer_url("http://docertest.ente.regione.emr.it/WSDocer/services/DocerServices")
    docer.set_auth_url("http://docertest.ente.regione.emr.it/docersystem/services/AuthenticationService")

    docer.set_ente("EMR")
    docer.set_aoo("AOO_EMR")

    # Chiamata di login per ottenere il token.
    # In ambiente di collaudo la library dovrebe essere "docer"
    token = docer.login("username", "pwd", "library")
    

    # La libreria funziona sia con dizionari che con oggetti
    # per gli argomenti chiave e dati. Di seguito sono 
    # illustrati entrambi gli approcci.

    # I campi delle rispettive strutture dati sono documentati
    # nelle docstrings dei metodi create_burert e create_burertss

    class obj:
        pass
    
    chiave = obj()
    chiave.anno = 2011
    chiave.numero = 9
    
    dati = {}
    dati['annata'] = 2011
    dati['data'] = datetime.date.today()
    dati['numerazione'] = 1
    dati['parte'] = 14

    # Non e' necessario passare un file, e' sufficiente un file-like object.
    # I metodi utilizzati dalla libreria sono file.read() e file.name
    # NB: DocER si aspetta che i filename abbiano sempre un estensione
    f = open("filename.pdf", "rb")

    docid = docer.create_burert(token, chiave, dati, f)

    print docid


Documentazione di Riferimento dei Moduli:
-----------------------------------------

.. toctree::
   :maxdepth: 2

   docer

Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`

