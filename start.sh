# Run docker services

./gradlew build
docker build -t demo-backend .
docker-compose up
#docker-compose up -d