#!/bin/bash

if ! [ -d ccc ]; then
    echo "create directory ccc"
    eval "mkdir ccc"
fi
ls|grep ^c|while read f;
do
    if [ "$f" != "ccc" ]; then
        cp -R "$f" ccc/new_$f;
    fi;
done

