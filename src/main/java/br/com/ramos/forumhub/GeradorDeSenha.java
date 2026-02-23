package br.com.ramos.forumhub;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorDeSenha {

    public static void main(String[] args) {
        var encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("123456"); // senha em texto
        System.out.println(hash);
    }
}
