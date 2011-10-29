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
 * XEngine::OBJLoader
 * bitmesine gerek yok, biz wrl kullaniyoruz
 */


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import javax.media.opengl.GL;

import com.selcukcihan.xfacej.xmath.Vector3;

public class OBJLoader implements IModelLoader
{
	private Queue<String> m_TexFiles = new LinkedList<String>();
	private LinkedList<String> m_meshNames;
	private String m_objectName;
	//unsigned int m_IndexOffset;

	private DeformableGeometry m_pMesh;

	private String m_path;
	private Vector<Vector3> m_Vertices;
	private Vector<Vector3> m_Normals;
	private Vector<Vertex2D> m_TexCoords;
	private Vector<Integer> m_Face;
	private Vector<Integer> m_FaceTex;
	private Vector<Integer> m_FaceNormal;
	private Vector<Vector<Integer>> m_Indices;
	private Vector<Vector<Integer>> m_IndicesTex;
	private Vector<Vector<Integer>> m_IndicesNormal;

	public OBJLoader()
	{
		/*
		 * OBJLoader(void);
		 */
	}
	
	private void decodeFace(final String s)
	{
		/*
		 * void decodeFace( const std::string& s );
		 */
	}
	
	private void onMaterial(RandomAccessFile pStream, final String path)
	{
		/*
		 * void onMaterial(std::istream& pStream, const std::string& path);
		 */
	}
	
	private void onGroup(RandomAccessFile pStream)
	{
		/*
		 * void onGroup(std::istream& pStream);
		 */
	}
	
	private void onFace(RandomAccessFile pStream)
	{
		/*
		 * void onFace(std::istream& pStream);
		 */
	}
	
	private void onVertex(RandomAccessFile pStream)
	{
		/*
		 * void onVertex(std::istream& pStream);
		 */
	}
	
	private void onTexCoord(RandomAccessFile pStream)
	{
		/*
		 * void onTexCoord(std::istream& pStream);
		 */
	}
	
	private void onNormal(RandomAccessFile pStream)
	{
		/*
		 * void onNormal(std::istream& pStream);
		 */
	}

	private void createMeshNode()
	{
		/*
		 * void createMeshNode(void);
		 */
		// if we don't have a valid m_pMesh, it means that we don't have a group node associated with this mesh, so it is base mesh.
		if(m_pMesh == null)
			m_pMesh = new DeformableGeometry(m_objectName);
		Vector<Vertex2D> d_TexCoords = VectorWrapper.WrapVertex2D(m_TexCoords);

		// making texture coords indices exactly the same as vertex indices,
		// unfortunately, in OBJ format, this isn't a rule, so we enforce it here
		if(!m_TexCoords.isEmpty())
		{
			Iterator<Vector<Integer>> it, it_t;
			Iterator<Integer> it2, it2_t;
			it_t = m_IndicesTex.iterator();
			it = m_Indices.iterator();
			while(it.hasNext())
			{
				it2_t = it_t.next().iterator();
				it2 = it.next().iterator();
				while(it2.hasNext())
					m_TexCoords.set(it2.next(), new Vertex2D(d_TexCoords.elementAt(it2_t.next())));
			}
		}

		// Indices are attached as IndexedFaceSet objects as children to the mesh node
		m_pMesh.setVertices(m_Vertices);
		m_pMesh.setIndices2(m_Indices);
		if(!m_Normals.isEmpty())
			m_pMesh.setNormals(m_Normals);
		else
			m_pMesh.computeVertexNormals();
		if(!m_TexCoords.isEmpty())
			m_pMesh.setTexCoords(m_TexCoords);
		
		MeshManager.getInstance().registerMesh(m_pMesh);
		m_meshNames.add(m_pMesh.getName());

		// create the corresponding texture
		if(!m_TexFiles.isEmpty())
		{
			/* TextureManager.getInstance().load((m_path + m_TexFiles.poll()), m_pMesh.getName()); */
			/* m_TexFiles.pop(); */
		}

	/*
		m_Vertices.clear();
		m_Normals.clear();
		m_TexCoords.clear();
		m_IndexOffset += m_Indices.size();
		m_Indices.clear();
	*/
	}
	
	public LinkedList<Drawable> loadModel(final String filename, final String dir, GL p_gl)
	{
		/*
		 * std::list<boost::shared_ptr<Drawable> > loadModel(const std::string &filename, const std::string& dir="./");
		 */
		m_path = dir;
		LinkedList<Drawable> drawables = new LinkedList<Drawable>();
		m_meshNames.clear();
		String modelfile = dir + filename;
		RandomAccessFile fp = null;
		try
		{
			fp = new RandomAccessFile(modelfile, "r");
		}
		catch(FileNotFoundException fnfe)
		{
			return drawables;
		}

		String linestring, key_str = null;
		/* while((key_str = getStringLikeCPP(fp)).equals(anObject)) */
		{
			if(key_str.equals("v"))
				onVertex(fp);
			else if(key_str.equals("vt"))
				onTexCoord(fp);
			else if(key_str.equals("vn"))
				onNormal(fp);
			else if(key_str.equals("f"))
				onFace(fp);
			else if(key_str.equals("g"))
				onGroup(fp);
			else if(key_str.equals("o")) // object name
				; /*m_objectName = getStringLikeCPP(fp); */
			else if(key_str.equals("mtllib"))
				onMaterial(fp, dir);
			else // skip comments & not used parts of OBJ structure
				; /*fp.readLine(); */
		}
		createMeshNode();

		Drawable  pDrawable = null;
		for(String str : m_meshNames)
		{
			pDrawable = new Drawable();
			pDrawable.setMeshName(str);
			pDrawable.setTexName(str, 0);
			drawables.push(pDrawable);			
		}
		return drawables;
	}

	private static String getStringLikeCPP(RandomAccessFile fp) throws IOException
	{
		String retVal = new String();
		byte [] b = new byte[1];
		b[0] = fp.readByte();
		while((b[0]!=' ') && (b[0]!='\n') && (b[0]!='\r') && (b[0]!='\t') && (b[0]!='\0'))
		{
			retVal = retVal.concat(new String(b));
			b[0] = fp.readByte();
		}
		return retVal;
	}

}
