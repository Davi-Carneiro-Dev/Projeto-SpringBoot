# Leque de Vagas - API

API REST acadêmica para cadastro e consulta de vagas de tecnologia. O projeto
integra as quatro frentes: vagas, empresas, pessoas, candidaturas, busca,
paginação e estatísticas.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Jakarta Bean Validation
- H2 Database
- Maven Wrapper
- JUnit e MockMvc

## Divisão das frentes

| Frente | Responsabilidade | Integrante |
| --- | --- | --- |
| 1 | Vaga | Davi (`Davi-Carneiro-Dev`) |
| 2 | Empresa | Carlos (`Carlos-Andre-2006`) |
| 3 | Pessoa e candidatura | Davi (`Davi-Carneiro-Dev`) |
| 4 | Busca e estatísticas | Carlos (`Carlos-Andre-2006`) |

## Executar o projeto

No Windows, abra um terminal na raiz do projeto e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

O projeto inclui uma carga inicial com empresas, vagas, pessoas e candidaturas
para facilitar a avaliação das rotas. Como o banco H2 está configurado em
memória, os dados são recriados sempre que a aplicação é reiniciada.

## Console do H2

Acesse `http://localhost:8080/h2-console` e utilize:

- Driver Class: `org.h2.Driver`
- JDBC URL: `jdbc:h2:mem:vagasdb`
- User Name: `sa`
- Password: deixe em branco

## Rotas disponíveis

### Empresas

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/empresas` | Lista as empresas por nome |
| GET | `/empresas/{slug}` | Consulta o perfil público de uma empresa |
| POST | `/empresas` | Cadastra uma empresa |
| PUT | `/empresas/{slug}` | Atualiza uma empresa |
| DELETE | `/empresas/{slug}` | Exclui uma empresa sem vagas |
| GET | `/empresas/{slug}/vagas` | Lista as vagas publicadas pela empresa |

### Vagas, busca e estatísticas

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/vagas` | Lista as vagas com paginação e ordenação |
| GET | `/vagas/{id}` | Consulta uma vaga |
| POST | `/vagas` | Cria uma vaga vinculada a uma empresa |
| PUT | `/vagas/{id}` | Atualiza uma vaga |
| DELETE | `/vagas/{id}` | Exclui uma vaga |
| GET | `/vagas/busca` | Filtra e ordena vagas por diferentes critérios |
| GET | `/estatisticas` | Resume os números do catálogo |

Os parâmetros de paginação de `/vagas` são `pagina`, `tamanho`, `ordenarPor` e
`direcao`. A busca aceita `area`, `senioridade`, `aceitaIniciante`, `local`,
`empresaSlug`, `dataInicio`, `dataFim`, `ordenarPor` e `direcao`.

### Pessoas e candidaturas

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/pessoas` | Lista as pessoas |
| GET | `/pessoas/{id}` | Consulta uma pessoa |
| POST | `/pessoas` | Cadastra uma pessoa |
| PUT | `/pessoas/{id}` | Atualiza uma pessoa |
| DELETE | `/pessoas/{id}` | Exclui uma pessoa sem candidaturas |
| GET | `/candidaturas` | Lista ou filtra candidaturas por pessoa e vaga |
| GET | `/candidaturas/{id}` | Consulta uma candidatura |
| POST | `/candidaturas` | Cria uma candidatura |
| PATCH | `/candidaturas/{id}/situacao` | Atualiza a situação da candidatura |
| DELETE | `/candidaturas/{id}` | Exclui uma candidatura |

## Regras principais

- O `slug` da empresa e o e-mail da pessoa são únicos; duplicidades retornam
  HTTP 409.
- Toda vaga deve estar associada a uma empresa existente.
- Empresas com vagas e pessoas com candidaturas não podem ser excluídas.
- Uma pessoa não pode se candidatar duas vezes à mesma vaga.
- Requisições inválidas retornam HTTP 400 e recursos inexistentes retornam
  HTTP 404.

## Exemplos de requisições

O arquivo [`requisicoes.http`](requisicoes.http) contém exemplos executáveis no
IntelliJ para todas as operações principais da API.

Algumas consultas rápidas também podem ser abertas diretamente no navegador:

- `http://localhost:8080/empresas`
- `http://localhost:8080/vagas`
- `http://localhost:8080/pessoas`
- `http://localhost:8080/candidaturas`
- `http://localhost:8080/estatisticas`

## Testes

Para executar toda a suíte automatizada:

```powershell
.\mvnw.cmd test
```

Os testes cobrem serviços, validações, conflitos e endpoints REST com MockMvc.
