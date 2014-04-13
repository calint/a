#include"cap.h"
#include<stdlib.h>
#include<string.h>
struct file{
    void*address;
    size_t size_in_bytes;
};
size_t file_sizeof=sizeof(file);
size_t file_count=0;
file*file_new(void*address,const size_t size){// gives
	file_count++;
    file*o=(file*)malloc(sizeof(file));
    o->address=address;
    o->size_in_bytes=size;
    return o;
}
//file file_mk(void*address,const size_t size){// gives
//	file_count++;
//    file o;
//    o.address=address;
//    o.size_in_bytes=size;
//    return o;
//}
void file_free(file*o){
    file_count--;
    free(o);
}
//#define debug(fmt, ...) printf("%s:%d: "fmt,__FILE__,__LINE__,__VA_ARGS__);
void file_info(const file*o,FILE*f){
    //	debug("%p %lu\n",o->address,o->size_in_bytes);
	fprintf(f,"file{%p %lu}",o->address,o->size_in_bytes);
}
void file_to(const file*o,FILE*f){
    fwrite(o->address,o->size_in_bytes,1,f);
}
void file_foreach_char_write(const file*o,void(^callback)(char*)){
    char*p=o->address;
    size_t c=o->size_in_bytes;
    while(c--)
        callback(p++);
}
void file_foreach_char(const file*o,void(^callback)(char)){
    char*p=o->address;
    size_t c=o->size_in_bytes;
    while(c--)
        callback(*p++);
}
void file_copy(const file*o,const char*src,size_t count){
    if(count>o->size_in_bytes){d("!overflow %lu>%lu\n",count,o->size_in_bytes);exit(-1);}
    memcpy(o->address,src,count);
}
const size_t file_size_in_bytes(const file*f){
    return f->size_in_bytes;
}




struct type{
    struct type*parent;
    const char*name;
    struct func*ls_func;int ls_func_size;
    struct field*ls_field;int ls_field_size;
};
size_t type_sizeof=sizeof(type);
size_t type_count=0;



