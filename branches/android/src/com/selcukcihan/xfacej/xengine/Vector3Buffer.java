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
 * - Selcuk Cihan (selcukcihan@gmail.com)
 * ***** END LICENSE BLOCK ***** */

package com.selcukcihan.xfacej.xengine;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import com.selcukcihan.xfacej.xmath.Vector3;

public class Vector3Buffer
{
	/*
	 * we need these *Buffer classes to be able to pass them to opengl calls
	 * each Vector3 is formed from 3 floats,
	 * so get and put operations must be talking in terms of Vector3,
	 * taking into account this fact 
	 */
	private FloatBuffer m_buffer = null; /* the underlying buffer, should be direct buffer */
	private ByteBuffer m_byteBuf = null; /* the direct buffer that backs FloatBuffer */
	
	public Vector3Buffer()
	{
		this(0);
	}
	
	public Vector3Buffer(int sz)
	{
		/*
		 * underlying m_buffer size = sizeof(a Vector3 in terms of bytes) * sz
		 * Vector3 is 3 floats => 3*4 = 12 bytes
		 */
		if(size() != sz)
		{
			m_byteBuf = ByteBuffer.allocateDirect(sz * 3 * 4);
			m_byteBuf.order(ByteOrder.nativeOrder());
		}
		m_buffer = m_byteBuf.asFloatBuffer();
		m_buffer.rewind();
	}
	
	public Vector3Buffer(final Vector3Buffer p_rhs)
	{
		/*
		 * like a copy constructor
		 */
		this(p_rhs.size());
		p_rhs.rewind();
		for(int i = 0; i < p_rhs.size(); i++)
		{
			put(p_rhs.get());
		}
		m_buffer.rewind();
	}

	public Vector3Buffer(final Vector<Vector3> p_vertices)
	{
		this(p_vertices.size());
		for(Vector3 v : p_vertices)
			put(v);
		m_buffer.rewind();
	}

	public boolean hasRemaining()
	{
		return m_buffer.hasRemaining();
	}

	public Vector3 get()
	{
		Vector3 retVal = new Vector3(m_buffer.get(), m_buffer.get(), m_buffer.get());
		return retVal;
	}
	
	public Vector3 get(int p_index) /* absolute get */
	{
		int floatIndex = p_index * 3;
		Vector3 retVal = new Vector3(m_buffer.get(floatIndex), m_buffer.get(floatIndex + 1), m_buffer.get(floatIndex + 2));
		return retVal;
	}
	
	public void rewind()
	{
		m_buffer.rewind();
	}
	
	public int size()
	{
		/*
		 * size in terms of Vertex3 size, that is, each Vertex3 occupies 3 floats in the backbone buffer
		 */
		if(m_buffer == null)
			return -1;
		else
			return m_buffer.capacity() / 3;
	}

	public void put(int p_index, Vector3 p_v)
	{
		/*
		 * absolute put method
		 */
		int floatIndex = p_index * 3;
		m_buffer.put(floatIndex, p_v.x).put(floatIndex + 1, p_v.y).put(floatIndex + 2, p_v.z);
	}

	public void put(Vector3 p_v)
	{
		/*
		 * relative put method
		 */
		m_buffer.put(p_v.x).put(p_v.y).put(p_v.z);
	}
	
	public byte [] byteArray()
	{
		return m_byteBuf.array();
	}
	
	public float [] floatArray()
	{
		return m_buffer.array();
	}
	
	public FloatBuffer floatBuffer()
	{
		return m_buffer;
	}

}
