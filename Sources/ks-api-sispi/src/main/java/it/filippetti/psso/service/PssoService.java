package it.filippetti.psso.service;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import it.filippetti.psso.model.ScuolaAlunno;
import it.filippetti.psso.model.TipoDieta;
import it.filippetti.psso.model.TipoDietaSpeciale;
import it.filippetti.psso.model.TipoRichiedente;
import it.filippetti.psso.repository.ScuolaAlunnoRepository;
import it.filippetti.psso.repository.TipoDietaRepository;
import it.filippetti.psso.repository.TipoDietaSpecialeRepository;
import it.filippetti.psso.repository.TipoRichiedenteRepository;

@Service
@Validated
public class PssoService {

	private static final Logger logger = LoggerFactory.getLogger(PssoService.class);

	@Autowired
	private TipoDietaRepository tipoDietaRepository;

	@Autowired
	private TipoDietaSpecialeRepository tipoDietaSpecialeRepository;

	@Autowired
	private TipoRichiedenteRepository tipoRichiedenteRepository;

	@Autowired
	private ScuolaAlunnoRepository scuolaAlunnoRepository;

	public List<TipoDieta> getListaTipoDieta() {
		logger.debug("getListaTipoDieta");
		final Sort sort = Sort.by("id").ascending();
		return tipoDietaRepository.findAll(sort);
	}

	public List<TipoDietaSpeciale> getListaTipoDietaSpeciale() {
		logger.debug("getListaTipoDietaSpeciale");
		final Sort sort = Sort.by("codice").ascending();
		return tipoDietaSpecialeRepository.findAll(sort);
	}

	public List<TipoRichiedente> getListaTipoRichiedente() {
		logger.debug("getListaTipoRichiedente");
		final Sort sort = Sort.by("id").ascending();
		return tipoRichiedenteRepository.findAll(sort);
	}

	public List<ScuolaAlunno> findByCodiceFiscaleAlunno(@NotBlank String codiceFiscaleAlunno) {
		logger.debug("findByCodiceFiscaleAlunno: {}", codiceFiscaleAlunno);
		return scuolaAlunnoRepository.findByCodiceFiscaleAlunno(codiceFiscaleAlunno.toUpperCase());
	}
}
