#!/bin/bash

find . -name 'a*' -type f -print0 | xargs -r -0 rm

