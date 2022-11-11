package me.Josh123likeme.Mandelbrot;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class Generator {

	public static int sizeX = 1000;
	public static int sizeY = 1000;
	public static double focusX = -0.6d;
	public static double focusY = 0d;
	
	public static double zoom = 2.5d;
	
	public static int currentSamples = 0;
	public static long startTime = 0;
	
	public static int[][] samples = new int[sizeY][sizeX];
	
	public static void generate() {
		
		currentSamples = 0;
		startTime = System.currentTimeMillis();
		
		samples = new int[sizeY][sizeX];
		
		for (int y = 0; y < sizeY; y++){
			
			for (int x = 0; x < sizeX; x++){
				
				samples[y][x] = (255 << 24) | (255 << 16) | (255 << 8) | (255 << 0);
				
			}
			
		}
		
		double[][] res = new double[sizeY][sizeX];
		double[][] ims = new double[sizeY][sizeX];
		
		while (true){
			
			currentSamples++;
			
			for (int y = 0; y < sizeY; y++){
				
				for (int x = 0; x < sizeX; x++){
					
					if (samples[y][x] != ((255 << 24) | (255 << 16) | (255 << 8) | (255 << 0))) continue;
					
					double reToAdd = ((zoom / sizeX) * (x - (sizeX / 2))) + focusX;
					double imToAdd = -((zoom / sizeY) * (y - (sizeY / 2))) + focusY;
					
					double newRe = res[y][x] * res[y][x] + -1 * ims[y][x] * ims[y][x];
					double newIm = res[y][x] * ims[y][x] * 2;
					
					newRe += reToAdd;
					newIm += imToAdd;
					
					res[y][x] = newRe;
					ims[y][x] = newIm;
					
					if (Double.isNaN(res[y][x] * res[y][x] + ims[y][x] * ims[y][x])){
						
						samples[y][x] = currentSamples;
						
						
					}
					
				}
				
			}
			
		}
		
	}
	
	public static int hashColour(int samples){
		
		if (samples == ((255 << 24) | (255 << 16) | (255 << 8) | (255 << 0))) return samples;
		
		int r = (samples * 69) % 256;
		int g = (samples * 172) % 256;
		int b = (samples * 37) % 256;
		
		return (255 << 24) | (r << 16) | (g << 8) | (b << 0);
		
	}
	
}
