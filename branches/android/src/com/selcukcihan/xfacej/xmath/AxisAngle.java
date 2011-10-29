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

package com.selcukcihan.xfacej.xmath;

/*
 * XMath::AxisAngle
 * tamamlandi.
 */

public final class AxisAngle
{
	private Vector3 m_axis = null;
	private float m_angle = 0; //!< angle in radians

	public AxisAngle()
	{
		// AxisAngle(void): m_axis(0, 0, 1), m_angle(0) {}
		m_axis = new Vector3(0,0,1);
		m_angle = 0;
	}
	
	public AxisAngle(final Vector3 axis, float angle)
	{
		// AxisAngle(const Vector3& axis, float angle) :m_axis(axis), m_angle(angle){}
		m_axis = new Vector3(axis);
		m_angle = angle;
	}
	
	public AxisAngle(AxisAngle rhs)
	{
		m_angle = rhs.m_angle;
		m_axis = new Vector3(rhs.m_axis);
	}

	public void setAngle(float ang)
	{
		// void setAngle(float ang) {m_angle = ang;}
		m_angle = ang;
	}
	
	public float getAngle()
	{
		// float getAngle() const {return m_angle;}
		return m_angle;
	}

	public void setAxis(final Vector3 axis)
	{
		// void setAxis(const Vector3& axis) {m_axis = axis;}
		m_axis = new Vector3(axis);
	}
	
	public void setAxis(float x, float y, float z)
	{
		// void setAxis(float x, float y, float z) {m_axis = Vector3(x, y, z);}
		m_axis = new Vector3(x, y, z);
	}
	
	public Vector3 getAxis()
	{
		// Vector3 getAxis() const {return m_axis;}
		return new Vector3(m_axis);
	}
	
}
