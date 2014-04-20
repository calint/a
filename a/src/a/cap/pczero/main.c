#include "pc.h"
//#include "file.h"
//extern "C" void __gxx_personality_v0(){}

asm(".global tsk0,tsk1,tsk2,tsk3,tsk4");
asm("tsk0:");
asm("  movl $0x00007c00,%esi");
asm("  movl $0x000a8000,%edi");
asm("  movl $0x00001000,%ecx");
asm("  rep movsl");
asm("  hlt");
asm("  jmp tsk0");
asm(".align 16");
asm("tsk1:");
asm("  addl $2,0xa0144");
asm("  hlt");
asm("  jmp tsk1");
asm(".align 16");
asm("tsk2:");
asm("  addl $2,0xa0148");
asm("  hlt");
asm("  jmp tsk2");
asm(".align 16");
asm("tsk3:");
asm("  addl $2,0xa014c");
asm("  hlt");
asm("  jmp tsk3");
asm(".align 16");
asm("tsk4:");
asm("  addl $2,0xa0150");
asm("  hlt");
asm("  jmp tsk4");

void tsk5(){
	while(1){
        osca_pass();
		*(int*)0xa0154+=3;
	}
}
void tsk6(){
	static char*p=(char*)0xa0400;
	static char*nl=(char*)0xa0400;
	static char key_prev=0;
	while(1){
        osca_pass();
	    if(key_prev==(char)osca_key){
	        char c=*p;
	        *p=c+1;
	    }else{
		    *p=(char)osca_key;
		    if(osca_key==1){
		        nl+=320;
		        p=nl;
	    		/*p+=320;
	    		int r=((int)p-0xa0280)%320;
	    		p-=r;*/
	    	}else{
	    	    p++;
	    	    *p=0;
	    	}
		    key_prev=(char)osca_key;
        }
	}
}

void tsk7(){
	while(1){
        osca_pass();
        int*p=(int*)0xa1800;
        int c=0x010;
        while(c--)
        	*p++=osca_t;
	}
}

void tsk8(){
	while(1){
        osca_pass();
		*(long int*)0xa0480=osca_t;
	}
}

void tsk9(){
	while(1){
        osca_pass();
        int n;
        for(n=0;n<8;n++)
        	*(int*)(0xa0100+n*4)=osca_t;
	}
}

void _run();
void tsk10(){_run();}

void osca_keyb_ev(){
	static char*p=(char*)0xa4000;
	*p++=(char)osca_key;
}

// Sun Apr 20 17:42:17 ICT 2014
//#include<stdio.h>
//#define d(fmt, ...)printf("\n%s:%d: " fmt,__FILE__,__LINE__,__VA_ARGS__);
//#define nl()printf("\n");
//#define p(format,args...)fprintf(stdout,format,##args)
//#define pl(format,args...)p(format,##args);nl()
//#define pi(M, ...) fprintf(stderr, "[INFO] (%s:%d) " M "\n", __FILE__, __LINE__, ##__VA_ARGS__)
//#define pls(stream,format,args...)fprintf(stream,format,##args)
#define pl(format,args...) ;
#define pls(stream,format,args...) ;
#define out 1
#define null 0
//typedef size_t      size;
//typedef FILE*       stream;
typedef long      size;
typedef int       stream;
typedef const char* str;
#define int_default 0
#define float_default 0f
#define str_default ""
#define true 1
#define false 0
//typedef struct slot{}slot;
typedef struct field{
    const char*type;
    const char*name;
    size offset;
    size sze;
}field;
typedef struct function{
    const char*type;
    const char*name;
    const char*args;
    void(*func)();
}function;
typedef struct struc{
    const char*name;
    size nfields;
    const field*fields;
    size nfuncs;
    const function*funcs;
}struc;
#define offsetof(st, m) ((size)(&((st *)0)->m))
//- -- - -- - - - - - -  ---  -- - - - - - - - - - -   - - - - - - - - - - - - -
static void mem_clear(int address,int nbytes,int colr){
	int*p1=(int*)address;
	while(nbytes--)
		*p1++=colr;
//
//	char*p=address;
//	char c=(char)color;
//	while(nbytes>0)
//		*p++=color;
}
//void osca_keyb_ev();//called from keyboard interrupt when new keycode from keyboard
//static void osca_pass();

