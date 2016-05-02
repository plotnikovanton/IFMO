#!/bin/bash

if [ ! -d ./bbb ]
	then mkdir -p ./bbb
fi

for file in ./b*; do
	mv "$file" ./bbb
done
