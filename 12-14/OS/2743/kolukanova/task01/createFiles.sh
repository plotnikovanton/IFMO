#!/bin/bash
for i in a b c d e f 
do
	for j in a b c d e f 
	do
		for k in a b c d e f 
			do	
			touch $i$j$k
		done
	done
done
