package br.com.ramos.forumhub.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<br.com.ramos.forumhub.domain.topico.Topico, Long> {

    boolean existsByTituloAndMensagem(String titulo, String mensagem);
}

