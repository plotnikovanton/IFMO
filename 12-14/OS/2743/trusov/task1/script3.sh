#!/bin/bash

mkdir -p ./ccc/ & find . -maxdepth 1 -name "c*" -type f | while read f
do
        cp -fb $f ./ccc/$1  
done