//volatile const int osca_key=0;//last received keycode (char)
//volatile const int osca_t=0;//time lower 32b
//volatile const int osca_t1=0;//time higher 32b
//inline void osca_pass(){asm("hlt");}
//tested on:
// * dell inspiron 1545
// * asus-eeepc-4g
// * hp-compaq-mini-110
// * sony-vaio-vgnfw11m
// * qemu 0.11.0 on linux 2.6
//-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
typedef struct address address;static struct address{
   int i;
}address_default={0,};
static const field address__field[]={
  {"int","i",offsetof(address,i),sizeof(int)},
};
static inline address address_mk(){return address_default;}///keep stack pointer
static inline int address_i(const address*o){return o->i;}
static inline void address_i_(address*o,int v){o->i=v;}
static inline void address_to(address*o,stream s){}
static const function address__func[]={
  {"void","to","stream",(void*)address_to},
};
typedef struct size_in_bytes size_in_bytes;static struct size_in_bytes{
   int i;
}size_in_bytes_default={0,};
static const field size_in_bytes__field[]={
  {"int","i",offsetof(size_in_bytes,i),sizeof(int)},
};
static inline size_in_bytes size_in_bytes_mk(){return size_in_bytes_default;}///keep stack pointer
static inline int size_in_bytes_i(const size_in_bytes*o){return o->i;}
static inline void size_in_bytes_i_(size_in_bytes*o,int v){o->i=v;}
static const function size_in_bytes__func[]={};
typedef struct name name;static struct name{
   str i;
}name_default={0,};
static const field name__field[]={
  {"str","i",offsetof(name,i),sizeof(str)},
};
static inline name name_mk(){return name_default;}///keep stack pointer
static inline str name_i(const name*o){return o->i;}
static inline void name_i_(name*o,str v){o->i=v;}
static const function name__func[]={};
typedef struct color color;static struct color{
   int i;
}color_default={0,};
static const field color__field[]={
  {"int","i",offsetof(color,i),sizeof(int)},
};
static inline color color_mk(){return color_default;}///keep stack pointer
static inline int color_i(const color*o){return o->i;}
static inline void color_i_(color*o,int v){o->i=v;}
static const function color__func[]={};
typedef struct file file;static struct file{
   address address;
   size_in_bytes size_in_bytes;
   name name;
}file_default={0,0,0,};
static const field file__field[]={
  {"address","address",offsetof(file,address),sizeof(address)},
  {"size_in_bytes","size_in_bytes",offsetof(file,size_in_bytes),sizeof(size_in_bytes)},
  {"name","name",offsetof(file,name),sizeof(name)},
};
static inline file file_mk(){return file_default;}///keep stack pointer
static inline address file_address(const file*o){return o->address;}
static inline void file_address_(file*o,address v){o->address=v;}
static inline size_in_bytes file_size_in_bytes(const file*o){return o->size_in_bytes;}
static inline void file_size_in_bytes_(file*o,size_in_bytes v){o->size_in_bytes=v;}
static inline name file_name(const file*o){return o->name;}
static inline void file_name_(file*o,name v){o->name=v;}
static inline void file_to(file*o,stream s){pls(s,"{%d %d}",o->address.i,o->size_in_bytes.i);}
static inline void file_to2(file*o,stream s){}
static inline address file_get_address(file*o){}
static inline void file_clear(file*o,color colr){
	mem_clear(o->address.i,o->size_in_bytes.i,colr.i);
}
static const function file__func[]={
  {"void","to","stream",(void*)file_to},
  {"void","to2","stream",(void*)file_to2},
  {"address","get_address","",(void*)file_get_address},
  {"void","clear","color",(void*)file_clear},
};
typedef struct width width;static struct width{
   int i;
}width_default={0,};
static const field width__field[]={
  {"int","i",offsetof(width,i),sizeof(int)},
};
static inline width width_mk(){return width_default;}///keep stack pointer
static inline int width_i(const width*o){return o->i;}
static inline void width_i_(width*o,int v){o->i=v;}
static const function width__func[]={};
typedef struct height height;static struct height{
   int i;
}height_default={0,};
static const field height__field[]={
  {"int","i",offsetof(height,i),sizeof(int)},
};
static inline height height_mk(){return height_default;}///keep stack pointer
static inline int height_i(const height*o){return o->i;}
static inline void height_i_(height*o,int v){o->i=v;}
static const function height__func[]={};
typedef struct width_in_pixels width_in_pixels;static struct width_in_pixels{
   width width;
}width_in_pixels_default={0,};
static const field width_in_pixels__field[]={
  {"width","width",offsetof(width_in_pixels,width),sizeof(width)},
};
static inline width_in_pixels width_in_pixels_mk(){return width_in_pixels_default;}///keep stack pointer
static inline width width_in_pixels_width(const width_in_pixels*o){return o->width;}
static inline void width_in_pixels_width_(width_in_pixels*o,width v){o->width=v;}
static const function width_in_pixels__func[]={};
typedef struct height_in_pixels height_in_pixels;static struct height_in_pixels{
   height height;
}height_in_pixels_default={0,};
static const field height_in_pixels__field[]={
  {"height","height",offsetof(height_in_pixels,height),sizeof(height)},
};
static inline height_in_pixels height_in_pixels_mk(){return height_in_pixels_default;}///keep stack pointer
static inline height height_in_pixels_height(const height_in_pixels*o){return o->height;}
static inline void height_in_pixels_height_(height_in_pixels*o,height v){o->height=v;}
static const function height_in_pixels__func[]={};
typedef struct bmp bmp;static struct bmp{
   file file;
   width_in_pixels width_in_pixels;
   height_in_pixels height_in_pixels;
}bmp_default={0,0,0,};
static const field bmp__field[]={
  {"file","file",offsetof(bmp,file),sizeof(file)},
  {"width_in_pixels","width_in_pixels",offsetof(bmp,width_in_pixels),sizeof(width_in_pixels)},
  {"height_in_pixels","height_in_pixels",offsetof(bmp,height_in_pixels),sizeof(height_in_pixels)},
};
static inline bmp bmp_mk(){return bmp_default;}///keep stack pointer
static inline file bmp_file(const bmp*o){return o->file;}
static inline void bmp_file_(bmp*o,file v){o->file=v;}
static inline width_in_pixels bmp_width_in_pixels(const bmp*o){return o->width_in_pixels;}
static inline void bmp_width_in_pixels_(bmp*o,width_in_pixels v){o->width_in_pixels=v;}
static inline height_in_pixels bmp_height_in_pixels(const bmp*o){return o->height_in_pixels;}
static inline void bmp_height_in_pixels_(bmp*o,height_in_pixels v){o->height_in_pixels=v;}
static inline void bmp_to(bmp*o,stream s){}
static const function bmp__func[]={
  {"void","to","stream",(void*)bmp_to},
};
typedef struct c c;static struct c{}c_default={};
static const field c__field[]={};
static inline c c_mk(){return c_default;}///keep stack pointer
static inline void cl(){
	file f=file_default;
	f.address.i=0xa5a00;
	f.size_in_bytes.i=0x20;
	int*p1=(int*)f.address.i;
	int c1=f.size_in_bytes.i;
	while(c1--)
		*p1++=3;
}
static inline void cl2(file*f,color colr){
	int*p1=(int*)f->address.i;
	int c1=f->size_in_bytes.i;
	while(c1--)
		*p1++=colr.i;
}
static inline void c_run(c*o){
	int a=1;
	while(1){
		file f=file_default;
		f.address.i=0xa5a00;
		f.size_in_bytes.i=0x20;
		file_clear(&f,(color){a++});
		osca_pass();
	}
}
static const function c__func[]={
  {"void","run","",(void*)c_run},
};
static const struct struc structs[]={
  {"address",sizeof(address__field)/sizeof(field),address__field,sizeof(address__func)/sizeof(function),address__func},
  {"size_in_bytes",sizeof(size_in_bytes__field)/sizeof(field),size_in_bytes__field,sizeof(size_in_bytes__func)/sizeof(function),size_in_bytes__func},
  {"name",sizeof(name__field)/sizeof(field),name__field,sizeof(name__func)/sizeof(function),name__func},
  {"color",sizeof(color__field)/sizeof(field),color__field,sizeof(color__func)/sizeof(function),color__func},
  {"file",sizeof(file__field)/sizeof(field),file__field,sizeof(file__func)/sizeof(function),file__func},
  {"width",sizeof(width__field)/sizeof(field),width__field,sizeof(width__func)/sizeof(function),width__func},
  {"height",sizeof(height__field)/sizeof(field),height__field,sizeof(height__func)/sizeof(function),height__func},
  {"width_in_pixels",sizeof(width_in_pixels__field)/sizeof(field),width_in_pixels__field,sizeof(width_in_pixels__func)/sizeof(function),width_in_pixels__func},
  {"height_in_pixels",sizeof(height_in_pixels__field)/sizeof(field),height_in_pixels__field,sizeof(height_in_pixels__func)/sizeof(function),height_in_pixels__func},
  {"bmp",sizeof(bmp__field)/sizeof(field),bmp__field,sizeof(bmp__func)/sizeof(function),bmp__func},
  {"c",sizeof(c__field)/sizeof(field),c__field,sizeof(c__func)/sizeof(function),c__func},
};
// - - -- -  -- - - - - -  --  --- -- - - - - - -- - - - - - - - - -- - - - - --
void _run(){
	c_run(null);
}

