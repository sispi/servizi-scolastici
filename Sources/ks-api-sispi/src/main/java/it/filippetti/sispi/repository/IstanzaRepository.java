package it.filippetti.sispi.repository;

import it.filippetti.sispi.model.Istanza;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IstanzaRepository extends JpaRepository<Istanza, Long> {

	Istanza findOneByConfermaNotificataAndCfContribuenteAndCfMinoreAndAnnoScolastico(String confermaNotificata,
			String cfContribuente, String cfMinore, String annoScolastico);

	Istanza findOneByConfermaNotificataAndCfMinoreAndAnnoScolastico(String confermaNotificata, String cfMinore,
			String annoScolastico);

	List<Istanza> findByCfContribuenteAndEsitoValutazioneAndAnnoScolasticoAndIdFamigliaNotNull(String cfContribuente,
			String esitoValutazione, String annoScolastico);

	List<Istanza> findByConfermaNotificataAndIdFamigliaAndAnnoScolastico(String confermaNotificata, Long idFamiglia,
			String annoScolastico);

	// List<Istanza> findByRicalcoloRate(String ricalcoloRate);

	List<Istanza> findByCfMinoreAndEsitoValutazioneAndConfermataIscrizioneAndAnnoScolastico(String cfMinore,
			String esitoValutazione, String confermataIscrizione, String annoScolastico);

	List<Istanza> findByCfMinoreAndEsitoValutazioneAndConfermataIscrizione(String cfMinore, String esitoValutazione,
			String confermataIscrizione);

	List<Istanza> findByIdFamigliaAndConfermaNotificataAndAnnoScolastico(Long idFamiglia, String confermaNotificata,
			String annoScolastico);

	List<Istanza> findByConfermataIscrizione(String confermataIscrizione);

}
