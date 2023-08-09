.PHONY: all run build dependencies

all: run

run:
	./gradlew --console=plain quarkusDev

build:
	./gradlew build

dependencies:
	docker run -tp 127.0.0.1:8080:8080/tcp exploreshackle/reservation-service:latest
