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

package xface;

/*
 * XFace::FDPItem
 */

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class FDPItem
{
	/*
	 * typedef std::set<unsigned short> AOI_STORE;
	 */
	private String m_affects;
	private int m_controlPoint;
	private String m_name;
	private Vector<IInfluenceCalculator> m_InfluenceCalculators;
	private boolean m_hasControlPoint;
	private HashSet<Integer> m_AOI;
	
	public FDPItem(final String name)
	{
		/*
		 * FDPItem(const std::string& name) : m_hasControlPoint(false), m_name(name), m_controlPoint(0), m_affects(""){};
		 */
		m_hasControlPoint = false;
		m_name = name;
		m_controlPoint = 0;
		m_affects = new String("");
		m_InfluenceCalculators = new Vector<IInfluenceCalculator>();
		m_AOI = new HashSet<Integer>();
	}
	
	public HashSet<Integer> getAOI()
	{
		/*
		 * const AOI_STORE getAOI() const {return m_AOI;};
		 */
		return m_AOI;
	}

	public String getAffects()
	{
		/*
		 * const std::string& getAffects() const {return m_affects;};
		 */
		return m_affects;
	}

	public int getIndex()
	{
		/*
		 * unsigned short getIndex() const {return m_controlPoint;};
		 */
		return m_controlPoint;
	}

	public String getName()
	{
		/*
		 * const std::string getName() const {return m_name;};
		 */
		return m_name; 
	}
	
	public void setName(final String name)
	{
		/*
		 * void setName(const std::string& name) {m_name = name;};
		 */
		m_name = name;
	}
	
	public boolean hasControlPoint()
	{
		/*
		 * bool hasControlPoint() const {return m_hasControlPoint;};
		 */
		return m_hasControlPoint;
	}
	
	public void setControlPoint(int ind)
	{
		/*
		 * void setControlPoint(unsigned short ind) {m_controlPoint = ind; m_hasControlPoint = true;};
		 */
		m_controlPoint = ind;
		m_hasControlPoint = true;
	}
	
	public void setAffects(final String aff)
	{
		/*
		 * void setAffects(const std::string& aff) {m_affects = aff;};
		 */
		m_affects = aff;
	}
	
	public void addAOIIndex(int ind)
	{
		/*
		 * void addAOIIndex(unsigned short ind) {m_AOI.insert(ind);};
		 */
		m_AOI.add(ind);
	}
	
	public void removeAOIIndex(int ind) 
	{
		/*
		 * void removeAOIIndex(unsigned short ind) {m_AOI.erase(ind);};
		 */
		m_AOI.remove(ind);
	}
	
	public boolean removeInfluenceCalculator(final String type, float w, int fap)
	{
		/*
		 * bool removeInfluenceCalculator(const std::string& type, float w, unsigned short id);
		 */
		
		// seek the coresponding InfluenceCalculator in fdp item
		Iterator<IInfluenceCalculator> it = m_InfluenceCalculators.iterator();
		while(it.hasNext())
		{
			IInfluenceCalculator calc = it.next();
			if((calc.getFapID() == (fap - 1)) // fap parameter is a value starting from 0, while fap id in FDPItem is stored as zero based...
				&& (Math.abs(calc.getCoefficient() - w) < 0.00001 )
				&& (calc.getTypeName().equals(type)))
			{
				it.remove();
				return true;
			}
		}
		return false;
	}
	
	public void modifyInfluenceCalculator(int order, final String type, float w, int fap)
	{
		/*
		 * void modifyInfluenceCalculator(unsigned int order, const std::string& type, float w, unsigned short fap);
		 */
		try
		{
			Class c = Class.forName(type);
			IInfluenceCalculator pInfluence = (IInfluenceCalculator)c.newInstance();
			if(pInfluence != null)
			{
				pInfluence.setFields(w, fap);
				
				pInfluence.init(this);
				if(order < m_InfluenceCalculators.size())
					m_InfluenceCalculators.set(order, pInfluence);
				else
					m_InfluenceCalculators.add(pInfluence);
			}
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(InstantiationException ie)
		{
			ie.printStackTrace();
		}
		catch(IllegalAccessException iae)
		{
			iae.printStackTrace();
		}
	}
	
	public void resetAOI()
	{
		/*
		 * void resetAOI() {m_AOI.clear();}
		 */
		m_AOI.clear();
	}
	
	public int getAOICount()
	{
		/*
		 * int getAOICount() const {return (int)m_AOI.size();};
		 */
		return m_AOI.size();
	}
	
	public Vector<IInfluenceCalculator> getInfluenceCalculators()
	{
		/*
		 * const std::vector<IInfluenceCalculator*>& getInfluenceCalculators() const {return m_InfluenceCalculators;}
		 */
		return m_InfluenceCalculators;
	}
	
	public void addInfluenceCalculator(IInfluenceCalculator pInf)
	{
		/*
		 * void addInfluenceCalculator(IInfluenceCalculator* pInf){m_InfluenceCalculators.push_back(pInf);}
		 */
		m_InfluenceCalculators.add(pInf);
	}
	
	public void clearInfluenceCalculators()
	{
		/*
		 * void clearInfluenceCalculators();
		 */
		throw new UnsupportedOperationException();
	}
}
