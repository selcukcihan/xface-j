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
 * XEngine::MorphController
 * tamam.
 */

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import com.selcukcihan.xfacej.xface.FDP;

public class MorphController extends ITimedController
{
	private static MorphController m_pThis; /* this is a singleton class */
	private static Entity m_dummy = new Entity();
	
	private MorphBlender m_blender;
	private Transform m_transform;
	
	/* typedef std::map<std::string, std::map<std::string, Entity> > MorphDictionary; */
	private Hashtable<String, Hashtable<String, Entity>> m_dictionaries;
	
	private Entity m_result;
	private LinkedList<MorphChannel> m_channels;
	
	private MorphController()
	{
		/*
		 * MorphController();
		 */
		super();
		
		m_blender = new MorphBlender();
		m_transform = new Transform();
		m_dictionaries = new Hashtable<String, Hashtable<String,Entity>>();
		m_result = new Entity();
		m_channels = new LinkedList<MorphChannel>();
	}
	
	public void update(int elapsed_time)
	{
		/*
		 * void update(unsigned int elapsed_time);
		 */
		if(elapsed_time == 0)
			return;
		
		Entity rest = getDictionaryItem("Rest");
		
		for(MorphChannel ch : m_channels)
		{
			ch.update(m_result, rest, elapsed_time);
		}
	}

	public void clearDictionary()
	{
		/*
		 * void clearDictionary();
		 */
		m_dictionaries.clear();
	}
	
	public void clearAllChannels()
	{
		/*
		 * void clearAllChannels();
		 */
		for(MorphChannel ch : m_channels)
		{
			ch.clear();
		}
		m_channels.clear();
	}
	
	public MorphBlender getBlender()
	{
		/*
		 * MorphBlender* const getBlender() {return &m_blender;}
		 */
		return m_blender;
	}
	
	public void dumpDebugInfo()
	{
		/*
		 * void dumpDebugInfo() const;
		 */
		for(MorphChannel ch : m_channels)
			ch.dumpDebugInfo();
	}
	
	public void setFDP(FDP pFDP)
	{
		/*
		 * void setFDP(boost::shared_ptr<XFace::FDP> pFDP) {m_blender.setFDP(pFDP); reset();}
		 */
		
		m_blender.setFDP(pFDP);
		reset();
	}
	
	public Transform getTransform()
	{
		/* 
		 * const Transform& getTransform() const {return m_transform;}
		 */
		
		return m_transform;
	}
	
	public void setTransform(final Transform tr)
	{
		/*
		 * void setTransform(const Transform& tr) {m_transform = tr;}
		 */
		m_transform = new Transform(tr);
	}
	
	public void processDictionary()
	{
		/*
		 * void processDictionary();
		 */
		Hashtable<String, Entity> ht = m_dictionaries.get("Viseme");
		if(ht != null)
			m_blender.prioritizeChannel(ht, getDictionaryItem("Rest"));
	}
	
	public void addChannel(final String name)
	{
		/*
		 * void addChannel(const std::string& name);
		 */
		MorphChannel channel = findChannel(name);
		if(channel == null)
			m_channels.add(new MorphChannel(name));
	}
	
	public void addChannel(MorphChannel ch)
	{
		/*
		 * void addChannel(boost::shared_ptr<MorphChannel> ch);
		 */
		MorphChannel channel = findChannel(ch.getName());
		if(channel == null)
			m_channels.add(ch);
	}
	
	public void next()
	{
		/*
		 * void next();
		 */
		
		for(MorphChannel ch : m_channels)
			ch.next();
	}
	
	public void rewind()
	{
		/*
		 * void rewind();
		 */
		
		for(MorphChannel ch : m_channels)
			ch.rewind();
	}
	
	public void reset()
	{
		/*
		 * void reset();
		 */
		clearAllChannels();
		m_result.release(true);
		m_result.copyFrom(getDictionaryItem("Rest"), true);
	}
	
	public Entity getResult()
	{
		/*
		 * const Entity& getResult() const {return m_result;}
		 */
		return m_result;
	}
	
	public Entity getDictionaryItem(final String key)
	{
		/*
		 * const Entity& getDictionaryItem(const std::string& key);
		 */
		
		for(Hashtable<String, Entity> dict : m_dictionaries.values())
		{
			if(dict.containsKey(key))
				return dict.get(key);
		}
		/*
		 * burada static Entity yapip onu dondurmus, biz m_dummy kullandik bilemiyorum sakincasi olacak mi
		 */
		return m_dummy;
		
	}
	
	public Hashtable<String, Hashtable<String, Entity>> getDictionary()
	{
		/*
		 * MorphDictionary getDictionary() const;
		 */
		return m_dictionaries;
	}
	
	public void removeDictionaryTarget(final String key)
	{
		/*
		 * void removeDictionaryTarget(const std::string& key);
		 */
		for(Hashtable<String, Entity> ht : m_dictionaries.values())
		{
			if(ht.contains(key))
				ht.remove(key);
		}
	}
	
	public void addDictionaryTarget(final String al, final String cat, final Entity ent)
	{
		/*
		 * void addDictionaryTarget(const std::string& al, const std::string& cat, const Entity& ent);
		 */
		
		Hashtable<String, Entity> ht = m_dictionaries.get(cat);
		if(ht == null)
		{
			ht = new Hashtable<String, Entity>();
			m_dictionaries.put(cat, ht);
		}
		
		ht.put(al, ent);
	}

	public void clearChannel(final String channel)
	{
		/*
		 * void clearChannel(const std::string& channel);
		 */
		MorphChannel ch = findChannel(channel);
		if(ch != null)
			ch.clear();
	}
	
	public void pushTarget(final MorphTarget pTrg, final String channel)
	{
		/*
		 * void pushTarget(MorphTarget* pTrg, const std::string& channel);
		 */
		MorphChannel ch = findChannel(channel);
		if(ch != null)
			ch.pushTarget(pTrg);
	}
	
	public void pushTargets(final LinkedList<MorphTarget> targets, final String channel)
	{
		/*
		 * void pushTargets(const MorphTargetList& targets, const std::string& channel);
		 * 
		 * where MorphTargetList is
		 * 		typedef std::list<MorphTarget*> MorphTargetList;
		 */
		MorphChannel ch = findChannel(channel);
		if(ch != null)
			ch.pushTargets(targets);
	}
	
	public MorphChannel findChannel(final String name)
	{
		/*
		 * ChannelItem findChannel(const std::string& name);
		 * 
		 * where ChannelItem is
		 * 		typedef boost::shared_ptr<MorphChannel> ChannelItem;
		 */
		for(Iterator<MorphChannel> it = m_channels.iterator(); it.hasNext();)
		{
			MorphChannel cur = it.next();
			if(cur.getName().equals(name))
				return cur;
		}
		return null;
		//return new MorphChannel(); c++da bu neydi du bakalim, nulla tekabul ediyodu ya neyse
	}
	
	public static MorphController getInstance()
	{
		/*
		 * static MorphController* getInstance();
		 * 
		 * singleton access
		 */
		if(m_pThis == null)
			m_pThis = new MorphController();
		return m_pThis;
	}
}
