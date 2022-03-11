package it.filippetti.sispi.service;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import it.filippetti.sispi.model.FasciaIstituto;
import it.filippetti.sispi.model.Istanza;
import it.filippetti.sispi.model.Pagamento;
import it.filippetti.sispi.model.Rata;
import it.filippetti.sispi.repository.FasciaIstitutoRepository;
import it.filippetti.sispi.repository.IstanzaRepository;
import it.filippetti.sispi.repository.PagamentoRepository;
import it.filippetti.sispi.repository.RataRepository;

@Service
@Transactional
@Validated
public class IstanzaService {

	private static final Logger logger = LoggerFactory.getLogger(IstanzaService.class);

	@Autowired
	private IstanzaRepository istanzaRepository;

	@Autowired
	private FasciaIstitutoRepository fasciaIstitutoRepository;

	@Autowired
	private RataRepository rataRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	public Istanza aggiornaIstanza(@Valid Istanza datiIstanza) {
		if (datiIstanza.getId() != null) {
			return updateIstanza(datiIstanza);
		} else {
			return insertIstanza(datiIstanza);
		}
	}

	private Istanza insertIstanza(Istanza datiIstanza) {
		return istanzaRepository.save(datiIstanza);
	}

	private Istanza updateIstanza(Istanza datiIstanza) {
		logger.debug("Update istanza ID: {}", datiIstanza.getId());
		Istanza result = istanzaRepository.findById(datiIstanza.getId()).orElse(null);
		if (result == null) {
			throw new ServiceException("Id istanza non trovato: " + datiIstanza.getId());
		}
		String cfMinoreModificato = null;
		if (!result.getCfMinore().equals(datiIstanza.getCfMinore())) {
			cfMinoreModificato = result.getCfMinore();
		}
		BeanUtils.copyProperties(datiIstanza, result);
		result = istanzaRepository.save(result);
		if (cfMinoreModificato != null) {
			final List<FasciaIstituto> listaFasciaIstituto = fasciaIstitutoRepository
					.findByCodiceFiscaleAndAnnoScolasticoOrderBySort(cfMinoreModificato, result.getAnnoScolastico());
			for (final FasciaIstituto fasciaIstituto : listaFasciaIstituto) {
				fasciaIstituto.setCodiceFiscale(result.getCfMinore());
				fasciaIstitutoRepository.save(fasciaIstituto);
			}

			final List<Rata> listaRate = rataRepository.findByCfIscrittoAndAnnoScolastico(cfMinoreModificato,
					result.getAnnoScolastico());

			for (final Rata rata : listaRate) {
				rata.setCfIscritto(result.getCfMinore());
				rataRepository.save(rata);
			}

			final List<Pagamento> listaPagamenti = pagamentoRepository.findByCfMinoreOrderById(cfMinoreModificato);
			for (final Pagamento pagamento : listaPagamenti) {
				pagamento.setCfMinore(result.getCfMinore());
				pagamentoRepository.save(pagamento);
			}
		}
		return result;
	}

	public List<Istanza> getListaIstanzaConfermate(String cfMinore, String annoScolastico) {
		return istanzaRepository.findByCfMinoreAndEsitoValutazioneAndConfermataIscrizioneAndAnnoScolastico(
				cfMinore.toUpperCase(), IscrizioneConstants.ESITO_AMMESSO, IscrizioneConstants.CONFERMATA_ISCRIZIONE_SI,
				annoScolastico);
	}

	public List<Istanza> getListaIstanzaConfermate() {
		return istanzaRepository.findByConfermataIscrizione(IscrizioneConstants.CONFERMATA_ISCRIZIONE_SI);
	}

	public Istanza getIstanzaNotificata(String cfMinore, String annoScolastico) {
		return istanzaRepository.findOneByConfermaNotificataAndCfMinoreAndAnnoScolastico(
				IscrizioneConstants.NOTIFICATA_ISCRIZIONE_SI, cfMinore, annoScolastico);
	}
}
