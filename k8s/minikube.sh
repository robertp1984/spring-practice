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

# Schema registry
kubectl apply -f schema-registry.yaml

# App
kns spring-practice
kubectl apply -f springjpa.yaml

# Ingress
kns spring-practice
kubectl -n spring-practice create secret tls ingress-tls --key tls/localhost.pem --cert tls/localhost.crt
kubectl apply -f ingress.yaml

# Minikube tunnel
minikube tunnel
