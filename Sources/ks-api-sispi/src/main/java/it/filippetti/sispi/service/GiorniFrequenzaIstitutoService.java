package it.filippetti.sispi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import it.filippetti.sispi.model.GiorniFrequenzaIstituto;
import it.filippetti.sispi.pagamentoretta.DettaglioRataBean;
import it.filippetti.sispi.pagamentoretta.RataUtils;
import it.filippetti.sispi.repository.GiorniFrequenzaIstitutoRepository;

@Service
@Validated
@Transactional
public class GiorniFrequenzaIstitutoService {

	@Autowired
	private GiorniFrequenzaIstitutoRepository giorniFrequenzaIstitutoRepository;

	public List<GiorniFrequenzaIstituto> inserisciDatiPerAnnoScolastico(String annoScolastico) {
		final List<DettaglioRataBean> rateDefault = RataUtils.getListaRateDefault();
		final List<GiorniFrequenzaIstituto> result = new ArrayList<GiorniFrequenzaIstituto>();
		for (DettaglioRataBean dettaglioRataBean : rateDefault) {
			final GiorniFrequenzaIstituto giorniFrequenza = getData(dettaglioRataBean, annoScolastico);
			result.add(giorniFrequenzaIstitutoRepository.save(giorniFrequenza));
		}
		return result;
	}

	private GiorniFrequenzaIstituto getData(DettaglioRataBean dettaglioRataBean, String annoScolastico) {
		final GiorniFrequenzaIstituto frequenzaIstituto = new GiorniFrequenzaIstituto();
		frequenzaIstituto.setAnnoScolastico(annoScolastico);
		frequenzaIstituto.setDescrizione(RataUtils.getDescizioneSoloMese(dettaglioRataBean.getDescrizione()));
		frequenzaIstituto.setGiorniFrequenza(IscrizioneConstants.GIORNI_FREQUENZA_DEFAULT);
		frequenzaIstituto.setSort(dettaglioRataBean.getOrdinamento());
		return frequenzaIstituto;
	}

	public List<GiorniFrequenzaIstituto> getByAnnoScolastico(String annoScolastico) {
		return giorniFrequenzaIstitutoRepository.findByAnnoScolasticoOrderBySort(annoScolastico);
	}

	public List<GiorniFrequenzaIstituto> getAll() {
		final Sort sort = Sort.by("sort").ascending();
		return giorniFrequenzaIstitutoRepository.findAll(sort);
	}
}
