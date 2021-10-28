#!/bin/bash
# https://blog.jdriven.com/2020/11/formatting-in-pre-commit-hook/
set -e

filesToAddAfterFormatting=()
containsJavaOrKotlin=0

# Collect all files currently in staging area, and check if there are any java or kotlin files
for entry in $(git status --porcelain | sed -r 's/[ \t]+/-/g')
do
  # entry can be for example:
  # MM-src/main/java/net/project/MyController.java
  # -M-src/main/java/net/project/AnotherController.java

 if [[ $entry == M* ]] ; then
    filesToAddAfterFormatting+=(${entry:2}) # strips the prefix
 fi

 if [[ $entry == *.java ]] || [[ $entry == *.kt ]] ; then
    containsJavaOrKotlin=1
 fi
done;


# If any java or kotlin files are found, run spotlessApply
if [ "$containsJavaOrKotlin" == "1" ] ; then
  echo "Kotlin and/or Java found in staging, running:  ./gradlew -PdisableSpotlessCheck spotlessApply"
  ./gradlew -PdisableSpotlessCheck spotlessApply
else
  echo "Not running spotlessApply"
fi

# Add the files that were in the staging area
for fileToAdd in $filesToAddAfterFormatting
do
  echo "re-adding $fileToAdd after formatting"
  git add "$fileToAdd"
done;