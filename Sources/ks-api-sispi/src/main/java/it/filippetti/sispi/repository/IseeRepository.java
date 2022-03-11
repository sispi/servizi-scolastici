package it.filippetti.sispi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.filippetti.sispi.model.Isee;

public interface IseeRepository extends JpaRepository<Isee, Long> {

	List<Isee> findByCodiceFiscaleOrderByDataInizioValiditaDesc(String codiceFiscale);

	@Query("SELECT i.codiceFiscale FROM Isee AS i WHERE i.codiceFiscale like %?1% GROUP BY i.codiceFiscale")
	List<String> findByCodiceFiscaleContains(String codiceFiscale);

}
