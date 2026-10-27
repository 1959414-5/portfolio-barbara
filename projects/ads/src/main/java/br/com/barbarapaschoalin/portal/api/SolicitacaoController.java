package br.com.barbarapaschoalin.portal.api;

import br.com.barbarapaschoalin.portal.domain.HistoricoStatus;
import br.com.barbarapaschoalin.portal.domain.Solicitacao;
import br.com.barbarapaschoalin.portal.domain.StatusSolicitacao;
import br.com.barbarapaschoalin.portal.service.SolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService service;

    public SolicitacaoController(SolicitacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Solicitacao> listar(@RequestParam(required = false) StatusSolicitacao status) {
        return service.listar(status);
    }

    @GetMapping("/{id}")
    public Solicitacao buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Solicitacao criar(@Valid @RequestBody CriarSolicitacaoRequest request) {
        return service.criar(request);
    }

    @PatchMapping("/{id}/status")
    public Solicitacao alterarStatus(@PathVariable Long id,
                                    @Valid @RequestBody AlterarStatusRequest request) {
        return service.alterarStatus(id, request.status(), request.observacao());
    }

    @GetMapping("/{id}/historico")
    public List<HistoricoResponse> historico(@PathVariable Long id) {
        return service.historico(id).stream().map(HistoricoResponse::from).toList();
    }
}
