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
 * XMath::LineSegment3
 * tamamlandi.
 */
public class LineSegment3
{
	private Vector3 m_start = null;
	private Vector3 m_end = null;
	
	LineSegment3()
	{
		// LineSegment3(void){}
		m_start = new Vector3();
		m_end = new Vector3();
	}
	
	LineSegment3(final Vector3 s, final Vector3 e)
	{
		// LineSegment3(const Vector3& s, const Vector3& e) : m_start(s), m_end(e) {}
		m_start = new Vector3(s);
		m_end = new Vector3(e);
	}
	
	public Vector3 getStart()
	{
		// const Vector3& getStart() const {return m_start;}
		return m_start;
	}
	
	public Vector3 getEnd()
	{
		// const Vector3& getEnd() const {return m_end;}
		return m_end;
	}
	
	public void setStart(final Vector3 s)
	{
		// void setStart(const Vector3& s)	{m_start = s;}
		m_start = new Vector3(s);
	}
	
	public void setEnd(final Vector3 e)
	{
		// void setEnd(const Vector3& e)	{m_end = e;}
		m_end = new Vector3(e);
	}
	
}
