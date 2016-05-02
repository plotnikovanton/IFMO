#!/bin/bash

find . -maxdepth 1 -type f | while read file; do
    ol=$(grep -Z "$1" -n -F "$file")
    if [ "$ol" != "" ]; then
        echo -n "$file "
        echo "$ol" | tr '\0' '\n'
    fi
done
