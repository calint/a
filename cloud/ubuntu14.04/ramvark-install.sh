date
cd /&&
curl http://calintenitchi.googlecode.com/svn/amazon-ec2/jre.tgz>jre.tgz&&
curl http://calintenitchi.googlecode.com/svn/amazon-ec2/a-lib.tgz>a-lib.tgz&&
curl http://calintenitchi.googlecode.com/svn/amazon-ec2/a.tgz>a.tgz&&
tar -xzvf a.tgz&&
tar -xzvf a-lib.tgz&&
tar -xzvf jre.tgz&&
chmod ugo+x /etc/rc.local&&
apt-get update&&
apt-get -y install gcc clang&&
dpkg -i deb/*&&
date

