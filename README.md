# ERP Backend Java – Documentação

## Requisitos
- Java 17+
- Maven 3.8+
- Docker e Docker Compose
- PostgreSQL 15 (já incluso via docker-compose)
- IDE (recomendado: IntelliJ IDEA)

---

## Como executar o projeto

### 1. Suba o banco de dados
```bash
docker-compose up -d
```

Isso irá subir um container PostgreSQL em `localhost:5432`, com:

- Banco: `backend`  
- Usuário: `postgres`  
- Senha: `postgres`  

---

### 2. Execute a aplicação
Você pode rodar pela IDE (classe principal `Main`) ou via Maven:

```bash
mvn spring-boot:run
```

---

## Documentação da API (Swagger)

Após a aplicação estar no ar, acesse:

```
http://localhost:8080/swagger-ui.html
```

ou

```
http://localhost:8080/swagger-ui/index.html
```

Essa interface contém todos os endpoints dos módulos de:
- **Produtos**
- **Pedidos**
- **Itens do Pedido**

---

## Testes automatizados

Os testes automatizados já estão implementados para os principais fluxos:

- Criação, edição, busca e exclusão de produtos
- Criação, atualização, consulta e remoção de pedidos e itens

Para rodar os testes:

```bash
mvn test
```

Os testes utilizam `MockMvc`, `JUnit 5`, `AssertJ` e `ObjectMapper` para validação de JSON.

---

## Estrutura de pastas

```
├── adapter
│ ├── converter
│ ├── inbound
│ │ └── web
│ │ ├── dto
│ │ ├── errorhandler
│ │ ├── openapi
│ │ └── v1
│ └── outbound
│ └── persistence
│ ├── entity
│ └── repository
├── core
│ ├── domain
│ ├── port
│ └── usecase
├── infrastructure
└── Main
```

---

## Tecnologias utilizadas

- Spring Boot 3.5
- Spring Web, JPA e Validation
- PostgreSQL + Flyway
- JUnit + Mockito + AssertJ
- Swagger via `springdoc-openapi-ui`
- Docker + Docker Compose