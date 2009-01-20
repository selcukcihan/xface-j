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

package xengine;

/*
 * XEngine::VRML97Loader
 * bitti.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;

import javax.media.opengl.GL;

import xmath.AxisAngle;
import xmath.Quaternion;
import xmath.Vector3;

public class VRML97Loader implements IModelLoader
{
	//private GL m_gl;
	
	public VRML97Loader()
	{
		//m_gl = _gl;
	}
	
	private IndexedFaceSet onIndexedFaceSet(Scanner p_scanner, final String label) throws IOException
	{
		/*
		 * IndexedFaceSet* onIndexedFaceSet(std::istream& pScanner, const std::string& label) const;
		 */
		String dummy = new String("");
		dummy = p_scanner.next();
		short nBlock = 0;
		if(dummy.equals("{"))
			nBlock++;
		else 
			return null; // sth wrong!

		Vector<Vector3> vertices = new Vector<Vector3>();
		Vector<Vertex2D> texCoords = new Vector<Vertex2D>();
		Vector<Vector3> normals = new Vector<Vector3>();
		Vector<Vector<Integer>> indices = new Vector<Vector<Integer>>();
		Vector<Vector<Integer>> indicesTex = new Vector<Vector<Integer>>();

		while (nBlock > 0)
		{
			dummy = p_scanner.next();
			if(dummy.equals("{"))
				nBlock++;
			else if (dummy.equals("}"))
				nBlock--;
			else if(dummy.equals("coord"))
				onCoord(p_scanner, vertices);
			else if(dummy.equals("texCoord"))
				onTexCoord(p_scanner, texCoords);
			else if(dummy.equals("coordIndex"))
			{
				onIndex(p_scanner, indices);
//				std::cout << "coordIndex: " << indices.size() << std::endl;	
			}
			else if(dummy.equals("texCoordIndex"))
			{
				onIndex(p_scanner, indicesTex);
//				std::cout << "texCoordIndex: " << indicesTex.size() << std::endl;	
			}
		}

		DeformableGeometry pMesh = new DeformableGeometry(label);

		Vector<Vertex2D> d_TexCoords = VectorWrapper.WrapVertex2D(texCoords);
		
		// making texture coords indices exactly the same as vertex indices,
		// unfortunately, in VRML97 format, this isn't a rule, so we enforce it here
		if(!texCoords.isEmpty())
		{
			Iterator<Vector<Integer>> it, it_t;
			Iterator<Integer> it2, it2_t;
			it_t = indicesTex.iterator();
			it = indices.iterator();
			while(it.hasNext())
			{
				it2_t = it_t.next().iterator();
				it2 = it.next().iterator();
				while(it2.hasNext())
					texCoords.set(it2.next(), new Vertex2D(d_TexCoords.elementAt(it2_t.next())));
			}
		}

		// Indices are attached as IndexedFaceSet objects as children to the mesh node
		pMesh.setVertices(vertices);
		pMesh.setIndices2(indices);
		if(!normals.isEmpty()) // no normals, always empty
			pMesh.setNormals(normals);
		else
			pMesh.computeVertexNormals();
		if(!texCoords.isEmpty())
			pMesh.setTexCoords(texCoords);
		
		MeshManager.getInstance().registerMesh(pMesh);
		return pMesh;
	}
	
	private void onCoord(Scanner p_scanner, Vector<Vector3> vertices) throws IOException
	{
		/*
		 * void onCoord(std::istream& pScanner, std::vector<Vector3>& vertices) const;
		 */
		String dummy = new String("");
		while(!dummy.equals("point"))
			dummy = p_scanner.next();

		while(!dummy.contains("["))
			dummy = p_scanner.next();

		boolean loopEnds = false; /* will be true when the character "]" is read */
		while(!loopEnds)
		{
			Vector3 coord3 = new Vector3();
			
			coord3.x = p_scanner.nextFloat();
			coord3.y = p_scanner.nextFloat();
			dummy = p_scanner.next(); /* should be a float ending with "," */
			if(dummy.contains(","))
				dummy = dummy.substring(0, dummy.length() - 1);
			else
				loopEnds = true;
			coord3.z = Float.parseFloat(dummy);
			vertices.add(coord3);
		}
		p_scanner.next(); /* this is "]" */
		while(!dummy.equals("}"))
			dummy = p_scanner.next(); // should be (})
//		std::cout << "num coord: " << vertices.size() << std::endl;
	}
	
	private void onTexCoord(Scanner p_scanner, Vector<Vertex2D> texCoords) throws IOException
	{
		/*
		 * void onTexCoord(std::istream& pStream, std::vector<Vertex2D>& texCoords) const;
		 */
		String dummy = new String("");
		while(!dummy.equals("point"))
			dummy = p_scanner.next();

		while(!dummy.equals("["))
			dummy = p_scanner.next();

		boolean loopEnds = false; /* will be true when the character "]" is read */
		while(!loopEnds)
		{
			Vertex2D coord2 = new Vertex2D();
			
			coord2.x = p_scanner.nextFloat();
			dummy = p_scanner.next(); /* should be a float ending with "," */
			if(dummy.contains(","))
				dummy = dummy.substring(0, dummy.length() - 1);
			else
				loopEnds = true;
			
			coord2.y = Float.parseFloat(dummy);
			texCoords.add(coord2);
		}
		p_scanner.next(); /* this is "]" */
		
		while(!dummy.contains("}"))
			dummy = p_scanner.next(); // should be (})
//		std::cout << "num texcoord: " << texCoords.size() << std::endl;
	}
	
	private void onIndex(Scanner p_scanner, Vector<Vector<Integer>> store) throws IOException
	{
		/*
		 * void onIndex(std::istream& pStream, std::vector< std::vector<unsigned short> >& store) const;
		 */
		String dummy = new String("");
		while(!dummy.equals("["))
			dummy = p_scanner.next();

		long ind = 0;
		
		boolean loopEnds = false; /* will be true when the character "]" is read */
		while(!loopEnds)
		{
			Vector<Integer> polyInd = new Vector<Integer>();
			while(true)
			{
				dummy = p_scanner.next();
				if(dummy.contains(","))
					dummy = dummy.substring(0, dummy.length() - 1); /* virgulu at */
				else /* virgul yoksa disaridaki loop bitecek demektir */
					loopEnds = true;
				
				ind = Long.parseLong(dummy);
				if(ind == -1) /* -1 gormek, icerideki loopun bitmesi demektir */
					break;
				polyInd.add(new Integer((int)ind));
			}
			store.add(polyInd);
		}
		p_scanner.next(); /* this is "]" */
	}

	public LinkedList<Drawable> loadModel(final String filename, final String dir, GL p_gl)
	{
		/*
		 * std::list<boost::shared_ptr<Drawable> > loadModel(const std::string &filename, const std::string& dir ="./" );
		 */
		Drawable pDrawable = new Drawable();
		Transform pTrans = null;
		LinkedList<Drawable> drawables = new LinkedList<Drawable>();
		LinkedList<String> meshNames = new LinkedList<String>();
		String modelfile = dir + filename;
		Scanner fp = null;
		try
		{
			fp = new Scanner(new BufferedReader(new FileReader(modelfile)));
			fp.useLocale(Locale.US);
		}
		catch(FileNotFoundException fnfe)
		{
			;
		}
		if(fp == null)
			return drawables;
		
		try
		{
		
			String linestring = new String();
			String key_str = new String();
			String meshname = new String();
			String texname = new String();
			int cnt = 0;
			while(fp.hasNext())
			{
				key_str = fp.next();
				if(key_str.equals("Transform"))
				{
					// delete pTrans;
					pTrans = new Transform();
					while(!key_str.equals("children"))
					{
						key_str = fp.next();
						if(key_str.equals("translation"))
						{
							Vector3 tr = new Vector3(0, 0, 0);
							tr.x = fp.nextFloat();
							tr.y = fp.nextFloat();
							tr.z = fp.nextFloat();
							pTrans.setTranslation(tr);
						}
						else if(key_str.equals("rotation"))
						{
							Vector3 rot = new Vector3(0, 1, 0);
							float ang = 0;
							rot.x = fp.nextFloat();
							rot.y = fp.nextFloat();
							rot.z = fp.nextFloat();
							ang = fp.nextFloat();
							ang = 180.0f * ang / 3.1415926535f;
							pTrans.setRotation(new Quaternion(new AxisAngle(rot, ang)));
						}
						else if(key_str.equals("scale"))
						{
							Vector3 sc = new Vector3(1, 1, 1);
							sc.x = fp.nextFloat();
							sc.y = fp.nextFloat();
							sc.z = fp.nextFloat();
							pTrans.setScale(sc);
						}
					}
				}
				else if(key_str.equals("Shape"))
				{
					pDrawable = new Drawable();
					drawables.add(pDrawable);
					if(pTrans != null)
						pDrawable.setTransform(pTrans);
				}
				else if(key_str.equals("texture"))
				{
					key_str = fp.next();
					if(key_str.equals("ImageTexture"))
					{
						while(!key_str.equals("url"))
							key_str = fp.next();
						texname = fp.next();
	
						// the filename is stored in double quotes, let's get rid of them
						texname = texname.substring(1);
						texname = texname.substring(0, texname.length()-1);
						TextureManager.getInstance().load(dir + texname, texname, p_gl);
						pDrawable.setTexName(texname, 0);
	//					std::cout << texname << "\n";
					}
				}
				else if(key_str.equals("geometry"))
				{
					// get/create the name for the mesh
					String label = new String();
					String dummy = new String();
					dummy = fp.next();
					if(dummy.equals("DEF"))
					{
						label = fp.next();
						dummy = fp.next();
					}
					else if(dummy.equals("IndexedFaceSet"))
					{
						label = filename+"_"+cnt;
						cnt++;
					}
						
					meshNames.add(label);
					
					if(dummy.equals("IndexedFaceSet")) // dummy should have IndexedFaceSet no matter what
					{
						// process the indexed face set
						if(onIndexedFaceSet(fp, label) != null)
							pDrawable.setMeshName(label);
						else
							break;
					}
				}
			}
			
			fp.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return drawables;
	}

}
