package br.com.barbarapaschoalin.portal.repository;

import br.com.barbarapaschoalin.portal.domain.Solicitacao;
import br.com.barbarapaschoalin.portal.domain.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findAllByOrderByCriadaEmDesc();
    List<Solicitacao> findByStatusOrderByCriadaEmDesc(StatusSolicitacao status);
}
