#include<stdio.h>
#include<unistd.h>
void p(const char*s){puts(s);system(s);}
int main(int argc,char**argv){
	setuid(geteuid());
	p("exec /usr/sbin/iwconfig wlan0 essid calink");
	p("exec /sbin/ifconfig wlan0 up");
	p("exec /sbin/dhcpcd wlan0");
	p("exec /sbin/route add default gw 192.168.2.1 wlan0");
	return 0;
}
