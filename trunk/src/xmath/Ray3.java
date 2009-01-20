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
 * XMath::Ray3
 * tamamlandi.
 */

public class Ray3
{
	private Vector3 m_origin = null;
	private Vector3 m_direction = null;
	
	Ray3()
	{
		// Ray3(void){};
		m_origin = new Vector3();
		m_direction = new Vector3();
	}
	
	Ray3(final Vector3 org, final Vector3 dir)
	{
		// Ray3(const Vector3& org, const Vector3& dir) : m_origin(org), m_direction(dir){};
		m_origin = new Vector3(org);
		m_direction = new Vector3(dir);
	}
	
	public void setOrigin(final Vector3 org)
	{
		// void setOrigin(const Vector3& org){m_origin = org;};
		m_origin = new Vector3(org);
	}
	
	public void setDirection(final Vector3 dir)
	{
		// void setDirection(const Vector3& dir){m_direction = dir;};
		m_direction = new Vector3(dir);
	}
	
	public Vector3 getOrigin()
	{
		// const Vector3& getOrigin() const {return m_origin;};
		return m_origin;
	}
	
	public Vector3 getDirection()
	{
		// const Vector3& getDirection() const {return m_direction;};
		return m_direction;
	}

}
