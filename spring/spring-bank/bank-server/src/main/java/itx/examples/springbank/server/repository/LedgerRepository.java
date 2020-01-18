package itx.examples.springbank.server.repository;


import itx.examples.springbank.server.repository.model.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerRepository extends JpaRepository<ClientEntity, Long> {
}
