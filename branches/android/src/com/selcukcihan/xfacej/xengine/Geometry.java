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
 * XEngine::Geometry
 * bitti.
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.FloatBuffer;
import java.util.Vector;

import com.selcukcihan.xfacej.xmath.Vector3;

public abstract class Geometry extends NamedObj
{
	public Vector3Buffer m_vertices = null; // std::vector<Vector3> m_vertices;
	public Vertex2DBuffer m_texCoords = null; // std::vector<Vertex2D> m_texCoords;
	public Vector3Buffer m_normals = null; // std::vector<Vector3> m_normals;
	
	Geometry(final String name)
	{
		// Geometry(const std::string& name) : NamedObj(name){};
		
		super(name);
		m_vertices = new Vector3Buffer();
		m_texCoords = new Vertex2DBuffer();
		m_normals = new Vector3Buffer();
	}
	
	public Vector3 computeCentroid()
	{
		// Vector3 computeCentroid();
		
		Vector3 mean = new Vector3(0, 0, 0);
		Vector3 bMin = new Vector3(1000000, 1000000, 1000000);
		Vector3 bMax = new Vector3(-1000000, -1000000, -1000000);
		
		m_vertices.rewind();
		while(m_vertices.hasRemaining())
		{
			Vector3 curVertex = m_vertices.get();
			if(bMin.x > curVertex.x)
				bMin.x = curVertex.x;
			if(bMin.y > curVertex.y)
				bMin.y = curVertex.y;
			if(bMin.z > curVertex.z)
				bMin.z = curVertex.z;

			if(bMax.x < curVertex.x)
				bMax.x = curVertex.x;
			if(bMax.y < curVertex.y)
				bMax.y = curVertex.y;
			if(bMax.z < curVertex.z)
				bMax.z = curVertex.z;			
		}
		mean = (bMax.opAdd(bMin)).opDivideScalar(2.f);
		return mean;
	}
	
	public Vector3 computeMeanOfVertices()
	{
		// Vector3 computeMeanOfVertices();
		
		Vector3 mean = new Vector3(0, 0, 0);
		m_vertices.rewind();
		while(m_vertices.hasRemaining())
		{
			Vector3 curVertex = m_vertices.get();
			mean = mean.opAdd(curVertex.opDivideScalar((float)m_vertices.size()));
		}
		return mean;
	}
	
	public void subtractMeanFromVertices(final Vector3 mean)
	{
		// void subtractMeanFromVertices(const Vector3& mean);
		for(int i = 0; i < m_vertices.size(); i++)
		{
			Vector3 curVertex = m_vertices.get(i);
			m_vertices.put(i, curVertex.opSubtract(mean));
		}
	}
	
	public void readBinary(RandomAccessFile fp) throws IOException
	{
		// virtual void readBinary(FILE* fp);
		
		int sz = BinaryModelBatchLoader.readUInt(fp);
		if(sz == 0)
			return;

		m_vertices = new Vector3Buffer(sz);
		/* fp.read(m_vertices.byteArray()); */
		for(int i = 0; i < sz; i++)
		{
			Vector3 v = new Vector3();
			v.x = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
			v.y = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
			v.z = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
			m_vertices.put(v);
		}
		m_vertices.rewind();
		
		sz = BinaryModelBatchLoader.readUInt(fp);
		m_normals = new Vector3Buffer(sz);
		/* fp.read(m_normals.byteArray()); */
		for(int i = 0; i < sz; i++)
		{
			Vector3 v = new Vector3();
			v.x = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
			v.y = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
			v.z = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
			m_normals.put(v);
		}
		m_normals.rewind();
	
		sz = BinaryModelBatchLoader.readUInt(fp);
		m_texCoords = new Vertex2DBuffer(sz);
		/* fp.read(m_texCoords.byteArray()); */
		for(int i = 0; i < sz; i++)
		{
			Vertex2D v = new Vertex2D();
			v.x = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
			v.y = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
			m_texCoords.put(v);
		}
		m_texCoords.rewind();
	}
	
	public void writeBinary(RandomAccessFile fp) throws IOException
	{
		// virtual void writeBinary(FILE* fp);
		
		fp.writeInt(m_vertices.size());
		fp.write(m_vertices.byteArray());
		m_vertices.rewind();
		
		fp.writeInt(m_normals.size());
		fp.write(m_normals.byteArray());
		m_normals.rewind();

		fp.writeInt(m_texCoords.size());
		fp.write(m_texCoords.byteArray());
		m_texCoords.rewind();
	}
	
	public Geometry copyFrom(final Geometry rhs)
	{
		// virtual Geometry& copyFrom(const Geometry& rhs);
		
		if(this == rhs)
			return this;
		
		m_vertices = new Vector3Buffer(rhs.m_vertices);
		m_normals = new Vector3Buffer(rhs.m_normals);
		m_texCoords = new Vertex2DBuffer(rhs.m_texCoords);
		
		return this;
	}
	
