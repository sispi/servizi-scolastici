package it.filippetti.sispi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import it.filippetti.sispi.model.Pagamento;

public interface PagamentoRepository extends CrudRepository<Pagamento, Long>, JpaSpecificationExecutor<Pagamento> {

	List<Pagamento> findByCfMinoreOrderById(String cfMinore);
}
