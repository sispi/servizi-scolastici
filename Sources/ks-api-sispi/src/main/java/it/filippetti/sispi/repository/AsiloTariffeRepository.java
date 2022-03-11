package it.filippetti.sispi.repository;

import it.filippetti.sispi.model.AsiloTariffe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsiloTariffeRepository extends JpaRepository<AsiloTariffe, Long> {

	List<AsiloTariffe> findByDenominazioneAsiloAndDescrizioneCategoriaAndAnnoRiferimento(String denominazioneAsilo,
			String descrizioneCategoria, Integer annoRiferimento);
}
