package it.kdm.docer.conservazione.provider.parer;

import it.eng.parer.ccd.lib.IParerLib;
import it.eng.parer.ws.versamento.dto.FileBinario;
import it.eng.parer.ws.xml.versReq.UnitaDocAggAllegati;
import it.eng.parer.ws.xml.versReq.UnitaDocumentaria;
import it.eng.parer.ws.xml.versResp.EsitoVersAggAllegati;
import it.eng.parer.ws.xml.versResp.EsitoVersamento;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.exolab.castor.xml.Marshaller;

public class ParerLibStub implements IParerLib {

	private static final String UD_TEMPLATE = "%d-unitaDocumentaria.xml";
	private static final String FB_TEMPLATE = "%d-fileBinario-%d.xml";

	private File tmpDir;

	public File getTmpDir() {
		return tmpDir;
	}

	public void setTmpDir(File tmpDir) throws IOException {
		this.tmpDir = tmpDir;
		if(!tmpDir.exists()) {
			FileUtils.forceMkdir(tmpDir);
		}
	}

	public EsitoVersamento versamento(String loginname, String password,
			UnitaDocumentaria versamento, Vector<FileBinario> fileBinari)
			throws Exception {

		EsitoVersamento esito = EsitoVersamento
				.unmarshal(new InputStreamReader(this.getClass()
						.getResourceAsStream("/response.xml")));
		esito.setDataVersamento(new Date());

		Date d = new Date();

		File file = new File(this.tmpDir, String.format(UD_TEMPLATE,
				d.getTime()));
		this.dump(versamento, file);

		for (FileBinario fileBinario : fileBinari) {
			file = new File(this.tmpDir, String.format(FB_TEMPLATE,
					d.getTime(), fileBinari.indexOf(fileBinario)));
			this.dump(fileBinario, file);
		}

		return esito;
	}

	private void dump(Object obj, File targetFile) throws Exception {
		FileWriter writer = new FileWriter(targetFile);
		Marshaller marshaller = new Marshaller(writer);
		marshaller.marshal(obj);
		writer.close();
	}

	@Override
	public EsitoVersAggAllegati modificaMetadati(String username, String password,
			UnitaDocAggAllegati ud) throws Exception {
		EsitoVersAggAllegati esito = EsitoVersAggAllegati
				.unmarshal(new InputStreamReader(this.getClass()
						.getResourceAsStream("/response.xml")));
		esito.setDataVersamento(new Date());

		Date d = new Date();

		File file = new File(this.tmpDir, String.format(UD_TEMPLATE,
				d.getTime()));
		this.dump(ud, file);
		
		return esito;
	}

	@Override
	public EsitoVersAggAllegati aggiungiDocumento(String username,
			String password, UnitaDocAggAllegati ud, FileBinario file)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
