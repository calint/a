for d in $(ls $WORKSPACE);do echo "*** $d" ; cd $WORKSPACE/$d ; git remote set-url origin git@github.com:calint/$d.git ; git add . ; git commit -m "." ; git push --all ; git push --tags; cd ..;done;
