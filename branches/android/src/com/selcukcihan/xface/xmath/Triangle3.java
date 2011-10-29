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

package com.selcukcihan.android.xface.xmath;

/*
 * XMath::Triangle3
 * tamamlandi.
 */

public class Triangle3
{

	private Vector3 m_origin = null;
	private Vector3 m_edge0 = null;
	private Vector3 m_edge1 = null;
	
	Triangle3()
	{
		// Triangle3(void){}
		m_origin = new Vector3();
		m_edge0 = new Vector3();
		m_edge1 = new Vector3();
	}
	
	Triangle3(final Vector3 p0, final Vector3 p1, final Vector3 p2)
	{
		m_origin = new Vector3(p0);
		m_edge0 = new Vector3(p1.opSubtract(p0));
		m_edge1 = new Vector3(p2.opSubtract(p0));
	}
	
	public Vector3 getOrigin()
	{
		// const Vector3& getOrigin() const {return m_origin;}
		return m_origin;
	}
	
	public Vector3 getEdge0()
	{
		// const Vector3& getEdge0() const {return m_edge0;}
		return m_edge0;
	}
	
	public Vector3 getEdge1()
	{
		// const Vector3& getEdge1() const {return m_edge1;}
		return m_edge1;
	}
	
	public void setOrigin(final Vector3 org)
	{
		// void setOrigin(const Vector3& org)	{m_origin = org;}
		m_origin = new Vector3(org);
	}
	
	public void setEdge0(final Vector3 ed0)
	{
		// void setEdge0(const Vector3& ed0)	{m_edge0 = ed0;}
		m_edge0 = new Vector3(ed0);
	}
	
	public void setEdge1(final Vector3 ed1)
	{
		// void setEdge1(const Vector3& ed1)	{m_edge1 = ed1;}
		m_edge1 = new Vector3(ed1);
	}
	
}