	public void setVertices(final Vector3 [] pVert, int size)
	{
		// virtual void setVertices(const Vector3* pVert, unsigned int size);
		throw new UnsupportedOperationException();
		/*
		m_vertices = new Vector<Vector3>(size);
		for(int i = 0; i<size; i++)
			m_vertices.add(i, new Vector3(pVert[i]));
			*/
	}
	
	public void setVertices(final Vector<Vector3> vertices)
	{
		// virtual void setVertices(const std::vector<Vector3> &vertices);
		
		/*
		 * m_vertices = VectorWrapper.WrapVector3(vertices);
		 */
		if(m_vertices.size() == vertices.size())
		{
			for(int i = 0; i < vertices.size(); i++)
			{
				m_vertices.put(i, vertices.get(i));
			}
		}
		else
			m_vertices = new Vector3Buffer(vertices);
	}
	
	public void setNormals(final Vector3 [] pNorm, int size)
	{
		// virtual void setNormals(const Vector3* pNorm, unsigned int size);
		
		throw new UnsupportedOperationException();
		/*
		m_normals = new Vector<Vector3>(size);
		for(int i = 0; i<size; i++)
			m_normals.add(i, new Vector3(pNorm[i]));
			*/
		
	}
	
	public void setNormals(final Vector<Vector3> normals)
	{
		// virtual void setNormals(const std::vector<Vector3> &normals);
		
		/*
		 * m_normals = VectorWrapper.WrapVector3(normals);
		 */
		if(m_normals.size() == normals.size())
		{
			for(int i = 0; i < normals.size(); i++)
			{
				m_normals.put(i, normals.get(i));
			}
		}
		else
			m_normals = new Vector3Buffer(normals);
	}
	
	public void setTexCoords(final Vertex2D pTex, int size)
	{
		// virtual void setTexCoords(const Vertex2D* pTex, unsigned int size);
		
		/*
		m_texCoords = new Vector<Vertex2D>(size);
		for(int i = 0; i<size; i++)
			m_texCoords.add(i, new Vertex2D(pTex[i]));
			*/
		throw new UnsupportedOperationException();
	}
	
	public void setTexCoords(final Vector<Vertex2D>  texCoords)
	{
		// virtual void setTexCoords(const std::vector<Vertex2D> &texCoords);
		
		/*
		 * m_texCoords = VectorWrapper.WrapVertex2D(texCoords);
		 */
		if(m_texCoords.size() == texCoords.size())
		{
			for(int i = 0; i < texCoords.size(); i++)
			{
				m_texCoords.put(texCoords.get(i));
			}
		}
		else
			m_texCoords = new Vertex2DBuffer(texCoords);
	}
	
	public Vector3Buffer getVertices()
	{
		// const std::vector<Vector3>& getVertices() const {return m_vertices;}
		m_vertices.rewind();
		return m_vertices;
	}
	
	public Vector3Buffer getNormals()
	{
		// const std::vector<Vector3>& getNormals() const {return m_normals;}//{return m_normals.getRawPointer();}
		m_normals.rewind();
		return m_normals;
	}
	
	public FloatBuffer getNormalsGL()
	{
		// const std::vector<Vector3>& getNormals() const {return m_normals;}//{return m_normals.getRawPointer();}
		m_normals.rewind();
		return m_normals.floatBuffer();
	}
	
	public Vertex2DBuffer getTexCoords()
	{
		// const std::vector<Vertex2D>& getTexCoords() const {return m_texCoords;}
		m_texCoords.rewind();
		return m_texCoords;
	}
	
	public FloatBuffer getTexCoordsGL()
	{
		// const std::vector<Vertex2D>& getTexCoords() const {return m_texCoords;}
		// gl fonksiyonlarina verebilmek icin yukaridakinin overloadedi
		m_texCoords.rewind();
		return m_texCoords.floatBuffer();
	}
	
	public int getVertexCount()
	{
		// size_t getVertexCount() const {return m_vertices.size();}
		
		return m_vertices.size();
	}
	
	public static Vector3 computeFaceNormal(final Vector3 p1, final Vector3 p2, final Vector3 p3)
	{
		// static Vector3 computeFaceNormal(const Vector3& p1, const Vector3& p2, const Vector3& p3);
		
		Vector3 u = new Vector3();
		Vector3 v = new Vector3();
		
		u.x = p2.x - p1.x; u.y = p2.y - p1.y; u.z = p2.z - p1.z;
		v.x = p3.x - p1.x; v.y = p3.y - p1.y; v.z = p3.z - p1.z;

		Vector3 facenormal = u.unitCross(v);

		return facenormal;
	}

}
