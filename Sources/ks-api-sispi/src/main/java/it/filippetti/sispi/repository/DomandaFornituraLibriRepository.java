package it.filippetti.sispi.repository;

import it.filippetti.sispi.model.DomandaFornituraLibri;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomandaFornituraLibriRepository extends JpaRepository<DomandaFornituraLibri, Long> {

	List<DomandaFornituraLibri> findByCfMinoreAndAnnoScolastico(String cfMinore, String annoScolastico);

	List<DomandaFornituraLibri> findByNumeroProtocolloAndAnnoProtocolloAndCfRichiedente(String numeroProtocollo,
			String annoProtocollo, String cfRichiedente);
}
