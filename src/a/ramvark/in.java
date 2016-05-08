package a.ramvark;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
public @Retention(RetentionPolicy.RUNTIME)@interface in{
	int type()default 0;
	//type 0:text field
	//     1:textarea
	//     3:aggregate many
	//     4:editor
	//     5:number
	//     6:input list orderable
	//     7:refn
	Class<? extends lst>lst()default lst.class;
	Class<? extends itm>itm()default itm.class;
	boolean must()default false;
}
