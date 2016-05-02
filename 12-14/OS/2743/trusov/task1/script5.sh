#!/bin/bash

find . -type f -maxdepth 1 -exec grep -m 1 -l $1 {} \;
