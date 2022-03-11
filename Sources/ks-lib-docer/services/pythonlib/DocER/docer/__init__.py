from poster.encode import multipart_encode, MultipartParam
from poster.streaminghttp import register_openers

import urllib2, base64, os

try:
    from xml.etree import ElementTree as etree
except ImportError:
    import elementtree.ElementTree as etree

__all__ = ["login", "create_burert", "create_burertss", "set_auth_url", "get_auth_url", 
           "set_docer_url", "set_ente", "get_ente", "set_aoo", "get_aoo", "set_app_versante", 
           "get_app_versante", "set_type_id", "get_type_id"]

register_openers()

__url__ = "http://192.168.0.16:8080/WSDocer/services/DocerServices/"
__auth_url__ = "http://192.168.0.16:8080/docersystem/services/AuthenticationService/"

__soap_ns__ = "http://www.w3.org/2003/05/soap-envelope"
__docer_ns__ = "http://webservices.docer.kdm.it"
__auth_ns__ = "http://authentication.core.docer.kdm.it"
__sdk_ns__ = "http://classes.sdk.docer.kdm.it/xsd"

__docer_date_format__ = "%Y/%m/%d %H:%M:%S"
__printable_date_format__ = "%d/%m/%Y"

__ente__ = "EMR"
__aoo__ = "AOO_EMR"
__type_id__ = 'DOCUMENTO'
__app_versante__ = 'BURERT'

def set_ente(codice_ente):
    """Imposta l'ente da utilizzare in creazione"""
    global __ente__
    __ente__ = codice_ente

def get_ente():
    return __ente__

def set_aoo(codice_aoo):
    """Imposta l'aoo da utilizzare in creazione"""
    global __aoo__
    __aoo__ = codice_aoo

def get_aoo():
    return __aoo__

def set_type_id(type_id):
    """Imposta il type id da utilizzare in creazione"""
    global __type_id__
    __type_id__ = type_id

def get_type_id():
    return __type_id__

def set_app_versante(app_versante):
    """Imposta l'app versante da utilizzare in creazione"""
    global __app_versante__
    __app_versante__ = app_versante

def get_app_versante():
    return __app_versante__

def __parse_url(url):
    if url.endswith("?wsdl"):
        url = url[:url.index('?')]
    if url[-1] != '/': 
        url += '/'

    return url

def set_docer_url(url):
    """Imposta l'url del servizio WSDocer"""
    global __url__
    __url__ = __parse_url(url)

def get_docer_url():
    return __url__

def set_auth_url(url):
    """Imposta l'url del servizio di autenticazione di DocER"""
    global __auth_url__
    __auth_url__ = __parse_url(url)

def get_auth_url():
    return __auth_url__

class __Struct:
    def __init__(self, **entries): 
        self.__dict__.update(entries)

def login(username,password,library):
    """ Esegue il login e restituisce il token da utilizzare nelle chiamate successive"""
    data = {
        'username' : username,
        'password' : password,
        'library' : library
        }
        
    envelope = __make_call__(__auth_url__ + "login", data)

    return envelope.findtext("{%s}return" % __auth_ns__)

def create_burertss(token, chiave, dati_burertss, file):
    """Crea un nuovo documento di tipo BURERTSS.
    
    Argomenti:

    - token: il token restituito dalla chiamata di login
    - chiave: un oggetto od un dizionario che presenta i seguenti attributi:
               - anno
               - numero
               - legislazione
    - dati_burertss: un oggetto od un dizionario che presenta i seguenti attributi:
                      - data: Puo' essere di tipo datetime, date o time
                      - numerazione
    - file: un file-like object contenente il file rappresentato.
            Le funzioni necessarie per il corretto funzionamento della libreria sono:
            
             - file.name: il sistema documentale necessita una estensione. Assicurarsi pertanto che sia presente.
             - file.read()
               
    """

    if isinstance(dati_burertss, dict):
        dati_burertss = __Struct(**dati_burertss)

    if isinstance(chiave, dict):
        chiave = __Struct(**chiave)

    m = {}

    m['REGISTRO_PG'] = 'BURERTSS-%s' % chiave.legislazione.upper()
    m['NUMERO_SSBU'] = chiave.legislazione.upper()
    m['NUMERAZIONE_SSBU'] = str(dati_burertss.numerazione)
    
    data = dati_burertss.data
    oggetto = "Supplemento Speciale del BURERT n. %s del %s" % ( str(chiave.numero), data.strftime(__printable_date_format__))
    return __create_document(token, data, oggetto, chiave, m, file)

