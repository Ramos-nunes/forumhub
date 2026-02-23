package br.com.ramos.forumhub.domain.controller;

import br.com.ramos.forumhub.domain.topico.DadosAtualizacaoTopico;
import br.com.ramos.forumhub.domain.topico.DadosCadastroTopico;
import br.com.ramos.forumhub.domain.topico.DadosDetalhamentoTopico;
import br.com.ramos.forumhub.domain.topico.DadosListagemTopico;
import br.com.ramos.forumhub.domain.topico.StatusTopico;
import br.com.ramos.forumhub.domain.topico.Topico;
import br.com.ramos.forumhub.domain.topico.TopicoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository repository;

    public TopicoController(TopicoRepository repository) {
        this.repository = repository;
    }

    // CADASTRO DE TÓPICO - POST /topicos
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid DadosCadastroTopico dados) {

        // verifica se já existe tópico com mesmo título e mensagem
        if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            return ResponseEntity.badRequest().body("Tópico já existente com mesmo título e mensagem.");
        }

        var topico = new Topico();
        topico.setTitulo(dados.titulo());
        topico.setMensagem(dados.mensagem());
        topico.setAutor(dados.autor());
        topico.setCurso(dados.curso());
        topico.setStatus(StatusTopico.NAO_RESPONDIDO);
        topico.setDataCriacao(LocalDateTime.now());

        repository.save(topico);

        return ResponseEntity.ok(topico);
    }

    // LISTAGEM COM PAGINAÇÃO - GET /topicos
    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(
            @PageableDefault(size = 10, sort = "dataCriacao") Pageable paginacao) {

        var page = repository.findAll(paginacao)
                .map(DadosListagemTopico::new);

        return ResponseEntity.ok(page);
    }

    // DETALHAMENTO POR ID - GET /topicos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> detalhar(@PathVariable Long id) {
        var topicoOptional = repository.findById(id);

        if (topicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var dto = new DadosDetalhamentoTopico(topicoOptional.get());
        return ResponseEntity.ok(dto);
    }
    public ResponseEntity<DadosDetalhamentoTopico> cadastrar(
            @RequestBody @Valid DadosCadastroTopico dados,
            UriComponentsBuilder uriBuilder) {

        if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Já existe um tópico com o mesmo título e mensagem.");
        }

        var topico = new Topico();
        topico.setTitulo(dados.titulo());
        topico.setMensagem(dados.mensagem());
        topico.setAutor(dados.autor());
        topico.setCurso(dados.curso());

        repository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}")
                .buildAndExpand(topico.getId())
                .toUri();

        return ResponseEntity.created(uri)
                .body(new DadosDetalhamentoTopico(topico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoTopico dados) {

        var optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Tópico não encontrado com o id: " + id);
        }

        var topico = optional.get();
        topico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        var optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204, típico para delete
    }

}
