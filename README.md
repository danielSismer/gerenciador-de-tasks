﻿# 🚀 Gerenciador de Tasks

Bem-vindo ao **Gerenciador de Tasks**!  
Um projeto feito com ❤️ para praticar Java, SQL e integração com banco de dados na nuvem.  
Ideal para quem quer aprender, experimentar e se divertir programando! 😄

---

## ✨ Sobre o Projeto

Este projeto nasceu para fins didáticos, com o objetivo de praticar:
- 🧑‍💻 Programação orientada a objetos em Java
- ☁️ Conexão com banco de dados MySQL na nuvem
- 🏗️ Estruturação de projetos Java com Maven
- 🗂️ Separação de camadas (modelo, serviço, visualização)
- 🗓️ Manipulação de datas, enums e listas

---

## 🛠️ Funcionalidades

- ✍️ Criar tarefas simples ou com prazo
- 📋 Listar todas as tarefas
- 🔄 Atualizar status das tarefas
- 🎯 Filtrar tarefas por status ou prioridade
- 📊 Exibir resumo de tarefas por status

---

## 🗃️ Estrutura do Projeto

- `src/net/intra/model/Task.java` & `TaskWithDeadline.java`: Modelos das tarefas
- `src/net/intra/service/TaskService.java`: Lógica de negócio e persistência
- `src/net/intra/viewer/TaskViewer.java`: Interface de linha de comando (CLI)
- `src/net/intra/database/DatabaseConnection.java`: Conexão com o MySQL
- `src/net/intra/main/Main.java`: Ponto de entrada da aplicação

---

## 🛢️ Banco de Dados

O projeto usa um banco MySQL na nuvem (Railway).  
As credenciais estão hardcoded para facilitar testes, mas **NUNCA** faça isso em produção! 🔒

**Exemplo de tabelas:**
```sql
CREATE TABLE tasks (
    id VARCHAR(36) PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    priority VARCHAR(10) NOT NULL,
    status VARCHAR(15) NOT NULL,
    creation_date DATE DEFAULT CURRENT_DATE,
    deadline DATE
);

CREATE TABLE task_status_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task_id VARCHAR(36),
    old_status VARCHAR(15),
    new_status VARCHAR(15),
    change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🚦 Requisitos

- Java 17+
- Maven
- MySQL (ou acesso ao banco em nuvem)

---

## ▶️ Como Executar

1. **Clone o repositório:**
   ```bash
   git clone <url-do-repo>
   cd gerenciador-de-tasks
   ```
2. **Configure o banco de dados** (opcional, caso queira usar outro banco).
3. **Compile o projeto:**
   ```bash
   mvn clean package
   ```
4. **Execute a aplicação:**
   ```bash
   mvn exec:java -Dexec.mainClass="net.intra.main.Main"
   ```

---

## 📦 Dependências

- [mysql-connector-j 8.0.33](https://mvnrepository.com/artifact/mysql/mysql-connector-j/8.0.33)

---

## ⚠️ Observações

- Projeto 100% didático, feito para aprender e compartilhar conhecimento!
- As credenciais do banco estão visíveis apenas para facilitar testes e aprendizado.
- Modifique, estude, brinque e divirta-se com o código! 🚀

---

Feito com muita curiosidade e café ☕ 
Bons estudos! 💡
