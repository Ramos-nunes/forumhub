# FórumHub — Challenge Back End | Oracle Next Education + Alura

API REST desenvolvida como solução do **Challenge Back End FórumHub**, proposto pelo programa
**Oracle Next Education (ONE)** em parceria com a **Alura**, dentro da trilha
*Praticando Spring Framework*.

---

## Sobre o desafio

O FórumHub replica o funcionamento do fórum da Alura no nível do back end.
O objetivo é construir uma API REST completa que permita a usuários autenticados
criar, listar, detalhar, atualizar e excluir tópicos — o famoso **CRUD** — com
validações de negócio e controle de acesso via token JWT.

---

## Funcionalidades obrigatórias implementadas

Todos os itens exigidos pelo challenge foram implementados:

### 1. CRUD completo de tópicos

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/topicos` | Cadastrar novo tópico |
| GET | `/topicos` | Listar todos os tópicos (paginado) |
| GET | `/topicos/{id}` | Detalhar um tópico específico |
| PUT | `/topicos/{id}` | Atualizar um tópico |
| DELETE | `/topicos/{id}` | Excluir um tópico |

### 2. Regras de negócio validadas

- Todos os campos do tópico são obrigatórios (`titulo`, `mensagem`, `autor`, `curso`).
- A API **não permite tópicos duplicados**: bloqueia cadastro com mesmo título e mensagem.
- A `dataCriacao` é preenchida automaticamente pelo sistema (`LocalDateTime.now()`), não pelo usuário.
- O `status` do tópico é definido automaticamente como `NAO_RESPONDIDO` na criação.

### 3. Banco de dados relacional

- **PostgreSQL** como banco de dados.
- Integração via **Spring Data JPA / Hibernate**.
- Migrations SQL em `src/main/resources/db/migration`:
  - `V1__create-table-usuarios.sql`
  - `V2__create-table-topicos.sql`

### 4. Autenticação e autorização com Spring Security + JWT

- Somente usuários cadastrados e autenticados podem criar, atualizar ou excluir tópicos.
- Login via `POST /login` retorna token JWT do tipo **Bearer**.
- Token validado em cada requisição pelo `SecurityFilter`.
- Senhas armazenadas com hash **BCrypt** — nunca em texto plano.
- Rotas públicas: `POST /login` e `POST /usuarios`.
- Todas as demais rotas exigem token válido no header `Authorization`.

### 5. API REST com boas práticas

- Códigos HTTP corretos: `201 Created`, `200 OK`, `204 No Content`, `400 Bad Request`,
  `404 Not Found`, `409 Conflict`.
- Uso de DTOs (Records Java) para entrada e saída de dados.
- Paginação com `@PageableDefault` na listagem de tópicos (10 por página, ordenado por `dataCriacao`).
- Separação clara de responsabilidades: controllers, services, repositories, DTOs, entidades e segurança.

---

## Funcionalidades opcionais implementadas

O challenge disponibiliza itens opcionais para enriquecer o projeto.
Os seguintes foram implementados:

### Opcional 1 — Outras rotas: `/usuarios` (Card 17 do Trello)

Além do CRUD de tópicos, foi implementado um **CRUD completo de usuários**,
atendendo ao item opcional que sugeria a criação da rota `/usuario`.

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/usuarios` | Não (pública) | Cadastrar novo usuário |
| GET | `/usuarios` | Sim (token) | Listar usuários (paginado) |
| GET | `/usuarios/{id}` | Sim (token) | Detalhar usuário por ID |
| PUT | `/usuarios/{id}` | Sim (token) | Atualizar nome e/ou senha |
| DELETE | `/usuarios/{id}` | Sim (token) | Excluir usuário |

Validações aplicadas:
- Não permite cadastro de **e-mail duplicado** — retorna `409 Conflict` com mensagem clara.
- Atualização de senha sempre recriptografa com **BCrypt**.

### Opcional 2 — Documentação com Swagger (Card 18 do Trello)

A API está documentada automaticamente via **SpringDoc OpenAPI**.

Acesse em: `http://localhost:8080/swagger-ui.html`

