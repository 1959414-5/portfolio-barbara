package br.com.barbarapaschoalin.portal.repository;

import br.com.barbarapaschoalin.portal.domain.HistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, Long> {
    List<HistoricoStatus> findBySolicitacaoIdOrderByAlteradoEmDesc(Long solicitacaoId);
}
