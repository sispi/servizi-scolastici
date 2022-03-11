package it.filippetti.sispi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.filippetti.sispi.model.DettaglioIscrizioneCentroInfanzia;

public interface DettaglioIscrizioneCentroInfanziaRepository
		extends JpaRepository<DettaglioIscrizioneCentroInfanzia, Long> {

	List<DettaglioIscrizioneCentroInfanzia> findAllByIscrizioneIdAndValoreRataNotNullOrderBySort(Long idIscrizione);

}
