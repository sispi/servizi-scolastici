package it.filippetti.sispi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.filippetti.sispi.model.FasciaIstituto;

public interface FasciaIstitutoRepository extends JpaRepository<FasciaIstituto, Long> {

	List<FasciaIstituto> findByCodiceFiscaleAndAnnoScolasticoOrderBySort(String codiceFiscale, String annoScolastico);

	List<FasciaIstituto> findByAnnoScolasticoAndSortAndNomeIstitutoAndFasciaAndFasciaOraria(String annoScolastico,
			Integer sort, String nomeIstituto, String fascia, String fasciaOraria);
}
