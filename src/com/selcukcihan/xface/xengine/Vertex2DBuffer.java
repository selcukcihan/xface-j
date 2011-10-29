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

package com.selcukcihan.android.xface.xengine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

public class Vertex2DBuffer
{
	/*
	 * we need these *Buffer classes to be able to pass them to opengl calls
	 * each Vertex2D is formed from 2 floats,
	 * so get and put operations must be talking in terms of Vertex2D,
	 * taking into account this fact 
	 */
	private FloatBuffer m_buffer; /* the underlying buffer, should be direct buffer */
	private ByteBuffer m_byteBuf; /* the direct buffer that backs FloatBuffer */
	
	public Vertex2DBuffer()
	{
		this(0);
	}
	
	public Vertex2DBuffer(int sz)
	{
		/*
		 * underlying m_buffer size = sizeof(a Vertex2D in terms of bytes) * sz
		 * Vertex2D is 2 floats => 2*4 = 8 bytes
		 */
		if(size() != sz)
		{
			m_byteBuf = ByteBuffer.allocateDirect(sz * 2 * 4);
			m_byteBuf.order(ByteOrder.nativeOrder());
		}
		m_buffer = m_byteBuf.asFloatBuffer();
		m_buffer.rewind();
	}

	public Vertex2DBuffer(final Vertex2DBuffer p_rhs)
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

	public Vertex2DBuffer(final Vector<Vertex2D> p_vertices)
	{
		this(p_vertices.size());
		for(Vertex2D v : p_vertices)
			put(v);
		m_buffer.rewind();
	}

	public boolean hasRemaining()
	{
		return m_buffer.hasRemaining();
	}

	public Vertex2D get()
	{
		Vertex2D retVal = new Vertex2D(m_buffer.get(), m_buffer.get());
		return retVal;
	}
	
	public Vertex2D get(int p_index) /* absolute get */
	{
		int floatIndex = p_index << 1; /* multiply by two to convert Vertex2D index into float index */
		Vertex2D retVal = new Vertex2D(m_buffer.get(floatIndex), m_buffer.get(floatIndex + 1));
		return retVal;
	}
	
	public void rewind()
	{
		m_buffer.rewind();
	}
	
	public int size()
	{
		 /* 
		  * size in terms of Vertex2D is equal to float size divided by 2
		  */
		if(m_buffer == null)
			return -1;
		else
			return m_buffer.capacity() >> 1;
	}

	public void put(int p_index, Vertex2D p_v)
	{
		/*
		 * absolute put method
		 */
		int floatIndex = p_index << 1; /* multiply by two to convert Vertex2D index into float index */
		m_buffer.put(floatIndex, p_v.x).put(floatIndex + 1, p_v.y);
	}

	public void put(Vertex2D p_v)
	{
		/*
		 * relative put method
		 */
		m_buffer.put(p_v.x).put(p_v.y);
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
