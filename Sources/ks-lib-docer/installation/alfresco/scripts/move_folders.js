logger.log("Starting up");

var FOLDERS_ROOT = "Cartelle";
var DOCUMENTS_ROOT = "Temporanei";

function findChildrenByType(node, shortType) {
    var children = [];
    for(var i=0; i<node.children.length; i++) {
        var child = node.children[i];
        if (child.typeShort == shortType) {
            children.push(child);
        }
    }

    return children;
}

function renameFolder(folder, name, description, deep) {
    folder.name = name;
    folder.properties["docarea:folderName"] = name;
    folder.properties["docarea:desFolder"] = description;
    folder.properties["docarea:folderId"] = folder.properties["sys:node-dbid"];
    folder.save();

    if (deep) {
        var children = findChildrenByType(folder, "cm:folder");
        for (var i=0; i<children.length; i++) {
            var child = children[i];
            child.properties["docarea:parentFolderId"] = folder.properties["docarea:folderId"];
            child.save();
        }
    }
}

function resetPermissions(folder, aoo) {
    var aooGroup = "GROUP_" + aoo.properties["docarea:codAoo"];
    //folder.removePermission("FullAccess");
    folder.setPermission("FullAccess", aooGroup);
    folder.setInheritsPermissions(false);
}

var docarea = companyhome.childByNamePath ("DOCAREA");
var enti = findChildrenByType(docarea, "docarea:ente");
for(var i=0; i<enti.length; i++) {
    var ente = enti[i];
    
    logger.log("Found ENTE: " + ente.name);

    var aooList = findChildrenByType(ente, "docarea:aoo");
    for (var j=0; j<aooList.length; j++) {
        var aoo = aooList[j];

        logger.log("Found AOO: " + aoo.name);

        var foldersRoot = aoo.childByNamePath(FOLDERS_ROOT);
        if (foldersRoot != null) {
            logger.log("Folders root is already in the correct position, skipping");
            resetPermissions(foldersRoot, aoo);
            foldersRoot.save();
        } else {
            foldersRoot = aoo.childByNamePath("DOCUMENTI/" + aoo.name);
            if (foldersRoot == null) {
                logger.log("Creating a new Folders root");
                foldersRoot = aoo.createNode(FOLDERS_ROOT, "cm:folder");
                foldersRoot.properties["docarea:codEnte"] = ente.properties["docarea:codEnte"];
                foldersRoot.properties["docarea:codAoo"] = aoo.properties["docarea:codAoo"];
            } else {
                logger.log("Moving Folders root: " + foldersRoot.name);
                foldersRoot.move(aoo);
            }

            resetPermissions(foldersRoot, aoo);
            renameFolder(foldersRoot, FOLDERS_ROOT, "Folder root delle Folder", true);
        }

        var documentsRoot = aoo.childByNamePath(DOCUMENTS_ROOT);
        if (documentsRoot != null) {
            logger.log("Documents root is already in the correct position, skipping");
            resetPermissions(documentsRoot, aoo);
            documentsRoot.save();
        } else {
            logger.log("Copying Folders root: " + foldersRoot.name);
            documentsRoot = foldersRoot.copy(aoo);
            resetPermissions(documentsRoot, aoo);
            renameFolder(documentsRoot, DOCUMENTS_ROOT, "Folder root dei Documenti", false);
        }
    }
}
