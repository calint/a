HOST_=$(if [ -z $1 ];then echo 104.131.63.23;else echo $1;fi)
#USER_=ubuntu
WORKSPACE_=$(realpath $(if [ -z $2 ];then echo ../..;else echo $2;fi))
SYNC_=$WORKSPACE_/a/cloud/ubuntu14.04/
KEY_=$HOME/p/aws-key.pem

SSH_KEY_ARG="$(if [ -z KEY_ ];then echo;else echo -i$KEY_;fi)"
SSHK="ssh -oStrictHostKeyChecking=no $SSH_KEY_ARG"
SSHKR="$SSHK root@$HOST_"

echo "      host=$HOST_"
#echo "       key=$KEY_"
#echo "   ssh key=$SSH_KEY_ARG"
#echo "        ssh=$SSHK"
 echo "        ssh=$SSHKR"
#echo "      user=$USER_"
echo "      sync=$SYNC_"
echo "   ramvark=$WORKSPACE_/a"
