package me.Josh123likeme.Mandelbrot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseWitness implements MouseListener, MouseMotionListener {

	private int mouseX, mouseY;
	private boolean dragging;
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		mouseX = e.getX();
		mouseY = e.getY();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		dragging = false;
		
		mouseX = e.getX();
		mouseY = e.getY();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		/*
		
		 if (e.getButton() == MouseEvent.BUTTON1)
		 
		 */
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		dragging = true;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		dragging = false;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public int getMouseX() {
		
		return mouseX;
		
	}
	
	public int getMouseY() {
		
		return mouseY;
		
	}
	
	public boolean isDragging() {
		
		return dragging;
		
	}
	
}
