package it.filippetti.sispi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.filippetti.sispi.model.GiorniFrequenzaIstituto;

public interface GiorniFrequenzaIstitutoRepository extends JpaRepository<GiorniFrequenzaIstituto, Long> {

	List<GiorniFrequenzaIstituto> findByAnnoScolasticoOrderBySort(String annoScolastico);

}
