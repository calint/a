#!/bin/sh
CC="gcc -std=c11"
SRC=src/*.c
BIN=mounte
OPTS="-Wfatal-errors -Wall -Wextra"
$CC -o $BIN $OPTS $SRC&&
sudo chown root $BIN&&
sudo chmod u+s $BIN&&
ls -l $BIN
