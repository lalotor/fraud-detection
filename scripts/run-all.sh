echo "Killing apps"
pkill -9 -f fraud-detection || echo "Failed to kill any apps"

echo "Killing containers"
docker-compose kill || echo "No docker containers are running"

echo "Running infra"
docker-compose up -d tracing-server

echo "Running apps"
nohup ./mvnw -pl config-server spring-boot:run -Dcommon.id=fraud-detection > scripts/config-server.log 2>&1 &
echo "Waiting for config server to start"
sleep 10
nohup ./mvnw -pl discovery-server spring-boot:run -Dcommon.id=fraud-detection > scripts/discovery-server.log 2>&1 &
echo "Waiting for discovery server to start"
sleep 10
nohup ./mvnw -pl notification spring-boot:run -Dcommon.id=fraud-detection > scripts/notification-service.log 2>&1 &
nohup ./mvnw -pl fraud-analysis spring-boot:run -Dcommon.id=fraud-detection > scripts/fraud-analysis-service.log 2>&1 &
nohup ./mvnw -pl transaction spring-boot:run -Dcommon.id=fraud-detection > scripts/transaction-service.log 2>&1 &
nohup ./mvnw -pl api-gateway spring-boot:run -Dcommon.id=fraud-detection > scripts/api-gateway.log 2>&1 &
echo "Waiting for apps to start"
sleep 30
