f=_
sh get-all-repo-names.sh > $f && cat $f &&
cat $f | while read nm; do cd $nm && pwd && git pull && cd ..; done
rm $f
echo done
