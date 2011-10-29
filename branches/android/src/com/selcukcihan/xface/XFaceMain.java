/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is XfaceApp Application Library.
 *
 * The Initial Developer of the Original Code is
 * ITC-irst, TCC Division (http://tcc.fbk.eu) Trento / ITALY.
 * For info, contact: xface-info@fbk.eu or http://xface.fbk.eu
 * Portions created by the Initial Developer are Copyright (C) 2004 - 2008
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * - Selcuk Cihan (selcukcihan@gmail.com)
 * ***** END LICENSE BLOCK ***** */

package com.selcukcihan.xfacej;

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

import com.selcukcihan.xfacej.xengine.*;
import com.selcukcihan.xfacej.xmath.Vector3;

public class XFaceMain extends Frame
{
	protected GLCanvas canvas;
	protected Animator animator;
	
	private int m_Width;
	private int m_Height;
	private byte[] m_pData;
	private byte[] m_rgbArray;
	
	public boolean load(String filename)
	{
		try
		{
			File file = new File("filename");
			BufferedImage bufferedImage = ImageIO.read(file);
			m_Width = bufferedImage.getWidth();
			m_Height = bufferedImage.getHeight();
			
			m_pData = new byte[m_Width*m_Height*4];
			int [] m_rgbArray = new int[m_Width*m_Height];
			

			bufferedImage.getRGB(0, 0, m_Width, m_Height, m_rgbArray, 0, m_Width);
			
			//System.arraycopy(arg0, arg1, arg2, arg3, arg4)
			//m_pData = bufferedImage
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		return false;
	}

	public static void main(String[] args)
	{
		Frame frame = new Frame("xface-j");
		frame.setSize(800, 600);
		//frame.setLayout(new FlowLayout(FlowLayout.LEFT));
		frame.setLayout(new BorderLayout());
	    GLCanvas canvas = new GLCanvas();

	    //canvas.addGLEventListener((GLEventListener) new GLTest());
	    canvas.addGLEventListener((GLEventListener) new XFaceDriver(canvas));
	    canvas.setAutoSwapBufferMode(true);
	    frame.add(canvas, BorderLayout.NORTH);
	    //canvas.setSize(800, 600);
	    //frame.add(new Label("FPS: "), BorderLayout.SOUTH);
	    
	    final Animator animator = new Animator(canvas);
	    frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          new Thread(new Runnable() {
	              public void run() {
	                animator.stop();
	                System.exit(0);
	              }
	            }).start();
	        }
	      });
	    frame.setVisible(true);
	    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	    canvas.setSize(frame.getSize());
	    canvas.validate();
	    animator.setRunAsFastAsPossible(true);
	    animator.start();
	}

}
