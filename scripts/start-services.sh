#!/bin/bash
set -e
echo "Starting KATM microservices..."
docker compose up -d katm-auth katm-gateway katm-client katm-claim katm-contract \
    katm-notification katm-report katm-queue katm-scheduler katm-mip
echo ""
echo "Microservices:"
echo "  Gateway:       http://localhost:8080"
echo "  Auth:          http://localhost:8081"
echo "  Notification:  http://localhost:8082"
echo "  Queue:         http://localhost:8083"
echo "  Report:        http://localhost:8084"
echo "  Claim:         http://localhost:8085"
echo "  Contract:      http://localhost:8086"
echo "  Client:        http://localhost:8087"
echo "  Scheduler:     http://localhost:8088"
echo "  MIP:           http://localhost:8089"
