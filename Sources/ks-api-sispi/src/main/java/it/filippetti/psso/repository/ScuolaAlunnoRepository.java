package it.filippetti.psso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.filippetti.psso.model.ScuolaAlunno;

public interface ScuolaAlunnoRepository extends JpaRepository<ScuolaAlunno, String> {

	List<ScuolaAlunno> findByCodiceFiscaleAlunno(String codiceFiscaleAlunno);

}
