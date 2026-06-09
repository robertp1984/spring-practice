# General
minikube start
minikube addons enable metrics-server
minikube addons enable ingress

kubectl create -f namespaces.yaml

# Strimzi operator
kns kafka
kubectl create -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka

# Wait for operator to fully start

# Schema registry
kubectl create -f schema-registry.yaml

# App
kns spring-practice
kubectl create -f springjpa.yaml
