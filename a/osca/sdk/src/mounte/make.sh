#!/bin/sh
BIN=mounte
gcc *.c -o $BIN -std=gnu99 -Wfatal-errors -Wall -Wextra&&
chown root $BIN&&
chmod u+s $BIN&&
ls -l $BIN
