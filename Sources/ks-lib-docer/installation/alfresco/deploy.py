import os
import shutil
import sys

import xml.etree.ElementTree as etree

source_dir = os.path.dirname(__file__)

cache_file = os.path.expanduser("~/.deploy")
cached_dir = None
if os.path.exists(cache_file):
    with open(cache_file) as f:
        cached_dir = f.read().strip()
    alf_webapp = raw_input("Inserire la cartella della webapp di alfresco [%s]:\n" % cached_dir)
    if not alf_webapp:
        alf_webapp = cached_dir
else:
    alf_webapp = raw_input("Inserire la cartella della webapp di alfresco\n")

if not os.path.exists(alf_webapp):
    print "La cartella %s non esiste" % alf_webapp
    sys.exit(1)
    
if not os.path.isdir(alf_webapp):
    print "%s non e' una cartella" % alf_webapp
    sys.exit(1)

if not os.path.exists(os.path.join(alf_webapp, "WEB-INF")):
    print "%s non e' una valida webapp" % alf_webapp
    sys.exit(1)

with open(cache_file, "wb") as f:
    f.write(alf_webapp)

def copy_contents(source, target):
    for file in os.listdir(source):
        full_path = os.path.join(source, file)
        print "Copying %s to %s..." % (full_path, target)
        shutil.copy(full_path, target)


source = os.path.join(source_dir, "extension")
target = os.path.join(alf_webapp, "WEB-INF/classes/alfresco/extension")
if not os.path.exists(target):
    print "Creating %s" % target
    os.makedirs(target)

copy_contents(source, target)

source = os.path.join(source_dir, "getDocumentsWebScript/getDocumentsWebScript-1.3.u.jar")
target = os.path.join(alf_webapp, "WEB-INF/lib")
print "Copying %s to %s..." % (source, target)
shutil.copy(source,target)

source = os.path.join(source_dir, "getDocumentsWebScript/getdocuments.post.desc.xml")
target = os.path.join(alf_webapp, "WEB-INF/classes/alfresco/templates/webscripts/it/kdm/search")
if not os.path.exists(target):
    print "Creating %s" % target
    os.makedirs(target)
print "Copying %s to %s..." % (source, target)
shutil.copy(source,target)



source = os.path.join(source_dir, "moveToSpaceWebScript/moveToSpaceWebScript-1.3.a.jar")
target = os.path.join(alf_webapp, "WEB-INF/lib")
print "Copying %s to %s..." % (source, target)
shutil.copy(source,target)

source = os.path.join(source_dir, "moveToSpaceWebScript/movetospace.post.desc.xml")
target = os.path.join(alf_webapp, "WEB-INF/classes/alfresco/templates/webscripts/it/kdm/move")
if not os.path.exists(target):
    print "Creating %s" % target
    os.makedirs(target)
print "Copying %s to %s..." % (source, target)
shutil.copy(source,target)


source = os.path.join(source_dir, "createGroupWebScript/createGroupWebScript-1.3.a.jar")
target = os.path.join(alf_webapp, "WEB-INF/lib")
print "Copying %s to %s..." % (source, target)
shutil.copy(source,target)

source = os.path.join(source_dir, "createGroupWebScript/creategroup.post.desc.xml")
target = os.path.join(alf_webapp, "WEB-INF/classes/alfresco/templates/webscripts/it/kdm/create")
if not os.path.exists(target):
    print "Creating %s" % target
    os.makedirs(target)
print "Copying %s to %s..." % (source, target)
shutil.copy(source,target)
    
    
source = os.path.join(source_dir, "model")
target = os.path.join(alf_webapp, "WEB-INF/classes/alfresco/model")
copy_contents(source, target)

source = os.path.join(source_dir, "icons")
target = os.path.join(alf_webapp, "images/icons")
copy_contents(source, target)

print 'Avviare alfresco, navigare su "Company Home".  Cliccare su'
print 'create -> Create Space e inserire "DOCAREA" (senza le ") in Name e Titolo.'
