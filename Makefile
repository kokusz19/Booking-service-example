.PHONY: clean build up run

run: clean-build up
clean-build: clean build

clean:
	gradlew clean
build:
	gradlew build
	docker-compose build
up:
	docker-compose up