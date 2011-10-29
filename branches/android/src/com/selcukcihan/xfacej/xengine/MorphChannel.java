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
 * XEngine::MorphChannel
 * bitti.
 * Entity x = y; problemi burada da var,
 * napmali bilmiyom, copyfromdan da ziyade = kullanmak gereken yer var,
 * simdilik x = y diyorum, ileride belki ozel bi fonksiyon copyCpp gibisinden lazim olabilir,
 * cppdeki = 'in semantigini implement etmek icin.
 */

import java.util.LinkedList;
import java.util.ListIterator;

public class MorphChannel
{
	protected String m_name;
	
	protected int m_totalTimePassed;

	// sequence member vars
	private LinkedList<MorphTarget> m_targetSequence = new LinkedList<MorphTarget>();
	private ListIterator<MorphTarget> m_current, m_next;
	protected Entity m_result = new Entity();
	
	private Entity m_unfinishedFrame;
	
	public MorphChannel(final String name)
	{
		/*
		 * MorphChannel(const std::string& name);
		 */
		m_name = name;
		m_totalTimePassed = 0;
		m_unfinishedFrame = null;
	}

	public MorphChannel()
	{
		/*
		 * MorphChannel();
		 */
		m_totalTimePassed = 0;
		m_unfinishedFrame = null;
	}
	
	private MorphTarget smoothTarget(MorphTarget pTrg)
	{
		/*
		 * MorphTarget* smoothTarget(MorphTarget* pTrg);
		 */
		if(pTrg.getAlias().equals("Rest"))
			return pTrg;
		Entity toInsert = new Entity();
		MorphController controller = MorphController.getInstance();
		if(pTrg.getWeight() < 1.0f)
		{
			Entity restEnt = controller.getDictionaryItem("Rest");
			if(controller.getDictionaryItem(pTrg.getAlias()).getDrawableCount() == 0)
				return pTrg;
		
			if(pTrg.getEntity().getDrawableCount() == 0)
				pTrg.setEntity(controller.getDictionaryItem(pTrg.getAlias()));
		
			toInsert.copyFrom(controller.getDictionaryItem(pTrg.getAlias()), true);
			KeyframeInterpolator.interpolate(restEnt, pTrg.getEntity(), toInsert, pTrg.getWeight());
			pTrg.setEntity(toInsert);
		}
		else
		{
			// if not yet initialized
			if(pTrg.getEntity().getDrawableCount() == 0)
			{
				toInsert.copyFrom(controller.getDictionaryItem(pTrg.getAlias()), true);
				pTrg.setEntity(toInsert);
			}
		}

		return pTrg;
	}
	
	private boolean borderFix()
	{
		/*
		 * bool borderFix();
		 */
		if(m_unfinishedFrame != null)
		{
			m_result.copyFrom(m_unfinishedFrame, true);
			m_unfinishedFrame = null;
		}
		return false;
	}
	
	protected boolean updateSelf(int elapsed_time)
	{
		/*
		 * virtual bool updateSelf(unsigned int elapsed_time);
		 */
		// if it's really early, do nothing
		if(elapsed_time < 1)
			return false;

		// update the time
		m_totalTimePassed += elapsed_time;
			
		// if the sequence is empty or we are at the end, return the last/empty entity
		if((!m_next.hasNext()) || m_targetSequence.isEmpty())
			return borderFix();
		
		if(m_totalTimePassed < getCurrentTarget().getStart())	// if it's not time yet
			return borderFix();
		
		if(m_totalTimePassed >= getCurrentTarget().getStart())	
		{
			int cnt = 0;
			while(m_totalTimePassed > getCurrentTarget().getEnd())	// time's up, go to next
			{
				//System.out.println("" + cnt + ": " + getCurrentTarget().getStart() + ", " + getCurrentTarget().getEnd() + ", " + getCurrentTarget().getDuration());
				m_current.next();
				cnt++;
				if(!m_next.hasNext())
					return borderFix();
				m_next.next();
			}
		}
		if(m_totalTimePassed < getCurrentTarget().getStart())	// if we are in a void space
			return borderFix();
		
		// do the interpolation
		float weight = ((float)m_totalTimePassed - (float)(getCurrentTarget()).getStart())
			/ (float)(getCurrentTarget()).getDuration();
		
		KeyframeInterpolator.interpolate(getCurrentTarget().getEntity(), getNextTarget().getEntity(), m_result, weight);
		m_unfinishedFrame = getNextTarget().getEntity();

		return true;
	}

