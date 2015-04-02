echo `date` rsync
rsync -e "ssh -i$KEY_ -oStrictHostKeyChecking=no" --recursive --exclude .svn $SYNC_ root@$HOST_:/

echo `date` install
$SSHKR 'sh -s'<install-ramvark.sh

echo `date` done
