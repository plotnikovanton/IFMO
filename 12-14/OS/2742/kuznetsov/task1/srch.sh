#!/bin/bash
if [ $# -eq 1 ]; then
  grep $1 file-with-strings >> file-with-found-strings
fi
