package a.medusa;

import java.io.Serializable;

import a.medusa.medusa.reads;

public interface glo extends Serializable{
	public void load();
	public void draw(final screen s,final@reads float[]xy,final float angle);
}