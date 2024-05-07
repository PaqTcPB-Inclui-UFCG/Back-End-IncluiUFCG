# Guia de Uso: Docker Compose para Back-end da Aplicação Adptare

Este guia fornecerá instruções passo a passo para construir e executar o contêiner Docker para o back-end da aplicação Adptare usando Docker Compose.

## Pré-requisitos

Certifique-se de ter o Maven, Docker e Docker Compose instalados em sua máquina. Você pode instalá-los seguindo as instruções oficiais em [Maven](https://maven.apache.org/install.html), [Docker](https://docs.docker.com/get-docker/) e [Docker Compose](https://docs.docker.com/compose/install/).

## Passos

1. **Clone este repositório:**
   
   ```bash
   git clone https://github.com/Adptare/Back-Adptare.git
   ```

2. **Navegue até o diretório do projeto:**
   
   ```bash
   cd Back-Adptare/Adptare-master/adptare/
   ```

3. **Construa o pacote JAR do back-end usando o Maven:**
   
   ```bash
   mvn clean package
   ```

   Isso criará o arquivo JAR executável do back-end na pasta `target`.

4. **Inicie o contêiner do Docker Compose para o back-end:**
   
   ```bash
   sudo docker-compose up --build -d
   ```

   Isso irá construir e iniciar o contêiner Docker para o back-end da aplicação Adptare. O parâmetro `-d` é opcional e significa "detached", o que faz com que o contêiner seja executado em segundo plano.

5. **Verifique se o contêiner está em execução:**
   
   ```bash
   docker ps
   ```

   Você deverá ver o contêiner do back-end da aplicação Adptare em execução na lista.

6. **Verifique os logs do contêiner para garantir que tudo está funcionando corretamente:**
   
   ```bash
   docker logs <nome-do-contêiner>
   ```

   Isso exibirá os logs do contêiner Docker, permitindo que você verifique se não há erros durante a inicialização.

## Observações

- Certifique-se de revisar e ajustar as configurações de segurança e acesso conforme necessário para o seu ambiente específico.
- Certifique-se de que o contêiner do back-end esteja na mesma rede Docker que os contêineres do front-end e do banco de dados para permitir a comunicação adequada entre eles.
- Esta aplicação é desenvolvida em Java com Spring Boot, incluindo Spring Security para controle de acesso e autenticação.
- Certifique-se de que o contêiner do back-end esteja na mesma rede Docker que os contêineres do front-end e do banco de dados para permitir a comunicação adequada entre eles. O back-end depende do contêiner do banco de dados para funcionar corretamente.
