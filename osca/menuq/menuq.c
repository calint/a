#include<X11/Xlib.h>
#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>
#include<X11/keysym.h>
#include <X11/Xutil.h>
int main(){
	puts("menuq");
	Display*dpy=XOpenDisplay(NULL);
	if(!dpy){
		//fprintf(stderr, "!!! could not open display\n");
		return 1;
	}
	int scr=DefaultScreen(dpy);
	unsigned int scr_w=DisplayWidth(dpy,scr);
	Window win=XCreateSimpleWindow(dpy,RootWindow(dpy,scr),0,0,scr_w,23,0,BlackPixel(dpy,scr),BlackPixel(dpy,scr));
	XSelectInput(dpy,win,KeyPressMask);
	XMapWindow(dpy,win);
	GC gc=XCreateGC(dpy,win,0,NULL);
	XSetForeground(dpy,gc,WhitePixel(dpy,scr));
	int x=(scr_w>>1)-(scr_w>>2)+(scr_w>>3);
	int y=(23>>1)+(23>>2);
	XDrawString(dpy,win,gc,x,y,"_",1);
	int keycode=0;
//	char*str;
	const int buflen=32;
	char buf[buflen];
	int bufi=0;
	char*bufp=buf;
	int go=1;
	while(go){
		XEvent e;
		XNextEvent(dpy,&e);
		switch(e.type){
		default:
			break;
		case DestroyNotify:
			go=0;
			break;
		case KeyPress:
			//printf("KeyPress %d %d\n",e.xbutton.button,e.xbutton.state);
			keycode=e.xkey.keycode;
			if(keycode==9){//esc
				go=0;
				bufp=buf;
				*bufp=0;
				break;
			}
			keycode=e.xkey.keycode;
			if(keycode==36){//return
				go=0;
				*bufp=0;
				break;
			}
			if(keycode==22){//backspace
				if(bufi==0)
					break;
				x-=7;
				XSetForeground(dpy,gc,BlackPixel(dpy,scr));
				XFillRectangle(dpy,win,gc,x,0,14,23);
				XSetForeground(dpy,gc,WhitePixel(dpy,scr));
				XDrawString(dpy,win,gc,x,y,"_",1);
				bufi--;
				bufp--;
				*bufp=0;
			}else{
				XSetForeground(dpy,gc,BlackPixel(dpy,scr));
				XFillRectangle(dpy,win,gc,x,0,7,23);
				XSetForeground(dpy,gc,WhitePixel(dpy,scr));

				char buffer[20];
				KeySym keysym;
				//XComposeStatus compose;
				XLookupString(&e.xkey,buffer,sizeof buffer,&keysym,NULL);
				//str=XKeysymToString(XKeycodeToKeysym(dpy,keycode,0));
				//if(keycode==65){//space
				//	str=" ";
				//}else if(keycode==61){
				//	str="-";
				//}
				XDrawString(dpy,win,gc,x,y,buffer,1);
				x+=7;
				*bufp++=buffer[0];
				bufi++;
				if(bufi>buflen)
					go=0;
				y+=-1+rand()%3;
				XDrawString(dpy,win,gc,x,y,"_",1);
			}
			XFillRectangle(dpy,win,gc,640,0,800,22);
		}
	}
	XCloseDisplay(dpy);
//	puts(buf);
	strcat(buf,"&");
	return system(buf);
}
