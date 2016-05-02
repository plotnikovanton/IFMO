#!/bin/bash
mkdir --parents ./ccc/
find . -maxdepth 1 -name 'c*' -type f | cat  | while read LINE; do 
cp $LINE "./ccc/newname_${LINE//.\//}"
done
