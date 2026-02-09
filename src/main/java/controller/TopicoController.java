package br.com.ramos.forumhub.controller;

import br.com.ramos.forumhub.domain.topico.DadosCadastroTopico;
import br.com.ramos.forumhub.domain.topico.Topico;
import br.com.ramos.forumhub.domain.topico.TopicoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository repository;

    public TopicoController(TopicoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid DadosCadastroTopico dados) {

        if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            return ResponseEntity.badRequest().body("Tópico já existente com mesmo título e mensagem.");
        }

        var topico = new Topico();
        topico.setTitulo(dados.titulo());
        topico.setMensagem(dados.mensagem());
        topico.setAutor(dados.autor());
        topico.setCurso(dados.curso());

        var salvo = repository.save(topico);
        return ResponseEntity.ok(salvo);
    }
}
