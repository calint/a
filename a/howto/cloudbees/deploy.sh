echo $0

WS=../..
WD=cloudbees-deploy-work-dir

date
if [ -d $WD ];then echo "  •• clear workdir $WD";rm -rf $WD;fi&&
mkdir $WD&&cd $WD&&
for p in a;do
  echo "  •• copy $p"&&cp -a $WS/$p/* .
done
if [ -d u ];then echo "  •• delete user files"&&rm -rf u;fi
echo 42>mu-1f7c1fa1-64535080-7d8aa870-6cb91bba&&    # blitz.io key
zip deploy.zip -r .&&
ls -lah deploy.zip&&
bees app:deploy -a bb -t java -P java_version=1.8 -R classpath=bin:lib/mysql-connector-java-5.1.31-bin.jar -R class=b.b -R args="cloud_bees true root_dir app" -v -d false deploy.zip stickySession=true httpVersion=1.1 jvmFileEncoding=UTF-8&&
cd ..&&
rm -rf $WD&&
echo "  •• clear workdir $WD"&&
date
#echo "  •• connect to application log"&&
#bees app:tail bb
