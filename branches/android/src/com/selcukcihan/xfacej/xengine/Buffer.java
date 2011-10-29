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
 * XEngine::Buffer
 * tamamlandi.
 * 
 * iyi de custom buffere ne kadar gerek var
 */

import java.util.Vector;
public class Buffer<E>
{
	private Vector<E> m_data = null;
	
	Buffer()
	{
		m_data = new Vector<E>();
	}
	
	public E opIndex(int i)
	{
		// DataType& operator[](int i){ assert(i >= 0 && i < m_data.size());return m_data[i];};
		assert(i >= 0 && i < m_data.size());
		return m_data.elementAt(i);
	}
	
	public Vector<E> getData()
	{
		// const std::vector<DataType>& getData() const {return m_data;}
		return m_data;
	}
	
	public void setData(final Vector<E> data)
	{
		// inline void setData(const std::vector<DataType>& data);
		if(data.size() != m_data.size())
			m_data.clear();
		m_data.addAll(data);
	}
	
	public int size()
	{
		// size_t size() const {return m_data.size();}
		return m_data.size();
	}
}
