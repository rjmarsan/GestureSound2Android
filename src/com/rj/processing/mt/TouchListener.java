package com.rj.processing.mt;


public interface TouchListener {
	public void touchDown(Cursor c);
	public void touchUp(Cursor c);
	public void touchMoved(Cursor c);
	public void touchAllUp(Cursor c);
}
