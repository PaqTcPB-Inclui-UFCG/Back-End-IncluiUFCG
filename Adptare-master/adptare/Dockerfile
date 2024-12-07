#Use a imagem oficial do Amazon Corretto 17 como a imagem base
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install 

# Defina o diretório de trabalho no contêiner
WORKDIR /app

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/adptare-0.0.1-SNAPSHOT.jar adptare.jar

# Comando para executar a aplicação quando o contêiner iniciar
CMD ["java", "-jar", "adptare.jar"]