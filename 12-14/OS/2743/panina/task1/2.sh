#!/bin/bash
mkdir -p ./bbb/
for f in ./b*; do
   if [ "$f" != "bbb" ]; then
        mv "$f" ./bbb/"$f";
   fi;
done
