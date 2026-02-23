package br.com.ramos.forumhub.domain.usuario;

import jakarta.validation.constraints.Size;

public record DadosAtualizacaoUsuario(

        String nome,

        @Size(min = 6)
        String senha
) { }
