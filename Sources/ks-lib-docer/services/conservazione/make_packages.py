import shlex, subprocess, os, shutil, zipfile

source_dir = "/home/lorenzo/workspaces/eclipse/"
dest_dir = '/home/lorenzo/Desktop/Conservazione'

clean_command = shlex.split("mvn clean")
install_command = shlex.split("mvn install")

projects = [
    #commons
    'KDMUtils',
    'Service-Commons',
    #docer
    'docer.sdk',
    'docer.provider.alfresco33ce',
    'docer.provider.eDocs',
    'docer.BusinessLogic',
    'docer.webservices',
    'DocER',
    #conservazione
    'conservazione'
    ]

wars = [
    ('DocER/target/docersystem.war',), 
    ('conservazione/conservazione.webservice/target/WSConservazione.war', ),
    ('conservazione/conservazione.batch/conservazione.batch.docerLib/target/docerlib-jar-with-dependencies.jar', 'docerlib.jar'),
    ('conservazione/conservazione.batch/conservazione.batch.conservazione/target/conservazione-jar-with-dependencies.jar', 'conservazione.jar'),
    ]

#readme = 'DocER/README'

folders = [
    'conservazione/conservazione.converter/src/main/resources/templates'	
    ]

for project in projects:
    proj_dir = os.path.join(source_dir, project)
    
    subprocess.check_call(clean_command, cwd=proj_dir)
    subprocess.check_call(install_command, cwd=proj_dir)

if os.path.exists(dest_dir):
    shutil.rmtree(dest_dir)
os.mkdir(dest_dir)

for war in wars:
    war_path = os.path.join(source_dir, war[0])
    if(len(war) > 1):
        dest_path = os.path.join(dest_dir, war[1])
    else:
        dest_path = dest_dir
    print "Copying %s to %s" % (war, dest_path)
    shutil.copy(war_path, dest_path)

for folder in folders:
    source_folder = os.path.join(source_dir, folder)
    target_folder = os.path.join(dest_dir, os.path.basename(folder))
    print "Copying %s to %s" % (folder, dest_dir)
    if os.path.exists(target_folder):
        shutil.rmtree(target_folder)

    shutil.copytree(source_folder, target_folder)

#readme_path = os.path.join(source_dir, readme)
#print "Copying %s to %s" % (readme, dest_dir)
#shutil.copy(readme_path, dest_dir)
    
archive = zipfile.ZipFile("%s.zip" % os.path.basename(dest_dir), "w", zipfile.ZIP_DEFLATED)
path_to_remove = dest_dir.replace(os.path.basename(dest_dir), '')
for root, dirs, files in os.walk(dest_dir):
    for d in dirs:
        archive.write(os.path.join(root,d), arcname=os.path.join(root.replace(path_to_remove, ''), d))
    for f in files:
        archive.write(os.path.join(root,f), arcname=os.path.join(root.replace(path_to_remove, ''), f))

shutil.rmtree(dest_dir)
