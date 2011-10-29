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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLContext;

import com.selcukcihan.xfacej.xengine.Drawable;
import com.selcukcihan.xfacej.xengine.Entity;
import com.selcukcihan.xfacej.xengine.IRenderer;
import com.selcukcihan.xfacej.xengine.ITimer;
import com.selcukcihan.xfacej.xengine.MorphController;
import com.selcukcihan.xfacej.xengine.RenderManager;
import com.selcukcihan.xfacej.xengine.RendererGL;
import com.selcukcihan.xfacej.xface.FAPFile;
import com.selcukcihan.xfacej.xface.FaceBase;
import com.selcukcihan.xfacej.xface.IFapStream;
import com.selcukcihan.xfacej.xfaceapp.ISound;


public class XFaceApplication
{
	private volatile boolean m_lock = false;
	
	public FaceBase m_pFace; /* multithread dikkat, sadece initte henuz tek thread varken access */
	
	private static int m_frame_count = 0;
	private static int m_missed_frames = 0;
	private static float m_frame_time = -1;
	
	//public GL m_gl;
	
	private enum RenderMode {FAP, KEYFRAME}
	private RenderMode m_renderMode;
	
	private boolean m_bBusy;
	
	private int m_sequenceDuration;
	private RenderManager m_renderManager;
	private ISound m_pSound;
	private ITimer m_pTimer;
	private IRenderer m_pRenderer;
	private IFapStream m_pFapStream;
	
	private GLCanvas m_canvas;
	
	public XFaceApplication(GLCanvas p_canvas)
	{
		//m_gl = p_gl;
		m_canvas = p_canvas;
		
		m_pRenderer = new RendererGL();
		m_renderManager = new RenderManager();
		m_pFace = new FaceBase();
		m_pTimer = new XFaceTimer();
		m_pFapStream = new FAPFile();
		
		m_renderManager.setRenderer(m_pRenderer);
		
		/*
		File dir = new File("face\\lang");
		String [] files = dir.list();
		if(files != null)
		{
			for(String file : files)
			{
				if(file.equals(".") || file.equals(".."))
					continue;
				else
					m_pFace.addPhonemeDictionary("face\\lang\\" + file);
			}
		}*/
		m_pFace.addPhonemeDictionary(XFaceApplication.class.getResourceAsStream("face/lang/enSAPI.dic"));
	}
	
	public synchronized boolean acquireLock(boolean p_persist)
	{
		if(p_persist)
		{
			while(m_lock);
			m_lock = true;
			return true;
		}
		else
		{
			if(m_lock)
				return false;
			else
			{
				m_lock = true;
				return true;
			}
		}
	}
	
	public synchronized boolean releaseLock()
	{
		m_lock = false;
		return true;
	}
	
	public void renderBegin(GL p_gl)
	{
		p_gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		p_gl.glMatrixMode(GL.GL_MODELVIEW);
		p_gl.glPushMatrix();
	}
	
	public void renderEnd(GL p_gl)
	{
		p_gl.glPopMatrix();
		//swap buffers yap
	}
	
	public void onRenderFrame(GL p_gl)
	{
		renderBegin(p_gl);
		m_renderManager.render(p_gl);
		renderEnd(p_gl);
	}
	
	public void onAdvanceFrame()
	{
		if(m_renderMode == RenderMode.FAP)
		{
			float [] FAPs = m_pFapStream.getCurrentFAP();
			if(FAPs != null)
			{
				if(FAPs.length > 0)
				{
					m_pFace.update(m_pFapStream);
					m_renderManager.setGlobalTransform(m_pFace.getTransform());
					m_pFapStream.next();
				}
			}
		}
	}
	
