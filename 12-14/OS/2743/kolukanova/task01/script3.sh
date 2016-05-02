#!/bin/bash
mkdir -p CCCC
for x in c*; do
   cp $x CCCC/$x-newName;   
done