Todos os endpoints são visíveis e testáveis pela interface gráfica do Swagger UI,
sem necessidade de configuração adicional.

---

## Tecnologias utilizadas

| Tecnologia | Versão | Papel no projeto |
|-----------|--------|-----------------|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.x | Framework base |
| Spring Web | — | Criação dos endpoints REST |
| Spring Security | — | Autenticação e autorização |
| Spring Data JPA | — | Persistência de dados |
| Hibernate | — | Implementação JPA |
| PostgreSQL | — | Banco de dados relacional |
| Auth0 Java JWT | — | Geração e validação de tokens JWT |
| SpringDoc OpenAPI | — | Documentação automática (Swagger UI) |
| Flyway | — | Migrations SQL (arquivos de referência) |
| Maven | — | Gerenciamento de dependências |
| Spring DevTools | — | Reload automático em desenvolvimento |
| Spring Actuator | — | Monitoramento de saúde da aplicação |

---

## Estrutura do projeto

src/main/java/br/com/ramos/forumhub/ │ ├── domain/ │ ├── controller/ │ │ ├── AutenticacaoController.java ← POST /login │ │ ├── HealthController.java ← endpoint de saúde │ │ ├── TopicoController.java ← CRUD /topicos │ │ └── UsuarioController.java ← CRUD /usuarios │ │ │ ├── infra/ │ │ └── security/ │ │ ├── AutenticacaoService.java ← UserDetailsService │ │ ├── SecurityConfigurations.java ← regras de segurança │ │ ├── SecurityFilter.java ← filtro JWT │ │ └── TokenService.java ← geração/validação JWT │ │ │ ├── topico/ │ │ ├── Topico.java ← entidade JPA │ │ ├── TopicoRepository.java ← JpaRepository │ │ ├── StatusTopico.java ← enum de status │ │ ├── DadosCadastroTopico.java ← DTO entrada │ │ ├── DadosAtualizacaoTopico.java ← DTO atualização │ │ ├── DadosDetalhamentoTopico.java ← DTO saída detalhada │ │ └── DadosListagemTopico.java ← DTO saída listagem │ │ │ └── usuario/ │ ├── Usuario.java ← entidade JPA + UserDetails │ ├── UsuarioRepository.java ← JpaRepository │ ├── DadosAutenticacao.java ← DTO login │ ├── DadosCadastroUsuario.java ← DTO entrada │ ├── DadosAtualizacaoUsuario.java ← DTO atualização │ ├── DadosDetalhamentoUsuario.java ← DTO saída detalhada │ └── DadosListagemUsuario.java ← DTO saída listagem │ ├── ForumhubApplication.java ← classe principal └── GeradorDeSenha.java ← utilitário BCrypt │ src/main/resources/ ├── db/migration/ │ ├── V1__create-table-usuarios.sql │ └── V2__create-table-topicos.sql └── application.properties
---

## Como executar o projeto localmente

### Pré-requisitos

- Java 17 ou superior
- Maven
- PostgreSQL instalado e rodando

### Passo a passo

**1. Clone o repositório**