def create_burert(token, chiave, dati_burert, file):
    """Crea un nuovo documento di tipo BURERT.
    
    Argomenti:

    - token: il token restituito dalla chiamata di login
    - chiave: un oggetto od un dizionario che presenta i seguenti attributi:
               - anno
               - numero
               - legislazione
    - dati_burert: un oggetto od un dizionario che presenta i seguenti attributi:
                    - annata
                    - data: Puo' essere di tipo datetime, date o time
                    - numerazione
                    - parte
    - file: un file-like object contenente il file rappresentato.
            Le funzioni necessarie per il corretto funzionamento della libreria sono:
            
             - file.name: il sistema documentale necessita una estensione. Assicurarsi pertanto che sia presente.
             - file.read()

    """

    if isinstance(dati_burert, dict):
        dati_burert = __Struct(**dati_burert)

    if isinstance(chiave, dict):
        chiave = __Struct(**chiave)

    m = {}

    m['REGISTRO_PG'] = 'BURERT'

    m['ANNATA_BU'] = str(dati_burert.annata)
    m['NUMERAZIONE_BU'] = str(dati_burert.numerazione)
    m['PARTE_BU'] = str(dati_burert.parte)
    
    data = dati_burert.data
    oggetto = "BURERT n. %s del %s parte %s" % ( str(chiave.numero), data.strftime(__printable_date_format__), str(dati_burert.parte))

    return __create_document(token, data, oggetto, chiave, m, file)

def __create_document(token, data, oggetto, chiave, metadata, file):
        
    metadata['D_REGISTRAZ'] = data.strftime(__docer_date_format__)

    metadata['ANNO_PG'] = str(chiave.anno)
    metadata['NUM_PG'] = str(chiave.numero)
    metadata['OGGETTO_PG'] = oggetto

    # Costanti
    metadata['T_CONSERV'] = 'SOSTITUTIVA'
    metadata['STATO_CONSERV'] = '1'
    metadata['FLAG_CONSERV'] = 'S'
    metadata['STATO_PANTAREI'] = '4'
    metadata['USA_D_CO_CER'] = 'NO'
    
    # Proprieta'
    metadata['COD_ENTE'] = get_ente()
    metadata['COD_AOO'] = get_aoo()
    metadata['TYPE_ID'] = get_type_id()
    metadata['APP_VERSANTE'] = get_app_versante()

    filename = os.path.basename(file.name)
#    i = filename.index('.')
 #   filename = filename[:i] + filename[i:].upper()
    metadata['DOCNAME'] = filename.upper()

    root = etree.Element("{%s}createDocument" % __docer_ns__)
    
    token_elem = etree.Element("{%s}token" % __docer_ns__)
    token_elem.text = token
    root.append(token_elem)
    
    for d in metadata:
        elem = etree.Element("{%s}metadata" % __docer_ns__)
        key = etree.Element("{%s}key" % __sdk_ns__)
        key.text = d
        value = etree.Element("{%s}value" % __sdk_ns__)
        value.text = metadata[d]
        
        elem.append(key)
        elem.append(value)
        
        root.append(elem)

    file_elem = etree.Element("{%s}file" % __docer_ns__)
    file_elem.text = base64.b64encode(file.read())
    root.append(file_elem)

    request = urllib2.Request(__url__ + "createDocument", etree.tostring(root), {"Content-Type" : "application/xml; charset=UTF-8"})

    envelope = __call__(request)
    
    return envelope.findtext("{%s}return" % __docer_ns__)

def __make_call__(url, dict):
    data, headers = multipart_encode(dict)
    request = urllib2.Request(url, data, headers)

    return __call__(request)

def __call__(request):    
    try:
        response = urllib2.urlopen(request)
    except IOError, e:
        if hasattr(e, 'code'):
            raise Exception, __extract_error__(__parse_response__(e.read()))
        else:
            raise e
    else:
        return __parse_response__(response.read())

def __parse_response__(response):
    if response.startswith("--MIME"):
        response = response[response.index('\r\n\r\n'):response.rindex('--MIME')].strip()
    
    return etree.fromstring(response)

def __extract_error__(envelope):
    if envelope.tag == "faultstring":
        return envelope.text
    else:
        text = envelope.findtext("{%s}Text" % __soap_ns__)
        return text

if __name__ == '__main__':
    token = login("administrator", "obelix", "docarea")
    
    import datetime

    class obj:
        pass
    
    chiave = {}
    chiave['anno'] = 2012
    chiave['numero'] = 9
    
    burert = {}
    burert['annata'] = 2011
    burert['data'] = datetime.date.today()
    burert['numerazione'] = 1
    burert['numero'] = 12
    burert['parte'] = 14

    f = open("/home/lorenzo/Desktop/test.pdf.p7m", "rb")
    print create_burert(token, chiave, burert, f)
    f.close()
