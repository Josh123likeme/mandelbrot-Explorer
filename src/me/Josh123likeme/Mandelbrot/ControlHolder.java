package me.Josh123likeme.Mandelbrot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ControlHolder {

	private static List<ButtonType> buttons = new ArrayList<ButtonType>();
	private static List<Integer> keys = new ArrayList<Integer>();
	
	public static ButtonType getButton(int keyCode) {
		
		if (keys.indexOf(keyCode) == -1) throw new IllegalArgumentException("No binding exists for that key");
		
		return buttons.get(keys.indexOf(keyCode));
		
	}
	
	public static int getKey(ButtonType button) {
		
		if (buttons.indexOf(button) == -1) throw new IllegalArgumentException("No binding exists for that button");
		
		return keys.get(buttons.indexOf(button));
		
	}
	
	public static void loadBinds() {

		for (ButtonType button : ButtonType.values()) {
			
			buttons.add(button);
			keys.add(button.key);
			
		}
		
	}
	
	public enum ButtonType {
		
		MOVE_UP(87),
		MOVE_LEFT(65),
		MOVE_DOWN(83),
		MOVE_RIGHT(68),
		ZOOM_IN(61),
		ZOOM_OUT(45),
		RENDER(82),
		
		;
		
		int key;
		
		ButtonType(int key) {
			
			this.key = key;
			
		}
		
	}
	
}