	public boolean onResumePlayback(GL p_gl, XFaceSound xfaceSound)
	{
		while(!acquireLock(false));
//		try
//		{
//			this.wait();
//		} catch (InterruptedException e1)
//		{
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		System.out.println("resume");
		
		if(m_bBusy)
			return false;
		if(m_renderMode == RenderMode.FAP)
			assert(m_pFapStream.isOpen());
		m_bBusy = true;
		if(m_renderMode == RenderMode.FAP)
			synchronize(true);
		
		if(m_renderMode == RenderMode.FAP)
		{
			while(!m_pFapStream.isEnd())
			{
				onAdvanceFrame();
				//m_canvas.repaint();
				//onRenderFrame(p_gl);
				
				synchronize(false);
				Thread.yield();
			}
		}
		else if((m_renderMode == RenderMode.KEYFRAME) && (m_sequenceDuration > 0))
		{
			MorphController con = MorphController.getInstance();
			con.rewind();
			m_pTimer.startTimer();
			xfaceSound.m_play = true;
			int sequenceDuration = m_sequenceDuration;
			LinkedList<Drawable> oldDrawables = m_pFace.getDrawables();
			while(sequenceDuration > 0)
			{
				//onRenderFrame(p_gl);
				int elapsed = (int) m_pTimer.getElapsedTime(true);
				final Entity res = m_pFace.update(elapsed);
				sequenceDuration -= elapsed;
				//System.out.println("while resume: " + sequenceDuration);
				m_renderManager.setGlobalTransform(m_pFace.getTransform());
				m_renderManager.resetDrawables();
				m_renderManager.addDrawables(res.getDrawables());
				
				releaseLock();
				//this.notify();
				//m_canvas.repaint();
				Thread.yield();
				//Thread.yield();
				while(!acquireLock(false));
//				try
//				{
//					this.wait();
//				} catch (InterruptedException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			}
			m_renderManager.resetDrawables();
			m_renderManager.addDrawables(oldDrawables);
			//onRenderFrame(p_gl);
		}
		m_bBusy = false;
		System.out.println("bitsin");
		
		releaseLock();
		//this.notify();
		
		return true;
	}
	
	public void onStopPlayback(GL p_gl)
	{
		acquireLock(true);
		
		System.out.println("stop");
		
		if(m_renderMode == RenderMode.FAP)
		{
			m_pFapStream.rewind();
			float [] FAPs = m_pFapStream.getCurrentFAP();
			if(FAPs != null)
			{
				if(FAPs.length > 0)
				{
					m_pFace.update(m_pFapStream);
					m_renderManager.setGlobalTransform(m_pFace.getTransform());
					
					renderBegin(p_gl);
					m_renderManager.render(p_gl);
					renderEnd(p_gl);
				}
			}
		}
		m_pFace.resetDeformedVertices();
		
		releaseLock();
	}
	
	public void onRewindPlayback()
	{
		if(m_pFapStream.isOpen())
			m_pFapStream.rewind();
		m_pFace.rewindKeyframeAnimation();
	}
	
	public boolean onLoadFDP(String p_filename, String p_path, GL p_gl)
	{
		if(m_bBusy)
			return false;
		
		m_pFace.init(p_filename, p_path, p_gl);
		MorphController.getInstance().processDictionary();
		m_renderManager.resetDrawables();
		m_renderManager.addDrawables(m_pFace.getDrawables());
		m_sequenceDuration = 0;
		return true;
	}
	
	public boolean onLoadFAP(String p_filename)
	{
		if(m_bBusy)
			return false;
		
		Scanner input = null;
		try
		{
			input = new Scanner(new File(p_filename));
			input.useLocale(Locale.US);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}

		m_pFapStream.open(input, m_pFace.getFAPU());
		input.close();
		m_renderMode = RenderMode.FAP; 
		return true;
	}
	
	public boolean onLoadANIM(String p_filename)
	{
		if(m_bBusy)
			return false;
		Scanner input = null;
		try
		{
			input = new Scanner(new File(p_filename));
			input.useLocale(Locale.US);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		m_sequenceDuration = m_pFace.processAnims(input);
		input.close();
		m_renderMode = RenderMode.KEYFRAME;
		return true;
	}
	
	public boolean onLoadPHO(String p_filename, String p_lang)
	{
		if(m_bBusy)
			return false;
		Scanner input = null;
		try
		{
			input = new Scanner(new File(p_filename));
			input.useLocale(Locale.US);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		m_sequenceDuration = m_pFace.processPhonemes(input, p_lang);
		input.close();
		m_renderMode = RenderMode.KEYFRAME;
		return true;
	}
	
	public long synchronize(boolean p_bStart)
	{
		if(m_frame_time == -1)
			m_frame_time = 1000 / m_pFapStream.getFPS();
		if(p_bStart)
		{
			m_pTimer.startTimer();
			m_missed_frames = 0;
			m_frame_count = 0;
			m_frame_time = 1000 / m_pFapStream.getFPS();
		}
		int elapsed_frames = (int) (m_pTimer.getElapsedTime(false) / m_frame_time);
		while(elapsed_frames > m_frame_count)
		{
			m_frame_count++;
			m_missed_frames++;
			onAdvanceFrame();
			System.err.println("missed frames " + m_missed_frames);
		}
		if(m_pTimer.getElapsedTime(false) < m_frame_time)
			m_pTimer.waitFor((long) m_frame_time - m_pTimer.getElapsedTime(false));
		m_frame_count++;
		return m_pTimer.getElapsedTime(true);
	}
}
