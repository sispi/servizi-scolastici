package it.filippetti.psso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.filippetti.psso.model.TipoDieta;

public interface TipoDietaRepository extends JpaRepository<TipoDieta, String> {

}
