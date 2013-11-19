cd ../../bin&&
jar="../upload.jar"&&
jar cvf $jar applet/*&&
#jarsigner $jar applet.upload&&
ls -l $jar
echo done

