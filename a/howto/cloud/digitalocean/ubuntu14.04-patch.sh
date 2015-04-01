IP=$(if [ -z $1 ];then echo 104.131.63.23;else echo $1;fi)
WS=$(realpath $(if [ -z $2 ];then echo /home/c/w;else echo $2;fi))
SRC=$WS/a/howto/cloud/digitalocean/ubuntu14.04/
echo "  workspace: $WS"
echo "      patch: $SRC"
echo "destination: $IP"
echo
rsync --recursive --verbose --progress --exclude .svn -aze 'ssh -oStrictHostKeyChecking=no -oBatchMode=yes' $SRC root@$IP:/
date
ssh root@$IP 'sh /ramvark-install.sh'
date
ssh root@$IP 'chmod ugo+x /etc/rc.local && /etc/rc.local'
date
ssh root@$IP 'apt-get install gcc clang crafty figlet cowsay fortune'
date
