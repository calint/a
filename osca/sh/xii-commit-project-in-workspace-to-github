#!/bin/sh
echo "*** $1" &&
cd $WORKSPACE/$1 &&
git remote set-url origin git@github.com:calint/$1.git &&
git add . &&
git commit -m "."
git push --all &&
git push --tags
