#ifndef pc_h
#define pc_h
void osca_keyb_ev();//called from keyboard interrupt when new keycode from keyboard
extern volatile const int osca_key;//last received keycode (char)
extern volatile const int osca_t;//time lower 32b
extern volatile const int osca_t1;//time higher 32b
void osca_pass();
void tsk1();
void tsk2();
void tsk3();
void tsk4();
void tsk5();
void tsk6();
void tsk7();
void tsk8();
void tsk9();
void tsk10();
#endif

