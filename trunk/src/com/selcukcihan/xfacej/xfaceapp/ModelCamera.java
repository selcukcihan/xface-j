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
 * - Koray Balci (koraybalci@gmail.com)
 * ***** END LICENSE BLOCK ***** */

package com.selcukcihan.xfacej.xfaceapp;

/*
 * XFaceApp::ModelCamera
 * bitti.
 */

import javax.media.opengl.GL;

import com.selcukcihan.xfacej.xmath.AxisAngle;
import com.selcukcihan.xfacej.xmath.Matrix4;
import com.selcukcihan.xfacej.xmath.Quaternion;
import com.selcukcihan.xfacej.xmath.Vector3;



public class ModelCamera
{
	public enum kMODE
	{
		IDLE,
		ZOOM,
		ROTATE,
		PAN	
	}
	private kMODE m_mode;
	private long m_wndWidth;
	private long m_wndHeight;
	private Vector3 m_start;
	private long m_panX;
	private long m_panY;

	private float m_rotAngle;
	private Vector3 m_rotAxis;
	private float m_zoom;
	private Matrix4 m_RotMatrix;
	
	//final private GL m_gl;

	public ModelCamera()
	{
		/*
		 * ModelCamera();
		 */
		m_wndHeight = 0;
		m_wndWidth = 0;
		m_zoom = -10;
		m_rotAngle = 0;
		m_mode = kMODE.IDLE;
		m_panX = 0;
		m_panY = 0;
		m_rotAxis = new Vector3(0, 1, 0);
		m_start = new Vector3();
		m_RotMatrix = new Matrix4();
		m_RotMatrix.loadIdentity();
		
		//m_gl = p_gl;
	}
	
	private void rotate(int x, int y)
	{
		/*
		 * private void rotate(int x, int y);
		 */
		// calculate percentages from centre of screen 
	    float horPct = (float)(x - m_wndWidth/2 ) / (float)(m_wndWidth/2);
	    float verPct = (float)(y - m_wndHeight/2 ) / (float)(m_wndHeight/2);

		// small speed up    
		float xDif = x - m_start.x;
		float yDif = y - m_start.y;

		// get percentages for rotations 
	    m_rotAxis.y = (1.0f - Math.abs(verPct))*xDif;
	    m_rotAxis.x = (1.0f - Math.abs(horPct))*yDif;
	    m_rotAxis.z = horPct/2*yDif - verPct/2*xDif;
		m_rotAxis.normalize();

	    // amount is distance between current and initial pos
	    m_rotAngle = (float)Math.sqrt( xDif*xDif + yDif*yDif);
	}
	private void zoom(int z)
	{
		/*
		 * private void zoom(int z);
		 */
		// change zoom and reset reference
		m_zoom += m_start.z - (float)z;
		m_start.z = (float)z;
	}
	private void pan(int x, int y)
	{
		/*
		 * private void pan(int x, int y);
		 */
		m_panX -= (int)m_start.x - x;
		m_panY += (int)m_start.y - y;
		
		m_start.y = (float)y;
		m_start.x = (float)x;
	}
	public void setScreenSize(long w, long h)
	{
		/*
		 * void setScreenSize(long w, long h) {m_wndWidth = w; m_wndHeight = h;}
		 */
		m_wndWidth = w; m_wndHeight = h;
	}
	public void setMode(kMODE mode)
	{
		/*
		 * void setMode(kMODE mode){m_mode = mode;};
		 */
		m_mode = mode;
	}
	public void start(int x, int y)
	{
		/*
		 * void start(int x, int y);
		 */
		/* store the down spot for later */
	    m_start.x = (float)x;
		m_start.y = (float)y;
		m_start.z = (float)y;
	}
	public void end(GL p_gl)
	{
		/*
		 * void end();
		 */
		/* save where we are */
		p_gl.glPushMatrix();
		/* update our stored matrix with a new rotation */
		p_gl.glLoadIdentity();
		if(m_rotAngle != 0)
			p_gl.glRotatef(m_rotAngle, m_rotAxis.x, m_rotAxis.y, m_rotAxis.z);
		p_gl.glMultMatrixf(m_RotMatrix.getMatrix(), 0);
   
		p_gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, m_RotMatrix.getMatrix(), 0);
		p_gl.glPopMatrix();
		
		m_rotAngle = 0;
	}
	public void update(int x, int y)
	{
		/*
		 * void update(int x, int y);
		 */
		switch(m_mode)
		{
		case ROTATE:
			rotate(x, y);
			break;
		case ZOOM:
			zoom(y);
			break;
		case PAN:
			pan(x, y);
		default:
			break;
		}
	}
	public void apply(GL p_gl)
	{
		/*
		 * void apply();
		 */
		p_gl.glLoadIdentity();

		/* move the image back from the camera */
		p_gl.glTranslatef( (float)m_panX, (float)m_panY, m_zoom );

		/* rotate image about its origin */
		if(m_rotAngle != 0)
			p_gl.glRotatef(m_rotAngle, m_rotAxis.x, m_rotAxis.y, m_rotAxis.z);
		p_gl.glMultMatrixf(m_RotMatrix.getMatrix(), 0);
	}
	public void setDistance(float dist)
	{
		/*
		 * void setDistance(float dist){m_zoom = dist;};
		 */
		m_zoom = dist;
	}
	public void setTranslation(final Vector3 trans)
	{
		/*
		 * void setTranslation(const Vector3& trans) {m_zoom = trans.z; m_panX = (long)trans.x; m_panY = (long)trans.y;}
		 */
		m_zoom = trans.z;
		m_panX = (long)trans.x;
		m_panY = (long)trans.y;
	}

	public AxisAngle getAxisAngle(GL p_gl)
	{
		/*
		 * AxisAngle getAxisAngle() const;
		 */
		Matrix4 mat = new Matrix4();
		p_gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, mat.getMatrix(), 0);
		Quaternion quat = new Quaternion();
		quat.FromRotationMatrix(mat);
		return quat.ToAxisAngle();
	}
	public void setAxisAngle(final AxisAngle axisAngle, GL p_gl)
	{
		/*
		 * void setAxisAngle(const AxisAngle& axisAngle);
		 */
		Quaternion quat = new Quaternion(axisAngle);
		m_RotMatrix = quat.ToRotationMatrix();
		apply(p_gl);
	}
	public Vector3 getTranslation()
	{
		/*
		 * Vector3 getTranslation() const {Vector3 trans((float)m_panX, (float)m_panY, m_zoom); return trans;}
		 */
		Vector3 trans = new Vector3((float)m_panX, (float)m_panY, m_zoom);
		return trans;
	}
	
}
