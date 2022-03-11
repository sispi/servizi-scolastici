package it.filippetti.sispi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.filippetti.sispi.model.CentroInfanziaTariffe;

public interface CentroInfanziaTariffeRepository extends JpaRepository<CentroInfanziaTariffe, Long> {

	List<CentroInfanziaTariffe> findByDenominazioneAsiloAndCategoriaAndAnnoRiferimento(String denominazioneAsilo,
			String categoria, Integer annoRiferimento);

}
