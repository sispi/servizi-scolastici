package it.filippetti.sispi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.filippetti.sispi.model.IscrizioniLog;

public interface IscrizioniLogRepository extends JpaRepository<IscrizioniLog, Long> {

	List<IscrizioniLog> findByIdIstanzaIscrizioneOrderByIdIstanzaIscrizioneAscDataModificaDesc(String idIstanza);

//	List<IscrizioniLog> findAllByOrderByIdIstanzaIscrizioneAscDataModificaDesc();

}
