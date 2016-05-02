#!/bin/bash
for i in {10..43}
do
  echo $i >> a$i
done

for i in {10..43}
do
  echo $i >> b$i
done

for i in {10..43}
do
  echo $i >> c$i
done
