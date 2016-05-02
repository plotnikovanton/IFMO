#!/bin/bash
mkdir --parents ./bbb/
find . -maxdepth 1 -name 'b*' -type f -exec mv {} ./bbb/ \;
