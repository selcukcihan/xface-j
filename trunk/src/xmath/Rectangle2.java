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

package xmath;

/*
 * XMath::Rectangle2
 * tamamlandi.
 */

import xengine.Vertex2D;

public class Rectangle2
{
	private Vertex2D m_topleft = null;
	private Vertex2D m_bottomright = null;
	
	Rectangle2()
	{
		m_topleft = new Vertex2D();
		m_bottomright = new Vertex2D();
	}
	
	Rectangle2(float _x0, float _y0, float _x1, float _y1)
	{
		// Rectangle2(float _x0 = 0, float _y0 = 0, float _x1 = 0, float _y1 = 0)
		
		m_topleft = new Vertex2D();
		m_bottomright = new Vertex2D();
		m_topleft.x = _x0;
		m_topleft.y = _y0;
		m_bottomright.x = _x0;
		m_bottomright.y = _y0;
	}
	
	public Vertex2D getTopLeft()
	{
		// const Vertex2D& getTopLeft() const {return m_topleft;};
		
		return m_topleft;
	}
	
	public Vertex2D getBottomRight()
	{
		// const Vertex2D& getBottomRight() const {return m_bottomright;};
		
		return m_bottomright;
	}
	
	public void setX0(float _x0)
	{
		// void setX0(float _x0) {m_topleft.x = _x0;};
		
		m_topleft.x = _x0;
	}
	
	public void setY0(float _y0)
	{
		// void setY0(float _y0) {m_topleft.y = _y0;};
		
		m_topleft.y = _y0;
	}
	
	public void setX1(float _x1)
	{
		// void setX1(float _x1) {m_bottomright.x = _x1;};
		
		m_bottomright.x = _x1;
	}
	
	public void setY1(float _y1)
	{
		// void setY1(float _y1) {m_bottomright.y = _y1;};
		
		m_bottomright.y = _y1;
	}
	
	public float getX0()
	{
		// float getX0() const {return m_topleft.x;};
		
		return m_topleft.x;
	}
	
	public float getY0()
	{
		// float getY0() const {return m_topleft.y;};
		
		return m_topleft.y;
	}
	
	public float getX1()
	{
		// float getX1() const {return m_bottomright.x;};
		
		return m_bottomright.x;
	}
	
	public float getY1()
	{
		// float getY1() const {return m_bottomright.y;};
		
		return m_bottomright.y;
	}
	
	public float getWidth()
	{
		// float getWidth() const {return m_bottomright.x - m_topleft.x;};
		
		return m_bottomright.x - m_topleft.x;
	}
	
	public float getHeight()
	{
		// float getHeight() const {return m_bottomright.y - m_topleft.y;};
		
		return m_bottomright.y - m_topleft.y;
	}
	
	public boolean isPointInRect(float x, float y)
	{
		// bool isPointInRect(float x, float y) const
		
		return (x > m_topleft.x && x < m_bottomright.x && y < m_topleft.y && y > m_bottomright.y);
	}
}
