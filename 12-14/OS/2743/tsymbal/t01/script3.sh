#!/bin/bash

if [ ! -d ./ccc ] 
	then mkdir -p ./ccc
fi

for file in ./c*; do
	mv "$file" ./ccc/"$file"_new;
done
