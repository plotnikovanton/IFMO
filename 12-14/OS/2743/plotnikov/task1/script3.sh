#!/bin/bash

mkdir -p ./ccc/ & find . -maxdepth 1 -name "c*" -type f | while read f
do
	cp -fb $f ./ccc/$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)  
done
