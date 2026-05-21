#!/bin/bash
set -e
echo "Starting KATM infrastructure..."
docker compose up -d oracle-db zookeeper kafka keycloak minio prometheus grafana zipkin kafka-ui
echo ""
echo "Waiting for services to start..."
sleep 10
echo ""
echo "Infrastructure services:"
echo "  Oracle DB:   localhost:1521"
echo "  Kafka:       localhost:9092"
echo "  Kafka UI:    http://localhost:8090"
echo "  Keycloak:    http://localhost:8180"
echo "  MinIO:       http://localhost:9001"
echo "  Prometheus:  http://localhost:9090"
echo "  Grafana:     http://localhost:3000"
echo "  Zipkin:      http://localhost:9411"
echo ""
echo "Run 'mvn clean package -DskipTests' to build services"
