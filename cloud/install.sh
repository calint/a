echo `date` rsync
rsync --recursive --exclude .svn $SYNC_ -e "ssh -i $KEY_ -o StrictHostKeyChecking=no" $SYNC_ root@$HOST_:/

echo `date` install
ssh -i$KEY_ root@$HOST_ 'sh -s'<install-ramvark.sh

echo `date` done
