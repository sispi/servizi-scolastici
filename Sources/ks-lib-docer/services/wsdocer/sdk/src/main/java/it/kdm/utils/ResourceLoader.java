package it.kdm.utils;

import java.io.File;
import java.net.URL;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;

public class ResourceLoader {
	public static File openFile(URL url) throws Exception {
		File file;
		
		if(url.getProtocol().equals("vfs")) {
			VirtualFile virtualFile = VFS.getChild(VFSUtils.toURI(url));
			file = virtualFile.getPhysicalFile();
		} else {
			file = new File(url.toURI());
		}
		
		return file;
	}
}
