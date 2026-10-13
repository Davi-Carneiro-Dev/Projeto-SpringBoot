# Leque de Vagas - API

API REST acadêmica para cadastro e consulta de vagas de tecnologia. Esta etapa
implementa a Frente 1: vagas, busca e estatísticas.

## Requisitos

- Java 21
- Maven Wrapper incluído no projeto

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

## Rotas da Frente 1

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/vagas` | Lista as vagas |
| GET | `/vagas/{id}` | Consulta uma vaga |
| POST | `/vagas` | Cria uma vaga |
| PUT | `/vagas/{id}` | Atualiza uma vaga |
| DELETE | `/vagas/{id}` | Exclui uma vaga |
| GET | `/vagas/busca` | Filtra por área, senioridade e iniciante |
| GET | `/estatisticas` | Resume os números do catálogo |

O arquivo `requisicoes.http` contém exemplos executáveis no IntelliJ.

## Testes

```powershell
.\mvnw.cmd test
```

Nesta fase, a vaga guarda `empresaSlug` como texto. O relacionamento com a
entidade Empresa será integrado posteriormente com a frente da outra pessoa da
dupla.
