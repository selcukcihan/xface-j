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
 * XEngine::RenderList
 * bitti.
 */

import java.util.Iterator;
import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL11;

public class RenderList
{
/*
	typedef std::list<boost::shared_ptr<Drawable> > RLIST;
	RLIST m_List;
 */
	private LinkedList<Drawable> m_List = new LinkedList<Drawable>();
	
	//! Adds a Drawable pointer to the list.
	public void addDrawable(Drawable item)
	{
		/*
		 * void addDrawable(boost::shared_ptr<Drawable> item){m_List.push_back(item);}
		 */
		m_List.add(item);
	}
	//! Adds a Drawable list to the list.
	public void addDrawables(final LinkedList<Drawable> dr)
	{
		/*
		 * void addDrawables(const std::list<boost::shared_ptr<Drawable> >& dr){m_List.insert(m_List.end(), dr.begin(), dr.end());}
		 */
		m_List.addAll(dr);
	}
	//! Removes a Drawable with given name.
	public void removeDrawable(final String name)
	{
		/*
		 * void removeDrawable(const std::string& name);
		 */
		Iterator<Drawable> it = m_List.iterator();
		while (it.hasNext())
		{
			if(it.next().getMeshName() == name)
			{
				it.remove();
				break;
			}
		}
	}
	
	//! Clears the list.
	public void clearList()
	{
		/*
		 * void clearList(){m_List.clear();};
		 */
		m_List.clear();
	}
	//! Sends the contents of the list to IRenderer sequentially.
	public void renderList(IRenderer pRenderer, GL11 p_gl)
	{
		/*
		 * void renderList(boost::shared_ptr<IRenderer> pRenderer) const;
		 */
		for(Drawable d : m_List)
		{
			pRenderer.render(d, p_gl);
		}
	}
}
