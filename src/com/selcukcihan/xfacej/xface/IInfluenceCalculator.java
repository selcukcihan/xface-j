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

package com.selcukcihan.android.xface.xface;

/*
 * XFace::IInfluenceCalculator
 * bitti.
 */

import java.util.Vector;

public abstract class IInfluenceCalculator
{
/*
	class IInfluenceCalculator
	{
	protected:
		float m_coef;
		unsigned short m_fapID;
	public:
		virtual void init(const FDPItem* const pFDP) = 0;
		virtual ~IInfluenceCalculator(void){};
		virtual const std::vector<float>& getWeights() const = 0;
		virtual const std::string getTypeName() const = 0;
		IInfluenceCalculator(float coef, unsigned short fapID) : m_fapID(fapID), m_coef(coef){};
		virtual float getCoefficient() const {return m_coef;}
		unsigned short getFapID() const {return m_fapID;}
	};
 */
	protected float m_coef;
	private int m_fapID;
	
	public IInfluenceCalculator()
	{
		/* needed for Class c.newInstance() since it uses nullary constructor */
	}
	
	public IInfluenceCalculator(float coef, int fapID)
	{
		m_fapID = fapID;
		m_coef = coef;
	}
	
	public void setFields(float coef, int fapID)
	{
		m_fapID = fapID;
		m_coef = coef;
	}
	
	public abstract void init(final FDPItem pFDP);
	
	public abstract Vector<Float> getWeights();
	
	public abstract String getTypeName();
	
	public float getCoefficient()
	{
		return m_coef;
	}
	
	public int getFapID()
	{
		return m_fapID;
	}
	
}
