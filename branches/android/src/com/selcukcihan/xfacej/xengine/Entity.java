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
 * XEngine::Entity
 * bitti.
 */

import java.util.Iterator;
import java.util.LinkedList;


public class Entity
{
	protected LinkedList<Drawable> m_drawables = null; // std::list<boost::shared_ptr<Drawable> > m_drawables;
	
	protected Transform m_transform = null; // Transform m_transform;
	
	public Entity()
	{
		// Entity() {}
			
		m_drawables = new LinkedList<Drawable>();
		m_transform = new Transform();
	}
	
	public void resetDeformedVertices()
	{
		// void resetDeformedVertices();
		
		for(Iterator<Drawable> it = m_drawables.iterator(); it.hasNext();)
		{
			it.next().resetDeformedVertices();
		}
	}
	
	public Entity copyCPP(final Entity rhs)
	{
		if(this == rhs)
			return this;
		
		m_drawables.clear();
		m_transform = new Transform(rhs.m_transform);
		
		for(Iterator<Drawable> it = rhs.m_drawables.iterator(); it.hasNext();)
		{
			m_drawables.add(it.next());
		}
		return this;
	}
	
	public Entity copyFrom(final Entity rhs, boolean duplicateData)
	{
		// virtual Entity& copyFrom(const Entity& rhs, bool duplicateData = false);
		
		// self assignment control
		if (this == rhs) 
			return this;

		m_drawables.clear();
		m_transform = new Transform(rhs.m_transform);
		
		for(Iterator<Drawable> it = rhs.m_drawables.iterator(); it.hasNext();)
		{
			addDrawable(it.next().clone(duplicateData));
		}
		return this;
	}
	
	public void addDrawables(LinkedList<Drawable> dr)
	{
		// void addDrawables(std::list<boost::shared_ptr<Drawable> >& dr) {m_drawables.insert(m_drawables.end(), dr.begin(), dr.end());}
		
		for(Iterator<Drawable> it = dr.iterator(); it.hasNext();)
		{
			//m_drawables.add(new Drawable(it.next()));
			m_drawables.add(it.next());
		}
	}
	
	public void addDrawable(Drawable dr)
	{
		// void addDrawable(boost::shared_ptr<Drawable>& dr) {m_drawables.push_back(dr);}
		
		//m_drawables.add(new Drawable(dr));
		m_drawables.add(dr);
	}
	
	public LinkedList<Drawable> getDrawables()
	{
		// const std::list<boost::shared_ptr<Drawable> >& getDrawables() const {return m_drawables;}
		
		return m_drawables;
	}
	
	public int getDrawableCount()
	{
		// size_t getDrawableCount() const { return m_drawables.size();}
		
		return m_drawables.size();
	}
	
	public Transform getTransform()
	{
		// const Transform& getTransform() const {return m_transform;}
		
		return m_transform;
	}
	
	public void setTransform(final Transform tr)
	{
		// void setTransform(const Transform& tr) {m_transform = tr;}

		m_transform = new Transform(tr);
	}
	
	public void release(boolean destroyData)
	{
		// virtual void release(bool destroyData = false);
		
		// clear the drawables
		if(destroyData)
		{
			for(Iterator<Drawable> it = m_drawables.iterator(); it.hasNext();)
			{
				it.next().destroyData();
			}
		}
		m_drawables.clear();
	}
	
}
