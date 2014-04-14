#ifndef cap_h
#define cap_h
#include<stdio.h>

static char ram[]="hellos. bootsector.  32 bytes..";
int cap_main(const int argc,const char**arg);
#define out stdout
typedef size_t size;
/// types
typedef struct file file;size file_count;size file_sizeof;
typedef struct type type;size type_count;size type_sizeof;

/// file
file*file_new(void*,const size);//gives
//file file_mk(void*,const size_t);
void file_free(file*);//takes
void file_info(const file*,FILE*);
void file_to(const file*,FILE*);
void file_foreach_char_write(const file*,void(^)(char*));
void file_foreach_char(const file*,void(^)(char));
void file_copy(const file*,const char*,const size);
const size file_size_in_bytes(const file*);
//#define vfunc(o,t,f, args...)t##_##f##(##o##,##args##)

struct func;typedef struct func func;
struct field;typedef struct field field;
struct object;typedef struct object object;


#define d(fmt, ...) printf("\n%s:%d: "fmt,__FILE__,__LINE__,__VA_ARGS__);
#define nl()printf("\n");
#define p(format,args...)fprintf(stdout,format,##args)
#define pl(format,args...)p(format,##args);nl()

#endif
