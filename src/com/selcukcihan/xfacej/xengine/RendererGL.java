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
 * XEngine::RendererGL
 * extensionsa bakmam lazim
 * sanirim kullanmiycam buffer extensionlarini simdilik
 */

import javax.microedition.khronos.opengles.GL11;
import com.selcukcihan.android.xface.xmath.Vector3;

public class RendererGL implements IRenderer
{
	private static final int MAX_EXTENSIONS = 1024;
	// ANDROID
	//private boolean [] m_supported = new boolean[MAX_EXTENSIONS];
	
	//private final GL m_gl;
	
	private int m_maxBufferID = 0;

	private enum XGL_EXTENSIONS
	{
		XGL_ARB_multitexture,
		XGL_ARB_vertex_buffer_object
	};
	
	private Transform m_globalTransform = null;
	
	public RendererGL()
	{
		// RendererGL(void);
		
		m_maxBufferID = 0;
		m_globalTransform = new Transform();
		//m_gl = gl;
		initGLExtensions();
	}

	private void initGLExtensions()
	{
		// void initGLExtensions();
		// burayla ilgilenmek gerekebilir, acep w.mobilede nasil halletmistim burayi? ici bostu sanirim yine
	}
	
	public void prepareBufferedMesh(IndexedFaceSet mesh)
	{
		// void prepareBufferedMesh(IndexedFaceSet* pMesh);
		
		if(!isExtSupported(XGL_EXTENSIONS.XGL_ARB_vertex_buffer_object))
			return;
	}
	/*
	 * ANDROID
	private void deleteBuffers(GL11 p_gl)
	{
		// void deleteBuffers();
		
		if(!isExtSupported(XGL_EXTENSIONS.XGL_ARB_vertex_buffer_object))
			return;

		for(int id = 1; id<m_maxBufferID; ++id)
		{
			if(p_gl.glIsBufferARB(id))
			{
				int [] buf = { id };
				p_gl.glDeleteBuffersARB(1, buf, 0);
			}
		}
	}
	*/
	private void doTexture(final ITexture tex, GL11 p_gl)
	{
		// void doTexture(const ITexture& tex) const;
		
		if(tex.getTextureType() == ITexture.TEXTURETYPE.TEXTURE2D)
		{
			p_gl.glEnable(GL11.GL_TEXTURE_2D);
			p_gl.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID().get(0));
		}
	}
	
	private void doGeometry(DeformableGeometry mesh, GL11 p_gl)
	{
		// void doGeometry(DeformableGeometry& mesh) const;
		
		p_gl.glVertexPointer(3, GL11.GL_FLOAT, 0, mesh.getDeformedVerticesGL());
		if(mesh.getNormals().size() != 0)
		{
			p_gl.glNormalPointer(GL11.GL_FLOAT, 0, mesh.getNormalsGL());
			p_gl.glEnableClientState(GL11.GL_NORMAL_ARRAY );
		}
		
		if(mesh.getTexCoords().size() != 0)
		{
			p_gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, mesh.getTexCoordsGL());
			p_gl.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		}

		p_gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		// ANDROID
		//p_gl.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndexCount(), GL11.GL_UNSIGNED_INT, mesh.getIndicesGL());
		p_gl.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndexCount(), GL11.GL_UNSIGNED_SHORT, mesh.getIndicesGL());
		//glDrawArrays( GL_TRIANGLES, 0, mesh.getIndexCount()/3);
		
		p_gl.glDisableClientState( GL11.GL_VERTEX_ARRAY );
		if(mesh.getNormals().size() != 0)
			p_gl.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		
		if(mesh.getTexCoords().size() != 0)
			p_gl.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

	}

	private void doTransform(final Transform trans, GL11 p_gl)
	{
		// void doTransform(const Transform& trans) const;
		
		p_gl.glMultMatrixf(trans.getWorldTransform());
	}

	public void render(Drawable pDrawable, GL11 p_gl)
	{
		// void render(boost::shared_ptr<Drawable> pDrawable) const;
		
		String MeshName = pDrawable.getMeshName();

		DeformableGeometry pMesh = MeshManager.getInstance().getMesh(MeshName);
		if(pMesh == null)
			return;
		p_gl.glPushMatrix();
		p_gl.glMultMatrixf(m_globalTransform.getLocalTransform());

		if(pDrawable.needUpdate())
		{
			if(pDrawable.getBinding().equals("LeftEye"))
			{
				Vector3 eye = pDrawable.getPivot();
				p_gl.glTranslatef(-eye.x, -eye.y, -eye.z);
				p_gl.glRotatef(pDrawable.getAPs()[(24)], 0.0f, 1.0f, 0.0f);
				p_gl.glRotatef(pDrawable.getAPs()[(22)], 1.0f, 0.0f, 0.0f);
				p_gl.glTranslatef(eye.x, eye.y, eye.z);
			}
			
			if(pDrawable.getBinding().equals("RightEye"))
			{
				Vector3 eye = pDrawable.getPivot();
				p_gl.glTranslatef(-eye.x, -eye.y, -eye.z);
				p_gl.glRotatef (pDrawable.getAPs()[(25)], 0.0f, 1.0f, 0.0f);
				p_gl.glRotatef (pDrawable.getAPs()[(23)], 1.0f, 0.0f, 0.0f);
				p_gl.glTranslatef (eye.x, eye.y, eye.z);
			}

			pDrawable.updateAnimation();	
	   }

		doTransform(pDrawable.getTransform(), p_gl);
		
		if(pDrawable.isTextureOn())
		{
			final ITexture pTexture = TextureManager.getInstance().getTexture(pDrawable.getTexName(0));
			if(pTexture != null)
				doTexture(pTexture, p_gl);
		}
		
		doGeometry(pMesh, p_gl);

		p_gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		p_gl.glPopMatrix();
	}

	public void setTransform(final Transform tr)
	{
		// void setTransform(const Transform& tr) {m_globalTransform = tr;}	
		
		m_globalTransform = new Transform(tr);
	}
	
	private boolean isExtSupported(XGL_EXTENSIONS ext)
	{
		// bool isExtSupported(XGL_EXTENSIONS ext) const {return m_supported[ext];};
		return false;
		// ANDROID
		//return m_supported[ext.ordinal()];
	}

}
