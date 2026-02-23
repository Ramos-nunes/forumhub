package br.com.ramos.forumhub.domain.controller;

import br.com.ramos.forumhub.domain.usuario.DadosAtualizacaoUsuario;
import br.com.ramos.forumhub.domain.usuario.DadosCadastroUsuario;
import br.com.ramos.forumhub.domain.usuario.DadosDetalhamentoUsuario;
import br.com.ramos.forumhub.domain.usuario.DadosListagemUsuario;
import br.com.ramos.forumhub.domain.usuario.Usuario;
import br.com.ramos.forumhub.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // POST /usuarios – cadastrar novo usuário
    // Rota pública (liberada no SecurityConfigurations)
    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid DadosCadastroUsuario dados) {

        // Verifica se já existe usuário com o mesmo email antes de salvar
        // Evita erro de chave única do PostgreSQL (500) e retorna 409 amigável
        if (repository.findByEmail(dados.email()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Já existe um usuário cadastrado com o email: " + dados.email());
        }

        // Cria o usuário com a senha já criptografada via BCrypt
        var usuario = new Usuario(
                dados.nome(),
                dados.email(),
                passwordEncoder.encode(dados.senha())
        );

        repository.save(usuario);

        // Retorna 201 Created com os dados do usuário criado (sem senha)
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new DadosDetalhamentoUsuario(usuario));
    }

    // GET /usuarios – listar todos os usuários com paginação
    // Rota protegida (exige token JWT)
    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable paginacao) {

        var page = repository.findAll(paginacao)
                .map(DadosListagemUsuario::new);

        return ResponseEntity.ok(page);
    }

    // GET /usuarios/{id} – detalhar um usuário específico pelo ID
    // Rota protegida (exige token JWT)
    @GetMapping("/{id}")
    public ResponseEntity<?> detalhar(@PathVariable Long id) {

        var optional = repository.findById(id);

        // Retorna 404 se o usuário não for encontrado
        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado com o id: " + id);
        }

        return ResponseEntity.ok(new DadosDetalhamentoUsuario(optional.get()));
    }

    // PUT /usuarios/{id} – atualizar nome e/ou senha de um usuário
    // Rota protegida (exige token JWT)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoUsuario dados) {

        var optional = repository.findById(id);

        // Retorna 404 se o usuário não for encontrado
        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado com o id: " + id);
        }

        var usuario = optional.get();

        // Atualiza o nome apenas se vier preenchido
        if (dados.nome() != null && !dados.nome().isBlank()) {
            usuario.setNome(dados.nome());
        }

        // Atualiza a senha apenas se vier preenchida, criptografando com BCrypt
        if (dados.senha() != null && !dados.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dados.senha()));
        }

        repository.save(usuario);

        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

    // DELETE /usuarios/{id} – excluir um usuário pelo ID
    // Rota protegida (exige token JWT)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        var optional = repository.findById(id);

        // Retorna 404 se o usuário não for encontrado
        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado com o id: " + id);
        }

        repository.deleteById(id);

        // 204 No Content – padrão para DELETE bem-sucedido
        return ResponseEntity.noContent().build();
    }
}

