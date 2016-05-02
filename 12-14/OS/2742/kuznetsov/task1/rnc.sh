#!/bin/bash
mkdir ccc
i=10
for f in c*
do
  test -f $f && cp $f ccc/$i && let "i += 1"
done
