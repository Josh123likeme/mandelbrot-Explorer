package me.Josh123likeme.Mandelbrot;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Generator {

	private static int sizeX = 1000;
	private static int sizeY = 1000;
	public static double focusX = -0.6d;
	public static double focusY = 0d;
	
	public static double zoom = 2.5d;
	public static int samples = 100;
	
	public static double progress = 0d;
	public static BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
	
	public static void generate() {
		
		samples = (int) (100 * 1 / zoom);
		
		for (int y = 0; y < sizeY; y++){
			
			progress = (double) y / sizeY;
			
			for (int x = 0; x < sizeX; x++){
				
				double re = 0;
				double im = 0;
				
				double reToAdd = ((zoom / sizeX) * (x - (sizeX / 2))) + focusX;
				double imToAdd = -((zoom / sizeY) * (y - (sizeY / 2))) + focusY;
				
				for (int i = 0; i < samples; i++){
					
					double newRe = re * re + -1 * im * im;
					double newIm = re * im * 2;
					
					newRe += reToAdd;
					newIm += imToAdd;
					
					re = newRe;
					im = newIm;
					
					if (Double.isNaN(re * re + im * im)){
						
						image.setRGB(x, y, (255 << 24) | (0 << 16) | (0 << 8) | (0 << 0));
						
						break;
						
					}
					
				}
				
				if (!Double.isNaN(re * re + im * im)){
					
					image.setRGB(x, y, (255 << 24) | (255 << 16) | (255 << 8) | (255 << 0));
					
				}
				
			}
			
		}
		
		File outputfile = new File("output.png");
		try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		progress = -1d;
		
	}
	
}
