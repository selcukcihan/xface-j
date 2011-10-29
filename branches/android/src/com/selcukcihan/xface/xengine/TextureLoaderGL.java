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
 * XEngine::TextureLoaderGL
 * bitti.
 */

import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL11;
//import javax.media.opengl.glu.GLU;
import android.opengl.GLUtils;

public class TextureLoaderGL implements ITextureLoader
{
	private LinkedList<Integer> m_TextureList;
	
	//private final GL m_gl;
	
	public TextureLoaderGL()
	{
		//m_gl = gl;
		m_TextureList = new LinkedList<Integer>();
	}

	public void unLoad(final ITexture pTexture, GL11 p_gl)
	{
		// void unLoad(const ITexture* pTexture);
		
		if(pTexture == null)
			return;

		m_TextureList.remove(pTexture.getTextureID().get(0));
		p_gl.glDeleteTextures(1, pTexture.getTextureID());
	}

	public boolean load(final String filename, ITexture pTexture, GL11 p_gl)
	{
		// bool load(const std::string& filename, ITexture* pTexture);
		
		if(pTexture == null)
			return false;
		// make a format check and use base class pointer for loader
		ITextureFile loader = null;
		
		if(filename.endsWith(".bmp"))
			loader = new BMPFile();
		else
			return false;
		
		if(!loader.load(filename))
			return false;

		// Generate a texture with the associative texture ID stored in the array
		p_gl.glGenTextures(1, pTexture.m_TexID);

		// This sets the alignment requirements for the start of each pixel row in memory.
		p_gl.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		// Bind the texture to the texture arrays index and init the texture
		p_gl.glBindTexture(GL11.GL_TEXTURE_2D, pTexture.getTextureID().get(0));

		p_gl.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE); 
		// Lastly, we need to tell OpenGL the quality of our texture map.  GL_LINEAR is the smoothest.
		// GL_NEAREST is faster than GL_LINEAR, but looks blochy and pixelated.  Good for slower computers though.
		// Read more about the MIN and MAG filters at the bottom of main.cpp	
		p_gl.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_LINEAR);
		p_gl.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR_MIPMAP_NEAREST);

		/*ANDROID
		final GLU glu = new GLU();
		// Build Mipmaps (builds different versions of the picture for distances - looks better)
		//glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, 3, loader.getWidth(), loader.getHeight(), GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, loader.getData());
        glu.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGB8, loader.getWidth(), loader.getHeight(), GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, loader.getData());
        */
		p_gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
		GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, null, 0); 


		m_TextureList.add(pTexture.getTextureID().get(0));

		return true;
	}
}
