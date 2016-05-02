#!/bin/bash
a[0]='a'
a[1]='b'
a[2]='c'
for i in $(seq 100) 
do
let "x= i % 3"
echo "file text" >> "./${a[$x]}$i"
done 
echo 'files created'