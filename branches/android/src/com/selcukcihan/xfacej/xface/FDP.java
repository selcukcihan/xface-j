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

package com.selcukcihan.xfacej.xface;

/*
 * XFace::FDP
 * bitti.
 */

import java.util.Iterator;
import java.util.LinkedList;

import com.selcukcihan.xfacej.xmath.AxisAngle;
import com.selcukcihan.xfacej.xmath.Quaternion;
import com.selcukcihan.xfacej.xmath.Vector3;

public class FDP
{
	private LinkedList<FDPItem> m_Items; /* typedef std::list<FDPItem*> FDPITEMS; and FDPITEMS m_Items; */ 
	private FAPU m_fapu;
	private AxisAngle m_axisAngle;
	private Vector3 m_translation;  //!< Global translation
	private Vector3 m_leftEyePivot;
	private Vector3 m_rightEyePivot;

	public FDP()
	{
		/*
		 * FDP(void) : m_leftEyePivot(-33.8f, -33.2f, -28.8f), m_rightEyePivot(33.8f, -33.2f, -28.8f), m_translation(0, 0, 0){};
		 */
		m_leftEyePivot = new Vector3(-33.8f, -33.2f, -28.8f);
		m_rightEyePivot = new Vector3(33.8f, -33.2f, -28.8f);
		m_translation = new Vector3(0, 0, 0);
		
		m_axisAngle = new AxisAngle();
		m_fapu = new FAPU();
		m_Items = new LinkedList<FDPItem>();
	}
	
	public LinkedList<FDPItem> getItems()
	{
		/*
		 * const FDPITEMS& getItems() const {return m_Items;};
		 */
		return m_Items;
	}
	
	public int getItemCount()
	{
		/*
		 * int getItemCount() const {return (int)m_Items.size();};
		 */
		return m_Items.size();
	}
	
	public void setES0(float es)
	{
		/*
		 * void setES0(float es){m_fapu.ES = es / 1024.0f;};
		 */
		m_fapu.ES = es / 1024.0f;
	}
	
	public void setIRISD0(float irisd)
	{
		/*
		 * void setIRISD0(float irisd){m_fapu.IRISD = irisd / 1024.0f;};
		 */
		m_fapu.IRISD = irisd / 1024.0f;
	}
	
	public void setENS0(float ens)
	{
		/*
		 * void setENS0(float ens){m_fapu.ENS = ens / 1024.0f;};
		 */
		m_fapu.ENS = ens / 1024.0f;
	}
	
	public void setMNS0(float mns)
	{
		/*
		 * void setMNS0(float mns){m_fapu.MNS = mns / 1024.0f;};
		 */
		m_fapu.MNS = mns / 1024.0f;
	}
	
	public void setMW0(float mw)
	{
		/*
		 * void setMW0(float mw){m_fapu.MW = mw / 1024.0f;};
		 */
		m_fapu.MW = mw / 1024.0f;
	}
	
	public void setFAPU(final FAPU fapus)
	{
		/*
		 * void setFAPU(const FAPU& fapus){m_fapu = fapus;};
		 */
		m_fapu = new FAPU(fapus);
	}
	
	public float getES()
	{
		/*
		 * float getES() const {return m_fapu.ES;};
		 */
		return m_fapu.ES;
	}
	
	public float getIRISD()
	{
		/*
		 * float getIRISD() const {return m_fapu.IRISD;};
		 */
		return m_fapu.IRISD;
	}
	
	public float getENS()
	{
		/*
		 * float getENS() const {return m_fapu.ENS;};
		 */
		return m_fapu.ENS;
	}
	
	public float getMNS()
	{
		/*
		 * float getMNS() const {return m_fapu.MNS;};
		 */
		return m_fapu.MNS;
	}
	
	public float getMW()
	{
		/*
		 * float getMW() const {return m_fapu.MW;};
		 */
		return m_fapu.MW;
	}

	public FAPU getFAPU()
	{
		/*
		 * const FAPU& getFAPU() const {return m_fapu;};
		 */
		return m_fapu;
	}

