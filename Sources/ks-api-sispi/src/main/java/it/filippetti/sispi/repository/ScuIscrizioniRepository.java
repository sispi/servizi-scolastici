package it.filippetti.sispi.repository;

import it.filippetti.sispi.model.ScuIscrizioni;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScuIscrizioniRepository extends JpaRepository<ScuIscrizioni, String> {

	List<ScuIscrizioni> findByTipoRichiestaInAndIseeNotNullAndCfMinoreNotNull(Collection<String> tipiRichiesta);

	List<ScuIscrizioni> findByTipoRichiestaInAndIseeNotNullAndCfMinore(Collection<String> tipiRichiesta,
			String cfMinore);
}
