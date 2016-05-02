#!/bin/bash
mkdir -p ./ccc/
for f in ./c*; do
   if [ "$f" != "ccc" ]; then
        cp "$f" ./ccc/"$f"_NEW;
   fi;
done
