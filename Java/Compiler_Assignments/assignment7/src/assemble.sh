#!/bin/bash
set dir=`dirname $0`
set file=`dirname $1`/`basename $1 .s`
gas $file.s -o $file.o
gld -e start $file.o runtime.o -lc -o $file