<div class="widget code-container remove-before-copy"><div class="code-header non-draggable"><span class="iaf s13 w700 code-language-placeholder">bash</span><div class="code-copy-button"><span class="iaf s13 w500 code-copy-placeholder">Copiar</span><img class="code-copy-icon" src="data:image/svg+xml;utf8,%0A%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%2216%22%20height%3D%2216%22%20viewBox%3D%220%200%2016%2016%22%20fill%3D%22none%22%3E%0A%20%20%3Cpath%20d%3D%22M10.8%208.63V11.57C10.8%2014.02%209.82%2015%207.37%2015H4.43C1.98%2015%201%2014.02%201%2011.57V8.63C1%206.18%201.98%205.2%204.43%205.2H7.37C9.82%205.2%2010.8%206.18%2010.8%208.63Z%22%20stroke%3D%22%23717C92%22%20stroke-width%3D%221.05%22%20stroke-linecap%3D%22round%22%20stroke-linejoin%3D%22round%22%2F%3E%0A%20%20%3Cpath%20d%3D%22M15%204.42999V7.36999C15%209.81999%2014.02%2010.8%2011.57%2010.8H10.8V8.62999C10.8%206.17999%209.81995%205.19999%207.36995%205.19999H5.19995V4.42999C5.19995%201.97999%206.17995%200.999992%208.62995%200.999992H11.57C14.02%200.999992%2015%201.97999%2015%204.42999Z%22%20stroke%3D%22%23717C92%22%20stroke-width%3D%221.05%22%20stroke-linecap%3D%22round%22%20stroke-linejoin%3D%22round%22%2F%3E%0A%3C%2Fsvg%3E%0A" /></div></div><pre id="code-wbi3n3hqf" style="color:white;font-family:Consolas, Monaco, &quot;Andale Mono&quot;, &quot;Ubuntu Mono&quot;, monospace;text-align:left;white-space:pre;word-spacing:normal;word-break:normal;word-wrap:normal;line-height:1.5;font-size:1em;-moz-tab-size:4;-o-tab-size:4;tab-size:4;-webkit-hyphens:none;-moz-hyphens:none;-ms-hyphens:none;hyphens:none;padding:8px;margin:8px;overflow:auto;background:#011627;width:calc(100% - 8px);border-radius:8px;box-shadow:0px 8px 18px 0px rgba(120, 120, 143, 0.10), 2px 2px 10px 0px rgba(255, 255, 255, 0.30) inset"><code class="language-bash" style="white-space:pre;color:#d6deeb;font-family:Consolas, Monaco, &quot;Andale Mono&quot;, &quot;Ubuntu Mono&quot;, monospace;text-align:left;word-spacing:normal;word-break:normal;word-wrap:normal;line-height:1.5;font-size:1em;-moz-tab-size:4;-o-tab-size:4;tab-size:4;-webkit-hyphens:none;-moz-hyphens:none;-ms-hyphens:none;hyphens:none"><span class="token" style="color:rgb(130, 170, 255)">git</span><span> clone https://github.com/SEU_USUARIO/forumhub.git
</span><span></span><span class="token" style="color:rgb(255, 203, 139)">cd</span><span> forumhub
</span></code></pre></div>

**2. Crie o banco de dados no PostgreSQL**

<div class="widget code-container remove-before-copy"><div class="code-header non-draggable"><span class="iaf s13 w700 code-language-placeholder">sql</span><div class="code-copy-button"><span class="iaf s13 w500 code-copy-placeholder">Copiar</span><img class="code-copy-icon" src="data:image/svg+xml;utf8,%0A%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%2216%22%20height%3D%2216%22%20viewBox%3D%220%200%2016%2016%22%20fill%3D%22none%22%3E%0A%20%20%3Cpath%20d%3D%22M10.8%208.63V11.57C10.8%2014.02%209.82%2015%207.37%2015H4.43C1.98%2015%201%2014.02%201%2011.57V8.63C1%206.18%201.98%205.2%204.43%205.2H7.37C9.82%205.2%2010.8%206.18%2010.8%208.63Z%22%20stroke%3D%22%23717C92%22%20stroke-width%3D%221.05%22%20stroke-linecap%3D%22round%22%20stroke-linejoin%3D%22round%22%2F%3E%0A%20%20%3Cpath%20d%3D%22M15%204.42999V7.36999C15%209.81999%2014.02%2010.8%2011.57%2010.8H10.8V8.62999C10.8%206.17999%209.81995%205.19999%207.36995%205.19999H5.19995V4.42999C5.19995%201.97999%206.17995%200.999992%208.62995%200.999992H11.57C14.02%200.999992%2015%201.97999%2015%204.42999Z%22%20stroke%3D%22%23717C92%22%20stroke-width%3D%221.05%22%20stroke-linecap%3D%22round%22%20stroke-linejoin%3D%22round%22%2F%3E%0A%3C%2Fsvg%3E%0A" /></div></div><pre id="code-w6tmi8xvi" style="color:white;font-family:Consolas, Monaco, &quot;Andale Mono&quot;, &quot;Ubuntu Mono&quot;, monospace;text-align:left;white-space:pre;word-spacing:normal;word-break:normal;word-wrap:normal;line-height:1.5;font-size:1em;-moz-tab-size:4;-o-tab-size:4;tab-size:4;-webkit-hyphens:none;-moz-hyphens:none;-ms-hyphens:none;hyphens:none;padding:8px;margin:8px;overflow:auto;background:#011627;width:calc(100% - 8px);border-radius:8px;box-shadow:0px 8px 18px 0px rgba(120, 120, 143, 0.10), 2px 2px 10px 0px rgba(255, 255, 255, 0.30) inset"><code class="language-sql" style="white-space:pre;color:#d6deeb;font-family:Consolas, Monaco, &quot;Andale Mono&quot;, &quot;Ubuntu Mono&quot;, monospace;text-align:left;word-spacing:normal;word-break:normal;word-wrap:normal;line-height:1.5;font-size:1em;-moz-tab-size:4;-o-tab-size:4;tab-size:4;-webkit-hyphens:none;-moz-hyphens:none;-ms-hyphens:none;hyphens:none"><span class="token" style="color:rgb(127, 219, 202)">CREATE</span><span> </span><span class="token" style="color:rgb(127, 219, 202)">DATABASE</span><span> forumhub</span><span class="token" style="color:rgb(199, 146, 234)">;</span><span>
</span></code></pre></div>

