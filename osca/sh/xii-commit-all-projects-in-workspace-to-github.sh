for d in $(ls $WORKSPACE);do
	xii-commit-project-in-workspace-to-github.sh $d
done
