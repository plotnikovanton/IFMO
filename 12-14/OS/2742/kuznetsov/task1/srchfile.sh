#!/bin/bash
if [ $# -eq 1 ]; then
  for f in *$1*
  do 
    echo $f
  done
fi