**3. Configure o `application.properties`**

Edite o arquivo `src/main/resources/application.properties` com suas credenciais:

<div class="widget code-container remove-before-copy"><div class="code-header non-draggable"><span class="iaf s13 w700 code-language-placeholder">properties</span><div class="code-copy-button"><span class="iaf s13 w500 code-copy-placeholder">Copiar</span><img class="code-copy-icon" src="data:image/svg+xml;utf8,%0A%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%2216%22%20height%3D%2216%22%20viewBox%3D%220%200%2016%2016%22%20fill%3D%22none%22%3E%0A%20%20%3Cpath%20d%3D%22M10.8%208.63V11.57C10.8%2014.02%209.82%2015%207.37%2015H4.43C1.98%2015%201%2014.02%201%2011.57V8.63C1%206.18%201.98%205.2%204.43%205.2H7.37C9.82%205.2%2010.8%206.18%2010.8%208.63Z%22%20stroke%3D%22%23717C92%22%20stroke-width%3D%221.05%22%20stroke-linecap%3D%22round%22%20stroke-linejoin%3D%22round%22%2F%3E%0A%20%20%3Cpath%20d%3D%22M15%204.42999V7.36999C15%209.81999%2014.02%2010.8%2011.57%2010.8H10.8V8.62999C10.8%206.17999%209.81995%205.19999%207.36995%205.19999H5.19995V4.42999C5.19995%201.97999%206.17995%200.999992%208.62995%200.999992H11.57C14.02%200.999992%2015%201.97999%2015%204.42999Z%22%20stroke%3D%22%23717C92%22%20stroke-width%3D%221.05%22%20stroke-linecap%3D%22round%22%20stroke-linejoin%3D%22round%22%2F%3E%0A%3C%2Fsvg%3E%0A" /></div></div><pre id="code-7vt5licq5" style="color:white;font-family:Consolas, Monaco, &quot;Andale Mono&quot;, &quot;Ubuntu Mono&quot;, monospace;text-align:left;white-space:pre;word-spacing:normal;word-break:normal;word-wrap:normal;line-height:1.5;font-size:1em;-moz-tab-size:4;-o-tab-size:4;tab-size:4;-webkit-hyphens:none;-moz-hyphens:none;-ms-hyphens:none;hyphens:none;padding:8px;margin:8px;overflow:auto;background:#011627;width:calc(100% - 8px);border-radius:8px;box-shadow:0px 8px 18px 0px rgba(120, 120, 143, 0.10), 2px 2px 10px 0px rgba(255, 255, 255, 0.30) inset"><code class="language-properties" style="white-space:pre;color:#d6deeb;font-family:Consolas, Monaco, &quot;Andale Mono&quot;, &quot;Ubuntu Mono&quot;, monospace;text-align:left;word-spacing:normal;word-break:normal;word-wrap:normal;line-height:1.5;font-size:1em;-moz-tab-size:4;-o-tab-size:4;tab-size:4;-webkit-hyphens:none;-moz-hyphens:none;-ms-hyphens:none;hyphens:none"><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">spring.application.name</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">forumhub</span><span>
</span>
<span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">spring.datasource.url</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">jdbc:postgresql://localhost:5432/forumhub</span><span>
</span><span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">spring.datasource.username</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">SEU_USUARIO_POSTGRES</span><span>
</span><span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">spring.datasource.password</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">SUA_SENHA_POSTGRES</span><span>
</span><span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">spring.datasource.driver-class-name</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">org.postgresql.Driver</span><span>
</span>
<span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">spring.jpa.hibernate.ddl-auto</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">update</span><span>
</span><span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">spring.jpa.show-sql</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">true</span><span>
</span><span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">spring.jpa.properties.hibernate.format_sql</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">true</span><span>
</span><span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">spring.jpa.open-in-view</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">false</span><span>
</span>
<span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">spring.flyway.enabled</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">false</span><span>
</span>
<span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">management.endpoints.web.exposure.include</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">*</span><span>
</span><span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">management.endpoint.health.show-details</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">always</span><span>
</span>
<span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">api.security.token.secret</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">SUA_CHAVE_SECRETA_JWT</span><span>
</span>
<span></span><span class="token key" style="color:rgb(173, 219, 103);font-style:italic">springdoc.swagger-ui.path</span><span class="token" style="color:rgb(199, 146, 234)">=</span><span class="token value" style="color:rgb(255, 203, 139)">/swagger-ui.html</span><span>
</span></code></pre></div>

