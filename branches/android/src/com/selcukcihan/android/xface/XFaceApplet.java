
package com.selcukcihan.android.xface;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import com.sun.opengl.util.Animator;

import com.selcukcihan.android.xface.xmath.Vector3;

public class XFaceApplet extends Applet
{
	private XFaceApplet m_applet;
	protected GLCanvas canvas;
	protected Animator animator;
	
	private int m_Width;
	private int m_Height;
	private byte[] m_pData;
	private byte[] m_rgbArray;

	public void init()
	{
		//Frame frame = new Frame("xface-j");
		//XFaceApplet xfaceApplet = new XFaceApplet();
		//frame.add(xfaceApplet);
		setSize(640,480);
		setLayout(new BorderLayout());
		
	    GLCanvas canvas = new GLCanvas();

	    //canvas.addGLEventListener((GLEventListener) new GLTest());
	    canvas.addGLEventListener((GLEventListener) new XFaceDriver(canvas));
	    canvas.setAutoSwapBufferMode(true);
	    add(canvas, BorderLayout.NORTH);
	    //frame.add(new Label("FPS: "), BorderLayout.SOUTH);
	    
	    animator = new Animator(canvas);
	    /*frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          new Thread(new Runnable() {
	              public void run() {
	                animator.stop();
	                System.exit(0);
	              }
	            }).start();
	        }
	      });*/
	    setVisible(true);
	    //setExtendedState(Frame.MAXIMIZED_BOTH);
	    canvas.setSize(getSize());
	    canvas.validate();
	    animator.setRunAsFastAsPossible(true);
	}
	
	public void start()
	{
		animator.start();
	}
	
	public void stop()
	{
		animator.stop();
	}
	
	public void destroy()
	{
		
	}
}
