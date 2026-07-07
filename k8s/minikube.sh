#!/usr/bin/env bash

# General
minikube start
minikube addons enable metrics-server
minikube addons enable ingress

kubectl apply -f namespaces.yaml

# Strimzi operator
kns kafka
kubectl create -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka
# Wait for operator to fully start

# Kafka using dual role nodes (controller + broker)
kubectl apply -f kafka-with-dual-role-nodes.yaml

# Schema registry
kubectl apply -f schema-registry.yaml

# Application SpringJPA
kns spring-practice
kubectl apply -f springjpa.yaml

# Application Chat
kns spring-practice
kubectl create secret generic openai-api-key --from-literal=OPENAI_API_KEY=${OPENAI_API_KEY}
kubectl create secret generic api-ninjas-api-key --from-literal=API_NINJAS_API_KEY=${API_NINJAS_API_KEY}
kubectl apply -f chat.yaml

# Ingress with TLS and dummy certificate
kns spring-practice
kubectl -n spring-practice create secret tls ingress-tls --key tls/localhost.pem --cert tls/localhost.crt
kubectl apply -f ingress.yaml

# Minikube tunnel
minikube tunnel

# Port forwarding to OAuth2.0 authserver (for logging into the SpringJPA application)
kubectl -n spring-practice port-forward svc/authserver 9000:9000