**4. Suba a aplicação**

<div class="widget code-container remove-before-copy"><div class="code-header non-draggable"><span class="iaf s13 w700 code-language-placeholder">bash</span><div class="code-copy-button"><span class="iaf s13 w500 code-copy-placeholder">Copiar</span><img class="code-copy-icon" src="data:image/svg+xml;utf8,%0A%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%2216%22%20height%3D%2216%22%20viewBox%3D%220%200%2016%2016%22%20fill%3D%22none%22%3E%0A%20%20%3Cpath%20d%3D%22M10.8%208.63V11.57C10.8%2014.02%209.82%2015%207.37%2015H4.43C1.98%2015%201%2014.02%201%2011.57V8.63C1%206.18%201.98%205.2%204.43%205.2H7.37C9.82%205.2%2010.8%206.18%2010.8%208.63Z%22%20stroke%3D%22%23717C92%22%20stroke-width%3D%221.05%22%20stroke-linecap%3D%22round%22%20stroke-linejoin%3D%22round%22%2F%3E%0A%20%20%3Cpath%20d%3D%22M15%204.42999V7.36999C15%209.81999%2014.02%2010.8%2011.57%2010.8H10.8V8.62999C10.8%206.17999%209.81995%205.19999%207.36995%205.19999H5.19995V4.42999C5.19995%201.97999%206.17995%200.999992%208.62995%200.999992H11.57C14.02%200.999992%2015%201.97999%2015%204.42999Z%22%20stroke%3D%22%23717C92%22%20stroke-width%3D%221.05%22%20stroke-linecap%3D%22round%22%20stroke-linejoin%3D%22round%22%2F%3E%0A%3C%2Fsvg%3E%0A" /></div></div><pre id="code-q3n9tcq2n" style="color:white;font-family:Consolas, Monaco, &quot;Andale Mono&quot;, &quot;Ubuntu Mono&quot;, monospace;text-align:left;white-space:pre;word-spacing:normal;word-break:normal;word-wrap:normal;line-height:1.5;font-size:1em;-moz-tab-size:4;-o-tab-size:4;tab-size:4;-webkit-hyphens:none;-moz-hyphens:none;-ms-hyphens:none;hyphens:none;padding:8px;margin:8px;overflow:auto;background:#011627;width:calc(100% - 8px);border-radius:8px;box-shadow:0px 8px 18px 0px rgba(120, 120, 143, 0.10), 2px 2px 10px 0px rgba(255, 255, 255, 0.30) inset"><code class="language-bash" style="white-space:pre;color:#d6deeb;font-family:Consolas, Monaco, &quot;Andale Mono&quot;, &quot;Ubuntu Mono&quot;, monospace;text-align:left;word-spacing:normal;word-break:normal;word-wrap:normal;line-height:1.5;font-size:1em;-moz-tab-size:4;-o-tab-size:4;tab-size:4;-webkit-hyphens:none;-moz-hyphens:none;-ms-hyphens:none;hyphens:none"><span>mvn spring-boot:run
</span></code></pre></div>

