# 💊 Meu Sintoma

Sistema de Gerenciamento de Saúde

Plataforma integrada para controle de sintomas, acompanhamento da evolução de doenças e gestão de consultas médicas, com os seguintes recursos:

- Registro e monitoramento de sintomas

- Acompanhamento da evolução de doenças ao longo do tempo

- Agendamento de consultas médicas via calendário interativo

- Filtragem por localidade, especialidade médica e planos de saúde aceitos

- Confirmação de agendamento via e-mail

- Controle completo do histórico de consultas e tratamentos

![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.2-green.svg)
![Status](https://img.shields.io/badge/status-%20funcional-green)

---

## 📚 Tecnologias Utilizadas

- **Java 17** ☕  
- **Spring Boot 3**  
- **Spring Data JPA + Hibernate**  
- **Spring Security** 🔐  
- **PostgreSQL** 🐘  
- **Docker** 🐳  

---

## ✅ Funcionalidades Principais

- 📅 Cadastro e gestão de calendários médicos  
- 🧑‍⚕️ Autenticação e autorização de usuários  
- 📨 Confirmação de e-mail com criptografia  
- 🏥 Associação de médicos a planos de saúde
- 🏥 Adoção de planos de saúde pelos pacientes
- 📊 Consulta de disponibilidade em múltiplos formatos: diário, semanal, por intervalo de datas  
- 🩺 Registro e acompanhamento de sintomas e doenças crônicas  
- 📝 Anotações médicas por consulta, visíveis para médico e/ou paciente  
- 🧾 Controle de secretárias na gestão de agenda e marcação de consultas  

---

## 🛠️ Como rodar localmente

### Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL rodando
- (Opcional) Docker & Docker Compose

### Passos

```bash
# Clone o repositório
git clone https://github.com/Andrey-Lucca/meu-sintoma.git
cd meu-sintoma

# Configure o application.yml (caso esteja utilizando o docker) ou application.properties
