#!/bin/sh
DIR="$( cd "$( dirname "$0" )" && pwd )"
exec "$DIR/gradlew" "$@"
