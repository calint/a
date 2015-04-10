HOST_=$(if [ -z $1 ];then echo ramvark.net;else echo $1;fi)
WORKSPACE_=$(realpath $(if [ -z $2 ];then echo ../..;else echo $2;fi))
SYNC_=$WORKSPACE_/a/cloud/ubuntu14.04/
SSHK='ssh -oConnectTimeout=60 -oBatchMode=yes -oStrictHostKeyChecking=no'$(if [ ! -z "$KEY_" ];then echo " -i$KEY_";fi)
SSHKR="$SSHK root@$HOST_"

echo "      host=$HOST_"
echo "       key=$KEY_"
echo "        ssh=$SSHKR"
echo "      sync=$SYNC_"
echo "   ramvark=$WORKSPACE_/a"
