package me.Josh123likeme.Mandelbrot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
		
		new Game();
		
		Generator.generate();
		
		while (true){
			
			BufferedReader reader = new BufferedReader(
		            new InputStreamReader(System.in));
		 
		        
	        String input = reader.readLine();
	 
	        switch (input) {
	        
	        	case "zoom":
	        		
	        		Generator.zoom /= 2;
	        		
	        		break;
	        		
	        	case "out":
	        		
	        		Generator.zoom *= 2;
	        		
	        		break;
	        		
	        	case "up":
	        		
	        		Generator.focusY += 0.1 * Generator.zoom;
	        		
	        		break;
	        		
	        	case "left":
	        		
	        		Generator.focusX -= 0.1 * Generator.zoom;
	        		
	        		break;
	        		
	        	case "down":
	        		
	        		Generator.focusY -= 0.1 * Generator.zoom;
	        		
	        		break;
	        		
	        	case "right":
	        		
	        		Generator.focusX += 0.1 * Generator.zoom;
	        		
	        		break;
	        	
	        }
		        
		    Generator.generate();  
			
		}
	
	}
	
}
