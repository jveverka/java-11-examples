package itx.examples.springbank.server.repository;


import itx.examples.springbank.server.repository.model.LedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<LedgerEntity, Long> {
}
