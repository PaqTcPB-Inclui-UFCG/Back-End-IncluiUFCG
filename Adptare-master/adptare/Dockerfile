#Use a imagem oficial do Amazon Corretto 17 como a imagem base
FROM amazoncorretto:17-alpine

# Defina o diretório de trabalho no contêiner
WORKDIR /app

COPY target/*.jar /app/adptare.jar

# Comando para executar a aplicação quando o contêiner iniciar
CMD ["java", "-jar", "/app/adptare.jar"]