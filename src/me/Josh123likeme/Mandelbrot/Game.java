package me.Josh123likeme.Mandelbrot;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import me.Josh123likeme.Mandelbrot.ControlHolder.ButtonType;


public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int INITIAL_WIDTH = 400, INITIAL_HEIGHT = 400;
	
	private MouseWitness mouseWitness;
	private KeyboardWitness keyboardWitness;
	
	private Thread thread;
	private boolean running = false;
	
	private Thread generatorThread;
	private double deltaFrame = 0;
	
	private boolean prevDragging;
	
	private int prevMouseX;
	private int prevMouseY;
	
	public Game() {
		
		ControlHolder.loadBinds();
		
		mouseWitness = new MouseWitness();
		keyboardWitness = new KeyboardWitness();
		
		addMouseListener(mouseWitness);
		addMouseMotionListener(mouseWitness);
		addKeyListener(keyboardWitness);
		
		new Window(INITIAL_WIDTH, INITIAL_HEIGHT, "Window Name", this);
		
		requestFocus();
		
	}
	
	public synchronized void start() {
		
		thread = new Thread(this);
		thread.start();
		running = true;
		
	}
	
	public synchronized void stop() {
		
		try 
		{
			thread.join();
			running = false;
		}
		
		catch(Exception e) {e.printStackTrace();}
		
	}
	
	public void run() {
		
		double targetfps = 10000000d;
		long targetDeltaFrame = Math.round((1d / targetfps) * 1000000000);
		long lastSecond = System.nanoTime();
		int frames = 0;
		
		long lastFrame = 0;;
		
		while (running) {
			
			frames++;
			
			//starting to push frame
			
			long nextTime = System.nanoTime() + targetDeltaFrame;
			
			deltaFrame = ((double) (System.nanoTime() - lastFrame)) / 1000000000;
			
			lastFrame = System.nanoTime();
			
			paint();
			
			//finished pushing frame
			
			while (nextTime > System.nanoTime());
			
		}
		stop();	
		
	}
	
	private void tick() {
		
	}

	private void paint() {
	
		BufferStrategy bufferStrategy = this.getBufferStrategy();
		if(bufferStrategy == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics graphics = bufferStrategy.getDrawGraphics();
		
		//basic black background to stop flashing
		graphics.setColor(Color.black); 
		graphics.fillRect(0, 0, getWidth(), getHeight());
		
		boolean changed = false;
		
		if (keyboardWitness.isButtonHeld(ButtonType.ZOOM_IN)){
			
			Generator.zoom /= (2 * deltaFrame) + 1;
			changed = true;
		}
		if (keyboardWitness.isButtonHeld(ButtonType.ZOOM_OUT)){
			
			Generator.zoom *= (2 * deltaFrame) + 1;
			changed = true;
		}
		if (keyboardWitness.isButtonHeld(ButtonType.MOVE_UP)){
			
			Generator.focusY += 0.1 * Generator.zoom * deltaFrame;
			changed = true;
		}
		if (keyboardWitness.isButtonHeld(ButtonType.MOVE_LEFT)){
			
			Generator.focusX -= 0.1 * Generator.zoom * deltaFrame;
			changed = true;
		}
		if (keyboardWitness.isButtonHeld(ButtonType.MOVE_DOWN)){
	
			Generator.focusY -= 0.1 * Generator.zoom * deltaFrame;
			changed = true;
		}
		if (keyboardWitness.isButtonHeld(ButtonType.MOVE_RIGHT)){
	
			Generator.focusX += 0.1 * Generator.zoom * deltaFrame;
			changed = true;
		}
		
		if (mouseWitness.isDragging() && !prevDragging){
			
			prevDragging = true;
			
			prevMouseX = mouseWitness.getMouseX();
			prevMouseY = mouseWitness.getMouseY();
			
		}
		
		if (!mouseWitness.isDragging() && prevDragging){
			
			prevDragging = false;
			
			double deltaX = mouseWitness.getMouseX() - prevMouseX;
			double deltaY = mouseWitness.getMouseY() - prevMouseY;
			
			Generator.focusX -= deltaX * (Generator.zoom / Generator.sizeX);
			Generator.focusY += deltaY * (Generator.zoom / Generator.sizeY);
			
			changed = true;
			
		}
		
		if (changed) {
			
			if (generatorThread != null) generatorThread.stop();
			
			generatorThread = new Thread(new Runnable() {
			    @Override
			    public void run() {
			        Generator.generate();
			    }
			});  
			generatorThread.start();
			
		}
		
		//put rendering stuff here
		
		BufferedImage image = new BufferedImage(Generator.sizeX, Generator.sizeY, BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y < image.getHeight(); y++){
			
			for (int x = 0; x < image.getWidth(); x++){
				
				image.setRGB(x, y, Generator.hashColour(Generator.samples[y][x]));
				
			}
			
		}
		
		graphics.drawImage(image, 0, 0, getHeight() - 10, getHeight() - 10, null);
		
		if (keyboardWitness.isButtonHeld(ButtonType.RENDER)){
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date date = new Date();
			
			File outputfile = new File(dateFormat.format(date) + ".png");
			try {
				ImageIO.write(image, "png", outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		graphics.setColor(Color.black);
		
		graphics.fillRect(0, 0, 300, 60);
		
		graphics.setColor(Color.white);
		
		graphics.drawString("Current Samples: " + Generator.currentSamples, 10, 10);
		
		long time = System.currentTimeMillis() - Generator.startTime;
		
		int hours = (int) (time / (60 * 60 * 1000));
		time = time % (60 * 60 * 1000);
		int minutes = (int) (time / (60 * 1000));
		time = time % (60 * 1000);
		int seconds = (int) (time / 1000);
		time = time % 1000;
		int milliseconds = (int) time;
		
		String strTime = String.format("%1$" + 2 + "s", hours).replace(' ', '0') + ":" +
				String.format("%1$" + 2 + "s", minutes).replace(' ', '0') + ":" +
				String.format("%1$" + 2 + "s", seconds).replace(' ', '0') + "." +
				String.format("%1$" + 3 + "s", milliseconds).replace(' ', '0');
		
		graphics.drawString("Current uptime: " + strTime, 10, 25);
		graphics.drawString("Current Zoom: " + 1 / Generator.zoom, 10, 40);
		graphics.drawString("Current Focus: " + Generator.focusX + " + " + Generator.focusY + "i", 10, 55);
		
		//this pushes the graphics to the window
		bufferStrategy.show();
		
	}
	
}
