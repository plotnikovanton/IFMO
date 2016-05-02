#!/bin/bash

for i in {1..33}
do
    eval "touch a$i"
done
for i in {1..33}
do
    eval "touch b$i"
done
for i in {1..33}
do
    eval "touch c$i"
done
eval "touch x13"

