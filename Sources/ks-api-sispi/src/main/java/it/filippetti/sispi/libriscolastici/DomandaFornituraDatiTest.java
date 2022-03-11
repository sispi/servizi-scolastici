package it.filippetti.sispi.libriscolastici;

import java.util.ArrayList;
import java.util.List;

public class DomandaFornituraDatiTest {

	private DomandaFornituraDatiTest() {
	}

	public static List<DomandaFornituraBean> getListaAlunniTest() {
		final List<DomandaFornituraBean> result = new ArrayList<>();
		final ScuolaBean s1 = getScuola1();
		final ScuolaBean s2 = getScuola2();
		result.add(getEntitaTest("0", "DSTKRZ02P68G273I", "01/01/1980", s2));
		result.add(getEntitaTest("0", "CLARHM09E25G273S", "01/01/2010", s1));
		result.add(getEntitaTest("0", "DMRVTR06D12C351J", "05/02/2005", s1));
		result.add(getEntitaTest("0", "GRRLEA10S56A944W", "15/09/2002", s1));
		return result;
	}

	public static List<ScuolaBean> getListaScuoleTest() {
		final List<ScuolaBean> result = new ArrayList<>();
		result.add(getScuola1());
		result.add(getScuola2());
		return result;
	}

	private static ScuolaBean getScuola1() {
		final ScuolaBean s1 = new ScuolaBean();
		s1.setCap_scuola("90129");
		s1.setCivico_scuola("1");
		s1.setComune_scuola("Palermo");
		s1.setDenominazione_scuola("KIYOHARA O. e V. RAGUSA -* PARLATORE");
		s1.setId_scuola("1");
		s1.setIndirizzo_scuola("PIAZZA TURBA * EUCLIDE * GEN.");
		s1.setProvincia_scuola("Palermo");
		s1.setTelefono_scuola("091-486092");
		s1.setIstituzione_scolastica("Paritaria");
		return s1;
	}

	private static ScuolaBean getScuola2() {
		final ScuolaBean s2 = new ScuolaBean();
		s2.setCap_scuola("90129");
		s2.setCivico_scuola("2");
		s2.setComune_scuola("Palermo");
		s2.setDenominazione_scuola("TOPOLINO");
		s2.setId_scuola("2");
		s2.setIndirizzo_scuola("PIAZZA TURBA * EUCLIDE * GEN.");
		s2.setProvincia_scuola("Palermo");
		s2.setTelefono_scuola("091-486092");
		s2.setIstituzione_scolastica("Paritaria");
		return s2;
	}

	private static DomandaFornituraBean getEntitaTest(String esito, String codiceFiscale, String dataNascita,
			ScuolaBean scuola) {
		final DomandaFornituraBean result = new DomandaFornituraBean();
		result.setCap("CAP " + codiceFiscale);
		result.setCap_scuola(scuola.getCap_scuola());
		result.setCivico("CIVICO " + codiceFiscale);
		result.setCivico_scuola(scuola.getCivico_scuola());
		result.setClasse_scuola("CLASSE_SCUOLA " + codiceFiscale);
		result.setCodicefiscale(codiceFiscale);
		result.setCognome("COGNOME " + codiceFiscale);
		result.setComune("COMUNE " + codiceFiscale);
		result.setComune_scuola(scuola.getComune_scuola());
		result.setData_nascita(dataNascita);
		result.setDenominazione_scuola(scuola.getDenominazione_scuola());
		result.setEsito(esito);
		result.setIndirizzo("INDIRIZZO " + codiceFiscale);
		result.setIndirizzo_scuola(scuola.getIndirizzo_scuola());
		result.setLuogo_nascita("LUOGO_NASCITA " + codiceFiscale);
		result.setNome("NOME " + codiceFiscale);
		result.setProvincia_scuola(scuola.getProvincia_scuola());
		result.setTelefono_scuola(scuola.getTelefono_scuola());
		result.setScuola(scuola);
		return result;
	}
}
