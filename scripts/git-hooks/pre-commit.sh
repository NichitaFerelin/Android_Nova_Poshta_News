#!/bin/sh

echo "✨ Running linter.."
./gradlew ktlintCheck
EXIT_CODE=$?
if [ $EXIT_CODE -ne 0 ]; then
    echo "❌ Linter found errors in project; running formatting for you.."
    ./gradlew ktlintFormat -q
    echo "✅ Formatting done, please try to commit again!"

    exit ${EXIT_CODE}
fi

echo "🔎 Running static code analysis .."
./gradlew detektMain
EXIT_CODE=$?
if [ $EXIT_CODE -ne 0 ]; then
    echo "❌ Static code analysis failed."

    exit ${EXIT_CODE}
fi
