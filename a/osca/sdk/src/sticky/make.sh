#!/bin/sh
app=sticky
gcc -o $app $app.c -Wfatal-errors -Wall -Wextra `pkg-config --cflags --libs gtk+-3.0 gtksourceview-3.0`&&
ls -l $app
