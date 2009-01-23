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

package com.selcukcihan.xfacej.xengine;

/*
 * XEngine::ITextureFile
 * bitti.
 */

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract class ITextureFile
{
	protected int m_Width = 0; // malesef java protected ile c++ protected farkli anlamlarda
	protected int m_Height = 0;
	protected int m_nBits = 0;
	protected ByteBuffer m_pData = null; // a r g b degeri tutuluyor bu sirayla 4*8 = 32 bit intte
	
	ITextureFile()
	{
		// ITextureFile(void) : m_pData(0), m_Width(0), m_Height(0), m_nBits(0) {};
		
		m_Width = 0;
		m_Height = 0;
		m_nBits = 0;
		m_pData = null;
	}
	public ByteBuffer getData()
	{
		// unsigned char* getData() {return m_pData;};
		m_pData.rewind();
		return m_pData;
	}
	
	public int getNBits()
	{
		// int getNBits() const {return m_nBits;};
		
		return m_nBits;
	}
	
	public int getWidth()
	{
		// int getWidth() const {return m_Width;}
		
		return m_Width;
	}
	
	public int getHeight()
	{
		// int getHeight() const {return m_Height;}
		
		return m_Height;
	}
	
	public abstract boolean load(final String filename);
	
}
