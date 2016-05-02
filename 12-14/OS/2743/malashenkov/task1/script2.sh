#!/bin/bash

if ! [ -d bbb ]; then
    echo "create directory bbb"
    eval "mkdir bbb"
fi
ls|grep ^b|while read f;
do
    if [ "$f" != "bbb" ]; then
        mv "$f" bbb;
    fi;
done

