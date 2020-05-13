#!/usr/bin/env bash

echo "Deleting local DB file"
rm -rf ./h2

echo "Building the application"
./gradlew clean build

echo "Running the application"
./gradlew bootrun