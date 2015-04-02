echo `date` rsync
rsync -e "$SSHK" --recursive --exclude .svn $SYNC_ root@$HOST_:/

echo `date` install
$SSHKR 'sh -s'<install-ramvark.sh

echo `date` done
