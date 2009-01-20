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
 * XEngine::BinaryModelBatchLoader
 * bitti.
 * sorun olabilecek yerler:
 * 		GL gl gecirdim parametre olarak init'e, bu isi bir yerde baglamam lazim, bu gl nerden gelecek
 * 		adam fread(fp, &vector) falan yapmis) ben readFloat diye okuya okuya memberlere yazdim
 * 			ama sira onemli, belki sira yanlis olmustur falan bilemiyorum ki c++daki davranisi
 * 
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

import javax.media.opengl.GL;

import xmath.Quaternion;
import xmath.Vector3;

public class BinaryModelBatchLoader implements IModelLoader
{
	class DrawablePair
	{
		public String m_s;
		public LinkedList<Drawable> m_d;
		public DrawablePair(final String s, final LinkedList<Drawable> d)
		{
			m_s = s;
			m_d = d;
		}
	}
	private boolean m_bLoaded;
	/*
	 * typedef std::list<boost::shared_ptr<Drawable> > DRAWABLES;
	 * std::list< std::pair< std::string, DRAWABLES> > m_data;
	 */
	private LinkedList<DrawablePair> m_data;

	public BinaryModelBatchLoader()
	{
		m_bLoaded = false;
		m_data = new LinkedList<DrawablePair>();
	}
	
	public boolean isLoaded()
	{
		/*
		 * bool isLoaded() const {return m_bLoaded;}
		 */
		return m_bLoaded;
	}

	public static int readUShortInt(RandomAccessFile p_fp) throws IOException
	{
		int retVal = 0;
		
		int b = p_fp.readUnsignedByte();
		retVal += b * 0x1;
		
		b = p_fp.readUnsignedByte();
		retVal += b * 0x100;
		
		return retVal;
	}
	
	public static int readUInt(RandomAccessFile p_fp) throws IOException
	{
		int retVal = 0;
		
		int b = p_fp.readUnsignedByte();
		retVal += b * 0x1;
		
		b = p_fp.readUnsignedByte();
		retVal += b * 0x100;
		
		b = p_fp.readUnsignedByte();
		retVal += b * 0x10000;
		
		b = p_fp.readUnsignedByte();
		retVal += b * 0x1000000;
		
		return retVal;
	}
	
	public boolean init(final String filename, final String path, final GL p_gl)
	{
		/*
		 * bool init(const std::string& filename, const std::string& path);
		 */
		m_bLoaded = false;
		RandomAccessFile fp = null;
		try
		{
			fp = new RandomAccessFile(path+filename, "r");
		}
		catch(FileNotFoundException fnfe)
		{
			/* fnfe.printStackTrace(); */
		}
		if(fp == null)
			return false;
		
		try
		{
			m_data.clear();
					
			int sz_files = readUInt(fp); /* size_t unsigned int c++da, biz int yaptik bakalim sorun olmaz insallah */
			for(int i = 0; i < sz_files; ++i)
			{
				int sz = readUInt(fp);
				byte [] fname = new byte[sz];
				fp.read(fname);
	
				int sz_drawables = readUInt(fp);
				LinkedList<Drawable> drawables = new LinkedList<Drawable>();
				for(int j = 0; j < sz_drawables; ++j)
				{
					Drawable dr = new Drawable();
					// mesh name
					sz = readUInt(fp);
					byte [] drname = new byte[sz];
					fp.read(drname);
					dr.setMeshName(new String(drname));
	
					// tex name
					sz = readUInt(fp);
					drname = new byte[sz];
					fp.read(drname);
					dr.setTexName(new String(drname), 0);
					TextureManager.getInstance().load(path + new String(drname), new String(drname), p_gl);
	
					// transform (translation and rotation)
					Transform tr = new Transform();
					Vector3 trans = new Vector3();
					Quaternion q = new Quaternion();
					trans.x = Float.intBitsToFloat(readUInt(fp));
					trans.y = Float.intBitsToFloat(readUInt(fp));
					trans.z = Float.intBitsToFloat(readUInt(fp));
					tr.setTranslation(trans);
					q.x = Float.intBitsToFloat(readUInt(fp));
					q.y = Float.intBitsToFloat(readUInt(fp));
					q.z = Float.intBitsToFloat(readUInt(fp));
					q.w = Float.intBitsToFloat(readUInt(fp));
					
					tr.setRotation(q);
					dr.setTransform(tr);
	
					drawables.add(dr);
				}
				String dummy = new String(fname);
				m_data.add(new DrawablePair(dummy, drawables));
			}
	
			// process the data now
			MeshManager pMM = MeshManager.getInstance();
			for(DrawablePair dp : m_data)
			{
				for(Drawable d : dp.m_d)
				{
					DeformableGeometry pGeo = new DeformableGeometry(d.getMeshName());
					pGeo.readBinary(fp);
					pMM.registerMesh(pGeo);
				}
			}
			fp.close();
			m_bLoaded = true;
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			return false;
		}
		return true;
	}
	
	public LinkedList<Drawable> loadModel(final String filename, final String dir, GL p_gl)
	{
		/*
		 * std::list<boost::shared_ptr<Drawable> > loadModel(const std::string &filename, const std::string& dir="./");
		 */
		for(DrawablePair dp : m_data)
		{
			if(dp.m_s.equals(filename))
				return dp.m_d;
		}
		
		LinkedList<Drawable> retVal = new LinkedList<Drawable>();
		return retVal;
	}
}
