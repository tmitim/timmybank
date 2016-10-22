# Dropwizard - Gradle - Shadowjar


I realized I was wasting a lot of time starting projects using Dropwizard.... Never again.

This repo is a nice starting-point basic implementation of dropwizard using gradle and shadowJar.


## build...
```
gradle clean build shadowJar
```

## run... 
```
java -jar build/libs/dropwizard-base-project-all.jar server configuration.yml
```

## visit...

```
localhost:8080/api/v1/test/       (test)   
localhost:8081/                   (healthcheck)
```

*glhf*