package it.filippetti.sispi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.filippetti.sispi.model.IscrizioneCentroInfanzia;

public interface IscrizioneCentroInfanziaRepository extends JpaRepository<IscrizioneCentroInfanzia, Long> {

	Optional<IscrizioneCentroInfanzia> findOneByConfermaNotificataAndCfMinoreAndAnnoScolastico(
			String confermaNotificata, String cfMinore, String annoScolastico);

	List<IscrizioneCentroInfanzia> findByAnnoScolastico(String annoScolastico);

	List<IscrizioneCentroInfanzia> findByCfRichiedenteAndConfermaNotificataAndAnnoScolastico(String cfRichiedente,
			String notificataIscrizione, String annoScolastico);

	List<IscrizioneCentroInfanzia> findByCfMinoreAndEsitoValutazioneAndConfermataIscrizioneAndAnnoScolastico(
			String cfMinore, String esitoValutazione, String confermataIscrizione, String annoScolastico);

	List<IscrizioneCentroInfanzia> findByCfMinoreAndAnnoScolastico(String cfMinore, String annoScolastico);

}