	public void removeItemsByAffects(final String aff)
	{
		/*
		 * void removeItemsByAffects(const std::string &aff);
		 */
		
		Iterator<FDPItem> it = m_Items.iterator();
		while(it.hasNext())
		{
			if(it.next().getAffects().equals(aff))
				it.remove();
		}
	}
	
	public void setLeftEyePivot(final Vector3 eye)
	{
		/*
		 * void setLeftEyePivot(const Vector3& eye) {m_leftEyePivot = eye;}
		 */
		m_leftEyePivot = new Vector3(eye);
	}
	
	public void setRightEyePivot(final Vector3 eye)
	{
		/*
		 * void setRightEyePivot(const Vector3& eye) {m_rightEyePivot = eye;}
		 */
		m_rightEyePivot = new Vector3(eye);
	}
	
	public void setGlobalTranslation(final Vector3 tr)
	{
		/*
		 * void setGlobalTranslation(const Vector3& tr) {m_translation = tr;}
		 */
		m_translation = new Vector3(tr);
	}
	
	public void setGlobalTranslation(float x, float y, float z)
	{
		/*
		 * void setGlobalTranslation(float x, float y, float z) {m_translation.x = x; m_translation.y = y; m_translation.z = z;}
		 */
		m_translation.x = x;
		m_translation.y = y;
		m_translation.z = z;
	}
	
	public void setGlobalRotation(final AxisAngle axisAng)
	{
		/*
		 * void setGlobalRotation(const AxisAngle& axisAng) {m_axisAngle = axisAng;}
		 */
		m_axisAngle = new AxisAngle(axisAng);
	}
	
	public void setGlobalRotation(final Quaternion q)
	{
		/*
		 * void setGlobalRotation(const Quaternion& q) {m_axisAngle = q.ToAxisAngle();}
		 */
		m_axisAngle = q.ToAxisAngle();
	}
	
	public void setGlobalRotation(float x, float y, float z, float angle)
	{
		/*
		 * void setGlobalRotation(float x, float y, float z, float angle) {m_axisAngle.setAxis(x, y, z); m_axisAngle.setAngle(angle);}
		 */
		m_axisAngle = new AxisAngle();
		m_axisAngle.setAxis(x, y, z);
		m_axisAngle.setAngle(angle);
	}
	
	public Vector3 getLeftEyePivot()
	{
		/*
		 * Vector3 getLeftEyePivot() const {return m_leftEyePivot;}
		 */
		return new Vector3(m_leftEyePivot);
	}
	
	public Vector3 getRightEyePivot()
	{
		/*
		 * Vector3 getRightEyePivot() const {return m_rightEyePivot;}
		 */
		return new Vector3(m_rightEyePivot);
	}

	public Vector3 getGlobalRotationAxis()
	{
		/*
		 * Vector3 getGlobalRotationAxis() const {return m_axisAngle.getAxis();}
		 */
		return m_axisAngle.getAxis();
	}
	
	public float getGlobalRotationAngle()
	{
		/*
		 * float getGlobalRotationAngle() const {return m_axisAngle.getAngle();}
		 */
		return m_axisAngle.getAngle();
	}
	
	public AxisAngle getGlobalAxisAngle()
	{
		/*
		 * const AxisAngle& getGlobalAxisAngle() const {return m_axisAngle;}
		 */
		return new AxisAngle(m_axisAngle);
	}
	
	public Vector3 getGlobalTranslation()
	{
		/*
		 * Vector3 getGlobalTranslation() const {return m_translation;}
		 */
		return new Vector3(m_translation);
	}
	
	public void insertItem(FDPItem item)
	{
		/*
		 * void insertItem(FDPItem* item){m_Items.push_back(item);};
		 */
		m_Items.add(item);
	}

	public FDPItem findItem(final String name, final String aff)
	{
		/*
		 * const FDPItem* findItem(const std::string& name, const std::string& aff) const;
		 */
		for(FDPItem item: m_Items)
		{
			if(item.getName().equals(name) && item.getAffects().equals(aff))
				return item;
		}
		return null;
	}
}
