package br.com.barbarapaschoalin.portal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SolicitacaoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveListarSolicitacoesDeDemonstracao() throws Exception {
        mockMvc.perform(get("/api/solicitacoes"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveRecusarCadastroSemTitulo() throws Exception {
        mockMvc.perform(post("/api/solicitacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "titulo": "",
                      "descricao": "Descrição de teste",
                      "solicitante": "Pessoa Sintética",
                      "departamento": "Teste",
                      "prioridade": "MEDIA"
                    }
                    """))
            .andExpect(status().isBadRequest());
    }
}
