# Leque de Vagas - API

API REST acadêmica para cadastro e consulta de vagas de tecnologia. Esta branch
implementa a Frente 2: empresas e o relacionamento entre empresa e vagas, sobre
a base da Frente 1.

## Requisitos

- Java 21
- Maven Wrapper incluído no projeto

## Divisão das frentes

| Frente | Responsabilidade | Integrante |
| --- | --- | --- |
| 1 | Vaga | Davi (`Davi-Carneiro-Dev`) |
| 2 | Empresa | Carlos (`Carlos-Andre-2006`) |
| 3 | Pessoa e candidatura | Davi (`Davi-Carneiro-Dev`) |
| 4 | Busca e estatísticas | Carlos (`Carlos-Andre-2006`) |

## Executar

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API fica disponível em `http://localhost:8080`. O console do H2 pode ser
aberto em `http://localhost:8080/h2-console` usando:

- JDBC URL: `jdbc:h2:mem:vagasdb`
- usuário: `sa`
- senha: vazia

## Rotas disponíveis

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/vagas` | Lista as vagas |
| GET | `/vagas/{id}` | Consulta uma vaga |
| POST | `/vagas` | Cria uma vaga |
| PUT | `/vagas/{id}` | Atualiza uma vaga |
| DELETE | `/vagas/{id}` | Exclui uma vaga |
| GET | `/vagas/busca` | Filtra por área, senioridade e iniciante |
| GET | `/estatisticas` | Resume os números do catálogo |
| GET | `/empresas` | Lista as empresas por nome |
| GET | `/empresas/{slug}` | Consulta o perfil público de uma empresa |
| POST | `/empresas` | Cadastra uma empresa |
| PUT | `/empresas/{slug}` | Atualiza uma empresa |
| DELETE | `/empresas/{slug}` | Exclui uma empresa sem vagas |
| GET | `/empresas/{slug}/vagas` | Lista as vagas publicadas pela empresa |

O arquivo `requisicoes.http` contém exemplos executáveis no IntelliJ.

## Testes

```powershell
.\mvnw.cmd test
```

## Regras da Frente 2

- Cada empresa possui um `slug` único, usado em suas URLs públicas.
- O site é opcional; quando informado, deve começar com `http://` ou `https://`.
- Toda vaga precisa estar associada a uma empresa cadastrada.
- Uma empresa que possui vagas não pode ser excluída.
- A API continua recebendo e devolvendo `empresaSlug` nas vagas, preservando o
  contrato público da Frente 1, enquanto o banco utiliza o relacionamento JPA.
