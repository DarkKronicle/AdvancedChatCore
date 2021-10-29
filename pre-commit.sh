#!/bin/bash
./gradlew spotlessApply
# Restage
git update-index --again