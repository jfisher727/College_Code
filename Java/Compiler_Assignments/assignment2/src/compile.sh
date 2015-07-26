#!/bin/csh
setenv dir `dirname $1`
setenv base `basename $1 .java`
setenv file $dir/$base
setenv run .
# Assumes files $1, $run/macro.s, and $run/runtime.o exist
# Creates file $file
/bin/rm -f $file.s
java Main < $file.java > $file.s
gas $file.s -o $file.o
# do not delete $file.s; leave for debugging and evaluation
gld -e start $file.o $run/runtime.o -lc -o $file
/bin/rm -f $file.o # delete temporary files
