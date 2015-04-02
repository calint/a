cd /&&

echo `date` download jre
curl --silent http://calintenitchi.googlecode.com/svn/amazon-ec2/jre.tgz>jre.tgz&&

echo `date` download jars
curl --silent http://calintenitchi.googlecode.com/svn/amazon-ec2/a-lib.tgz>a-lib.tgz&&

echo `date` download ramvark
curl --silent http://calintenitchi.googlecode.com/svn/amazon-ec2/a.tgz>a.tgz&&

echo `date` download crafty
curl --silent http://calintenitchi.googlecode.com/svn/amazon-ec2/crafty_23.4-6ubuntu1_amd64.deb>crafty_23.4-6ubuntu1_amd64.deb&&

echo `date` unpack
tar -xzf a.tgz&&
tar -xzf a-lib.tgz&&
tar -xzf jre.tgz&&

chmod ugo+x /etc/rc.local&&
apt-get --quiet=2 update&&

echo `date` install gcc clang crafty
apt-get --quiet=2 -y install gcc clang&&
dpkg -i *.deb 1>/dev/null

echo `date` start
/etc/rc.local &

echo `date` done
