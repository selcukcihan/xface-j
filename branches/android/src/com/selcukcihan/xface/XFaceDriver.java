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

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.j2d.TextRenderer;

import com.selcukcihan.xfacej.xengine.Drawable;
import com.selcukcihan.xfacej.xengine.IRenderer;
import com.selcukcihan.xfacej.xengine.RenderManager;
import com.selcukcihan.xfacej.xengine.RendererGL;
import com.selcukcihan.xfacej.xengine.VRML97Loader;
import com.selcukcihan.xfacej.xfaceapp.ModelCamera;
import com.selcukcihan.xfacej.xfaceapp.ModelCamera.kMODE;


public class XFaceDriver implements GLEventListener
{
	XFaceApplication m_pApp;
	ModelCamera m_pCamera;
	
	Thread m_animThread;
	Thread m_soundThread;
	
	public GL m_gl;
	public XFaceSound m_sound;
	private long m_count = 0;
	private long m_time = 0;
	private int m_fps = 0;
	
	private GLCanvas m_canvas;
	private TextRenderer m_textRenderer;
	
	public XFaceDriver(GLCanvas p_canvas)
	{
		m_canvas = p_canvas;
	}
	
	public void display(GLAutoDrawable p_autoDrawable)
	{
//		try
//		{
//			m_pApp.wait();
//		} catch (InterruptedException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if(m_pApp == null)
			return;
		while(!m_pApp.acquireLock(false));
		long renderTime = System.currentTimeMillis();
		
		//if((m_count % 100) == 0)
		//	System.out.println("display: " + m_count);
		m_count++;
		
		//p_autoDrawable.setGL(new DebugGL(p_autoDrawable.getGL()));
		m_gl = p_autoDrawable.getGL();

		//final GL m_gl = p_autoDrawable.getGL();
		if(m_pApp == null)
			return;
		if(m_pCamera == null)
		{
			int h = p_autoDrawable.getHeight();
			int w = p_autoDrawable.getWidth();
			m_gl.glViewport(0, 0, w, h);
			m_gl.glMatrixMode(GL.GL_PROJECTION);
			m_gl.glLoadIdentity();
			
			final GLU glu = new GLU();
			float ratio = (float)w / (float)h;
			glu.gluPerspective(30, ratio, 10.0, 1000.0);
			
			m_gl.glMatrixMode(GL.GL_MODELVIEW);
			
			m_pCamera = new ModelCamera();
			m_pCamera.setScreenSize(w, h);
			m_pCamera.setDistance(-700);
			m_pCamera.setMode(kMODE.ZOOM);
		}
		m_pCamera.apply(m_gl);
		
		m_pApp.onRenderFrame(m_gl);
		
		//m_canvas.repaint();
		
		renderTime = System.currentTimeMillis() - renderTime;
		m_textRenderer.beginRendering(p_autoDrawable.getWidth(), p_autoDrawable.getHeight());
		m_textRenderer.setColor(1.0f, 0.2f, 0.2f, 0.8f);
		long now = System.currentTimeMillis();
		if(now - m_time > 1000)
		{
			m_fps = (int)(m_count * 1000.0 / (now - m_time));
			System.out.println("display: " + m_count);
			m_count = 0;
			m_time = now;
		}		
		m_textRenderer.draw("" + m_fps + " FPS", 100, 100);
		//m_textRenderer.draw("" + (int)(1000.0 / renderTime) + " FPS", 100, 100);
		m_textRenderer.endRendering();

		m_pApp.releaseLock();
		//m_pApp.notify();
	}

	public void displayChanged(GLAutoDrawable p_autoDrawable, boolean p_modeChanged, boolean p_deviceChanged)
	{
		
	}
	
	public void testFile()
	{
		Scanner input = null;
		URL url = XFaceDriver.class.getResource("deneme.txt");
		//URL url = new URL("http://localhost/deneme.txt");
		//System.out.println(url.getFile());
		//input = new Scanner(XFaceDriver.class.getResourceAsStream("deneme.txt"));
		if(input != null)
		{
			System.out.println("hayde breee");
		}
		input.close();
	}

	public void init(GLAutoDrawable p_autoDrawable)
	{
		//testFile();
		m_textRenderer = new TextRenderer(new Font("Segoe Print", Font.BOLD, 36));
		
		p_autoDrawable.setGL(new DebugGL(p_autoDrawable.getGL()));
		m_gl = p_autoDrawable.getGL();
		
		m_gl.glShadeModel(GL.GL_SMOOTH);
		m_gl.glCullFace(GL.GL_BACK);
		m_gl.glFrontFace(GL.GL_CCW);
		m_gl.glEnable(GL.GL_CULL_FACE);
		m_gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		m_gl.glEnable(GL.GL_DEPTH_TEST);
		m_gl.glEnable(GL.GL_LIGHTING);
		m_gl.glEnable(GL.GL_LIGHT0);
		m_gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		m_gl.glMatrixMode(GL.GL_MODELVIEW);
		
		m_textRenderer.beginRendering(p_autoDrawable.getWidth(), p_autoDrawable.getHeight());
		m_textRenderer.setColor(1.0f, 0.2f, 0.2f, 0.8f);
		m_textRenderer.draw("xface-j Loading...", 300, 200);
		m_textRenderer.endRendering();

		m_pApp = new XFaceApplication(m_canvas);
		m_pCamera = new ModelCamera();
		
		m_pApp.onLoadFDP("alice.fdp", "face\\", m_gl);
		m_pCamera.setAxisAngle(m_pApp.m_pFace.getFDP().getGlobalAxisAngle(), m_gl);
		m_pCamera.setTranslation(m_pApp.m_pFace.getFDP().getGlobalTranslation());
		
		m_pApp.onLoadPHO("face\\say-suffering-angina.pho", "english");
		
		m_gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		m_sound = new XFaceSound();
		m_soundThread = new Thread(m_sound, "sound");
		
		m_animThread = new Thread("playback")
		{
			public void run()
			{
				//while(true)
				{
					m_pApp.onResumePlayback(m_gl, m_sound);
					m_pApp.onStopPlayback(m_gl);
				}
			}
		};
		
		
		m_animThread.start();
		//m_soundThread.start();
		//m_pApp.onResumePlayback(m_gl);
		//m_pApp.onStopPlayback(m_gl);
	}

	public void reshape(GLAutoDrawable p_autoDrawable, int p_x, int p_y, int p_width, int p_height)
	{
		/*
		 * event
		 */
		//final GL gl = p_autoDrawable.getGL();
		p_autoDrawable.setGL(new DebugGL(p_autoDrawable.getGL()));
		m_gl = p_autoDrawable.getGL();
		
		final GLU glu = new GLU();
		
		m_gl.glViewport(0, 0, p_width, p_height);
		m_gl.glMatrixMode(GL.GL_PROJECTION);
		m_gl.glLoadIdentity();
	    
		float ratio = (float)p_width / (float)p_height;
		glu.gluPerspective(30.0, ratio, 10.0, 1000.0);
		m_gl.glMatrixMode(GL.GL_MODELVIEW);

		// Camera creation takes place here
		if(m_pCamera == null)
		{
			m_pCamera = new ModelCamera();
			m_pCamera.setScreenSize(p_width, p_height);
			m_pCamera.setDistance(-700);
			m_pCamera.setMode(ModelCamera.kMODE.ZOOM);
		}
		else
		{
			m_pCamera.setScreenSize(p_width, p_height);
			m_pCamera.apply(m_gl);
		}
    }

}
