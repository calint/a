for d in $(ls $WORKSPACE);do
	echo "*** $d" &&
	xii-commit-project-in-workspace-to-github.sh $d
	cd ..
done
