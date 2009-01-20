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
 * XEngine::IndexedFaceSet
 * bitti.
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.util.Enumeration;
import java.util.Vector;

import xmath.Vector3;

public class IndexedFaceSet extends Geometry
{
	IntegerBuffer m_indices; // std::vector<unsigned short> m_indices;
	
	IndexedFaceSet(final String name)
	{
		// IndexedFaceSet(const std::string& name) : Geometry(name){};
		
		super(name);
		
		m_indices = null;
	}
	
	public void readBinary(RandomAccessFile fp) throws IOException
	{
		// void readBinary(FILE* fp);
		
		super.readBinary(fp);
		
		int sz = BinaryModelBatchLoader.readUInt(fp); /* read c style uint */
		m_indices = new IntegerBuffer(sz);
		/* fp.read(m_indices.byteArray()); */
		for(int i = 0; i < sz; i++)
		{
			m_indices.put(BinaryModelBatchLoader.readUShortInt(fp));
		}
		m_indices.rewind();		
	}
	
	public void writeBinary(RandomAccessFile fp) throws IOException
	{
		// void writeBinary(FILE* fp);
		
		super.writeBinary(fp);
		
		fp.writeInt(m_indices.size());
		fp.write(m_indices.byteArray());
		m_indices.rewind();
	}
	
	public void computeVertexNormals()
	{
		// void computeVertexNormals();
		
		Vector<Vector3> normals = new Vector<Vector3>(m_vertices.size());
		for(int i = 0; i < m_vertices.size(); i++)
			normals.add(new Vector3());
		// Compute face normal for each face and sum up for the corresponding vertices
		Vector3 facenormal;
		int sz = m_indices.size();
		if(sz == 0)
			return;
		for(int i = 0, nSides = 3; i < sz/* - nSides*/; i+= nSides) /* We are working ONLY with triangles */
		{
			facenormal = computeFaceNormal(m_vertices.get(m_indices.get(i)), 
											m_vertices.get(m_indices.get(i + 1)), 
											m_vertices.get(m_indices.get(i + 2)));
			normals.set(m_indices.get(i), (normals.get(m_indices.get(i))).opAdd(facenormal));
			normals.set(m_indices.get(i+1), (normals.get(m_indices.get(i+1))).opAdd(facenormal));
			normals.set(m_indices.get(i+2), (normals.get(m_indices.get(i+2))).opAdd(facenormal));
		}
		// Normalize the normals
		for(Vector3 n : normals)
			n.normalize();

		setNormals(normals);
	}
	
	public IndexedFaceSet copyFrom(final IndexedFaceSet rhs)
	{
		// IndexedFaceSet& copyFrom(const IndexedFaceSet& rhs);
		
		// self assignment control
		if (this == rhs) 
			return this;

		super.copyFrom(rhs);
		// copy data
		m_indices = new IntegerBuffer(rhs.m_indices);
		
		return this;
	}
	
	public int getIndexCount()
	{
		// size_t getIndexCount() const {return m_indices.size();}
		
		return m_indices.size();
	}
	
	public IntegerBuffer getIndices()
	{
		// const unsigned short* getIndices() const {return &m_indices[0];}//{return m_indices.getRawPointer();}
		m_indices.rewind();
		return m_indices;
	}

	public IntBuffer getIndicesGL()
	{
		// const unsigned short* getIndices() const {return &m_indices[0];}//{return m_indices.getRawPointer();}
		m_indices.rewind();
		return m_indices.intBuffer();
	}
	
	public void setIndices2(final Vector<Vector<Integer>> indices)
	{
		// void setIndices(const std::vector< std::vector<unsigned short> > &indices);
		
		/*
		 * setIndices iki fonksiyon var c++da overloaded, fakat javada signatureleri ayni oldugundan oyle yapamadim
		 * ben de iki boyutlu vector parametresi kabul edene setIndices2 dedim, oburune de setIndices1 dedim
		 * nerede hangisini cagiracagina dikkat et!!!
		 */ 
		Vector<Integer> tri_indices = new Vector<Integer>();
		
		for(Enumeration<Vector<Integer>> e = indices.elements(); e.hasMoreElements();)
		{
			Vector<Integer> cur = (Vector<Integer>)e.nextElement();
			
			Enumeration<Integer> eInner = cur.elements();
			Integer firstElement = (Integer)eInner.nextElement();
			Integer curElement = (Integer)eInner.nextElement();
			do
			{
				tri_indices.add(firstElement);
				tri_indices.add(curElement);
				curElement = (Integer)eInner.nextElement();
				tri_indices.add(curElement);
			}while(eInner.hasMoreElements());
		}// biraz kafa karistiriyor, c++ kodu aynen asagida
		/*
		std::vector< std::vector<unsigned short> >::const_iterator it;
		std::vector<unsigned short>::const_iterator it2;
		for (it = indices.begin(); it != indices.end(); ++it)
		{
			// triangulazing the polygons here..
			it2 = it->begin() + 2;
			do
			{
				tri_indices.push_back(*it->begin());
				tri_indices.push_back(*(it2 - 1));
				tri_indices.push_back(*it2);
				++it2;
			}while (it2 != it->end());
		}
		*/

		m_indices = new IntegerBuffer(tri_indices);
	}
	
	public void setIndices1(final Vector<Integer> indices)
	{
		// void setIndices(const std::vector< unsigned short > &indices);
		
		m_indices = new IntegerBuffer(indices);
	}
}
