#!/bin/sh

# stash any unstaged changes
git stash -q --keep-index

# build and test with the gradle wrapper
./gradlew clean build

# store the last exit code in a variable
RESULT=$?

# unstash the unstashed changes
git stash pop -q

# return the './gradlew clean build' exit code
exit $RESULT