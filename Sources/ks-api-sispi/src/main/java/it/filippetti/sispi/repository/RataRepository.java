package it.filippetti.sispi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.filippetti.sispi.model.Rata;

public interface RataRepository extends JpaRepository<Rata, Long> {

//	@Query("select r from Rata r left join r.pagamenti p on p.cfMinore = :cfMinore where p.id is null")
//	Rata findProssimaRata(String cfMinore);

	@Query("SELECT r FROM Rata r LEFT JOIN Pagamento p ON p.cfMinore = r.cfIscritto AND r.id = p.rata WHERE r.cfIscritto = :cfIscritto AND r.annoScolastico = :annoScolastico AND p.id is null ORDER BY r.sort")
	List<Rata> findProssimaRataByCfIscrittoAndAnnoScolastico(String cfIscritto, String annoScolastico);

	List<Rata> findByCfIscrittoAndAnnoScolastico(String cfIscritto, String annoScolastico);

	List<Rata> findByCfIscrittoOrderByAnnoScolasticoAscSortAsc(String cfIscritto);

	List<Rata> findByCfIscrittoAndAnnoScolasticoAndSort(String cfIscritto, String annoScolastico, Integer sort);

	@Query("SELECT r FROM Rata r JOIN Pagamento p ON p.cfMinore = r.cfIscritto AND r.id = p.rata WHERE r.cfIscritto = :cfIscritto AND r.annoScolastico = :annoScolastico ORDER BY r.sort")
	List<Rata> findRateWithPagamentoByCfIscritto(String cfIscritto, String annoScolastico);
}