**5. Acesse**

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Testando a API no Insomnia

### Fluxo completo de uso

#### Passo 1 — Cadastrar usuário (sem token)
POST http://localhost:8080/usuarios Content-Type: application/json
{
  "nome": "Ramos Nunes",
  "email": "ramos@forumhub.com",
  "senha": "SenhaSegura123"
}

POST http://localhost:8080/login Content-Type: application/json
{
  "email": "ramos@forumhub.com",
  "senha": "SenhaSegura123"
}

Authorization: Bearer SEU_TOKEN_AQUI
---

#### Passo 3 — Criar tópico (com token)
POST http://localhost:8080/topicos Content-Type: application/json Authorization: Bearer SEU_TOKEN_AQUI
{
  "titulo": "Dúvidas sobre autenticação JWT no FórumHub",
  "mensagem": "Como funciona o fluxo de login e geração de token?",
  "autor": "Ramos Nunes",
  "curso": "Java Spring"
}

GET http://localhost:8080/topicos Authorization: Bearer SEU_TOKEN_AQUI
Resposta esperada: `200 OK` com lista paginada de tópicos.

---

#### Passo 5 — Detalhar tópico (com token)
GET http://localhost:8080/topicos/{id} Authorization: Bearer SEU_TOKEN_AQUI
Resposta esperada: `200 OK` com os dados do tópico.

---

#### Passo 6 — Atualizar tópico (com token)
PUT http://localhost:8080/topicos/{id} Content-Type: application/json Authorization: Bearer SEU_TOKEN_AQUI
{
  "titulo": "Dúvida solucionada",
  "mensagem": "Não tenho mais dúvidas sobre o FórumHub.",
  "curso": "Java Spring"
}

DELETE http://localhost:8080/topicos/{id} Authorization: Bearer SEU_TOKEN_AQUI
Resposta esperada: `204 No Content`

---

## Checklist do Challenge

| # | Item | Status |
|---|------|--------|
| 1 | Repositório criado no GitHub | ✅ |
| 2 | Ambiente Java 17 + Spring Boot 3 configurado | ✅ |
| 3 | Diagrama e modelagem do banco de dados | ✅ |
| 4 | Banco de dados PostgreSQL configurado | ✅ |
| 5 | Cadastro de tópico (`POST /topicos`) | ✅ |
| 6 | Listagem de tópicos (`GET /topicos`) com paginação | ✅ |
| 7 | Detalhamento de tópico (`GET /topicos/{id}`) | ✅ |
| 8 | Atualização de tópico (`PUT /topicos/{id}`) | ✅ |
| 9 | Exclusão de tópico (`DELETE /topicos/{id}`) | ✅ |
| 10 | Testes dos endpoints no Insomnia | ✅ |
| 11 | Repositório atualizado no GitHub | ✅ |
| 12 | Autenticação com Spring Security | ✅ |
| 13 | Geração de token JWT | ✅ |
| 14 | Autenticação utilizando JWT em todas as rotas protegidas | ✅ |
| 15 | README completo | ✅ |
| 16 | Link do repositório enviado na plataforma Alura | ⬜ |
| 17 | *(Opcional)* Outras rotas: `/usuarios` com CRUD completo | ✅ |
| 18 | *(Opcional)* Documentação com Swagger UI | ✅ |

---

## Autor

Desenvolvido por **José dos Ramos** como parte do programa
**Oracle Next Education (ONE) — Turma 2025/2026**.

Trilha: Back End Java com Spring Boot
Desafio: Challenge FórumHub






