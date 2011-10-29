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

package com.selcukcihan.android.xface.xengine;

/*
 * XEngine::ITexture
 * bitti.
 */

import java.nio.IntBuffer;


public abstract class ITexture extends NamedObj
{

	public IntBuffer m_TexID = null; // unsigned int m_TexID;
	
	public enum TEXTURETYPE { TEXTURE2D }
	
/*	ITexture()
	{
		super();
		m_TexID = IntBuffer.allocate(1);
		m_TexID.put(0, 0);
	}*/
	ITexture(final String name)
	{
		// ITexture(const std::string& name) : m_TexID(0), NamedObj(name) {};
		super(name);
		m_TexID = IntBuffer.allocate(1);
		m_TexID.put(0, 0);
	}
	
	public abstract TEXTURETYPE getTextureType();
	
	public IntBuffer getTextureID()
	{
		// const unsigned int getTextureID() const {return m_TexID;};
		m_TexID.rewind();
		return m_TexID;
	}
	
}