	public String getName()
	{
		/*
		 * const std::string& getName() const {return m_name;}
		 */
		return m_name;
	}

	public void clear()
	{
		/*
		 * virtual void clear();
		 */
		for(MorphTarget mt : m_targetSequence)
			mt.getEntity().release(true);
		m_targetSequence.clear();
		m_current = m_targetSequence.listIterator();
		m_next = m_targetSequence.listIterator();
		m_result.release(true);
		m_totalTimePassed = 0;
		m_unfinishedFrame = null;
	}

	public void pushTarget(MorphTarget pTrg)
	{
		/*
		 * void pushTarget(MorphTarget* pTrg);
		 */
		// if the target sequence empty, we should initialize m_current to the beginning as well
		boolean rew = m_targetSequence.isEmpty();

		m_targetSequence.add(smoothTarget(pTrg));
		if(rew)
			rewind();
	}

	public void pushTargets(final LinkedList<MorphTarget> targets)
	{
		/*
		 * void pushTargets(const MorphTargetList& targets);
		 */
		// if the target sequence empty, we should initialize m_current to the beginning as well
		boolean rew = m_targetSequence.isEmpty();
		
		for(MorphTarget mt : targets)
		{
			smoothTarget(mt);
			m_targetSequence.add(mt);
		}
//		m_targetSequence.insert(m_targetSequence.end(), targets.begin(), targets.end());
			
		if(rew)
			rewind();
	}

	public void rewind()
	{
		/*
		 * void rewind();
		 */
		m_current = m_targetSequence.listIterator(0);
		m_next = m_targetSequence.listIterator(0);
		if(!m_targetSequence.isEmpty())
		{
			m_next.next();
			m_result.release(true);
			//m_result.copyFrom((*m_current)->getEntity(), true);
		}
		m_totalTimePassed = 0;
	}

	public void next()
	{
		/*
		 * void next();
		 */
		if(m_next.hasNext())
		{
			m_current.next();
			m_next.next();
		}
		else
			rewind();
	}

	private boolean updateResult(Entity result, final Entity rest)
	{
		/*
		 * virtual bool updateResult(Entity& result, const Entity& rest);
		 */
		if(m_result.getDrawableCount() != 0)
		{
			//result = m_result; // do not try copyFrom, creates problems..
			result.copyCPP(m_result);
		}
		else
			return false;
		return true;
	}
	
	public boolean update(Entity result, final Entity rest, int elapsed_time)
	{
		/*
		 * bool update(Entity& result, const Entity& rest, unsigned int elapsed_time);
		 */
		if(updateSelf(elapsed_time))
			return updateResult(result, rest);
		
		return false;
	}

	public void dumpDebugInfo()
	{
		/*
		 * void dumpDebugInfo() const;
		 */
		System.err.println("Channel Name: "+m_name);
		for(MorphTarget mt : m_targetSequence)
			System.err.println("\t"+mt.getAlias()+" start: "+mt.getStart()+"\tend: "+mt.getEnd());
		if(m_current.hasNext())
			System.err.println("\tcurrent"+getCurrentTarget().getAlias());
		if(m_next.hasNext())
			System.err.println(" next: "+getNextTarget().getAlias());
	}
	
	public int getTargetCount()
	{
		/*
		 * size_t getTargetCount() const {return m_targetSequence.size();}
		 */
		return m_targetSequence.size();
	}
	
	public MorphTarget getCurrentTarget()
	{
		/*
		 * const MorphTarget* const getCurrentTarget() const {return *m_current;}
		 * 
		 * we need an ugly hack, can not peek on the iterator, go forward then backward to work around the problem
		 */
		MorphTarget currentTarget = m_current.next();
		m_current.previous();
		return currentTarget;
	}

	public MorphTarget getNextTarget()
	{
		/*
		 * bunu ben ekledim c++da yok
		 * (*m_next) kullanilmis kodun bazi yerlerinde, onun yerini tutacak
		 */
		MorphTarget nextTarget = m_next.next();
		m_next.previous();
		return nextTarget;
	}
}
