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

package com.selcukcihan.android.xface.xface;

/*
 * XFace::IFapStream
 * bitti.
 */

import java.util.Scanner;
import java.util.Vector;

public abstract class IFapStream
{
	float [] m_currentFAP;
	VisemeStruct m_currentViseme;
	ExpressionStruct m_currentExpression;
	
	short m_FPS = 0;
	float m_version = 0;
	
	public IFapStream()
	{
		// IFapStream(void) : m_FPS(0){};
		
		m_FPS = 0;
		m_currentFAP = new float [0];
		m_currentViseme = null;
		m_currentExpression = null;
	}
	
	public abstract boolean isEnd();
	public abstract boolean isOpen();
	public abstract boolean open(Scanner input, final FAPU fapu);
	public abstract void next();
	public abstract void rewind();
	public abstract int getFAPCount();
	public abstract long getCurrentFAPId();
	
	public float getVersion()
	{
		// float getVersion() const {return m_version;}
		
		return m_version;
	}
	
	public short getFPS()
	{
		// short getFPS() const {return m_FPS;};
		
		return m_FPS;
	}
	
	public float [] getCurrentFAP()
	{
		// const std::vector<float>& getCurrentFAP() const {return m_currentFAP;};
		
		return m_currentFAP;
	}
	
	public VisemeStruct getCurrentViseme()
	{
		// const VisemeStruct& getCurrentViseme() const {return m_currentViseme;}
		
		return m_currentViseme;
	}
	
	public ExpressionStruct getCurrentExpression()
	{
		// const ExpressionStruct& getCurrentExpression() const {return m_currentExpression;}
		
		return m_currentExpression;
	}
}
