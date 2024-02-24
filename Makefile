.PHONY: clean build up run

run: clean-build up
clean-build: clean build

clean:
	gradlew clean
build:
	gradlew build
up:
	docker-compose up