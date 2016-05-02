#!/bin/bash

read pattern
if [ "$2" == "" ] || [ "$2" == "-" ]; then
    grep -F "$pattern" "$1"
else
    grep -F "$pattern" "$1" > "$2"
fi
