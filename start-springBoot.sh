rm -f tpid
#nohup java -jar -Xms2048m -Xmx2048m springBoot.jar > /dev/null 2>&1 &
nohup java -jar springBoot.jar > /dev/null 2>&1 &
echo $!>tpid