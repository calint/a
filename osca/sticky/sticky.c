// requires https://download.gnome.org/sources/gtksourceview/3.12/gtksourceview-3.12.3.tar.xz
#define APP "sticky"
#include<gtk/gtk.h>
#include<gtksourceview/gtksourceview.h>
#define WIDTH 200
#define HEIGHT 200
void on_destroy(/*GtkWidget*widget,gpointer data*/){
	gtk_main_quit();
}
static GtkWidget*text_view;
static gboolean on_key_press(GtkWidget*widget,GdkEventKey *event){
    printf("%s %s %s\n",(event->state&GDK_CONTROL_MASK)?"ctrl":"",(event->state&GDK_SHIFT_MASK)?"shift":"",gdk_keyval_name(event->keyval));
//	gtk_widget_set_sensitive(text_view,FALSE);
	GtkTextBuffer*buffer=gtk_text_view_get_buffer(GTK_TEXT_VIEW(text_view));
	GtkTextIter start,end;
	gtk_text_buffer_get_start_iter(buffer,&start);
	gtk_text_buffer_get_end_iter(buffer,&end);
	char*text=gtk_text_buffer_get_text(buffer,&start,&end,FALSE);
	printf("[%s]\n",text);       
	gtk_text_buffer_set_modified(buffer,FALSE);
//	gtk_widget_set_sensitive(text_view,TRUE);
	GError*err=NULL;
	const char*filename="sticky-note.txt";
	gboolean result=g_file_set_contents(filename,text,-1,&err);
	if(result==FALSE){
		puts(err->message);
		g_error_free(err);
	}        
	g_free(text); 
	return FALSE;
}

int main(int argc,char**argv){
	gtk_init(&argc,&argv);
	GdkDisplay*display=gdk_display_get_default();
	GdkScreen*screen=gdk_display_get_default_screen(display);
	GtkWidget*window=gtk_window_new(GTK_WINDOW_TOPLEVEL);
	g_signal_connect(G_OBJECT(window),"destroy",G_CALLBACK(on_destroy),NULL);
    g_signal_connect(G_OBJECT(window),"key-press-event",G_CALLBACK(on_key_press),NULL);

	GtkCssProvider*provider=gtk_css_provider_new();
	gtk_style_context_add_provider_for_screen(screen,GTK_STYLE_PROVIDER(provider),GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
//	GError*err;
//	gtk_css_provider_load_from_path(GTK_CSS_PROVIDER(provider),"~/.gconf/sticky/sticky.css",&err);
//	printf(" loading from path: %d  %s\n",err->code,err->message);
	gtk_css_provider_load_from_data(GTK_CSS_PROVIDER(provider),
		"GtkWindow{\n"
		"	background-color:yellow;\n"
		"	font-family:monospace;\n"
		"	font-size:1em;\n"
		"}\n"
		"GtkTextView{\n"
		"	background-color:inherit;\n"
		"}\n"
		,-1,NULL);
	g_object_unref(provider);
	gtk_window_set_title(GTK_WINDOW(window),APP);
	gtk_window_set_default_size(GTK_WINDOW(window),WIDTH,HEIGHT);//? magicnum
//	gtk_window_set_resizable(GTK_WINDOW(window),FALSE);
//? set position at pointer
	text_view=gtk_source_view_new();
	gtk_container_add(GTK_CONTAINER(window),text_view);
	gtk_widget_show_all(window);
	gtk_main();
	return 0;
}

