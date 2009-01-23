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
 * XEngine::MorphBlender
 * tamam.
 */

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import com.selcukcihan.xfacej.xface.FDP;
import com.selcukcihan.xfacej.xface.FDPItem;
import com.selcukcihan.xfacej.xmath.Vector3;

public class MorphBlender
{
	private Hashtable<String, Set<Integer>> m_lipAreas = new Hashtable<String, Set<Integer>>();
	
	private FDP m_pFDP = new FDP();
	
	private Entity m_restEntity = new Entity();
	private Entity m_priorityChannel = new Entity();
	
	private void resetEntity(Entity ent)
	{
		/*
		 * void resetEntity(Entity& ent) const;
		 */
		
		MeshManager pMM = MeshManager.getInstance();
		
		LinkedList<Drawable> drawables = ent.getDrawables();
		for(Drawable d : drawables)
		{
			DeformableGeometry pMesh = pMM.getMesh(d.getMeshName());
			Vector<Vector3> vertices = new Vector<Vector3>();
			for(int i = 0; i < pMesh.getVertexCount(); i++)
				vertices.add(new Vector3(0, 0, 0));
			pMesh.setVertices(vertices);
		}
	}
	
	private void computeEntityDifference(final Entity rest, Entity dst, final Entity ent)
	{
		/*
		 * void computeEntityDifference(const Entity& rest, Entity& dst, const Entity& ent) const;
		 */
		MeshManager pMM = MeshManager.getInstance();
		
		LinkedList<Drawable> entDr = ent.getDrawables();
		LinkedList<Drawable> dstDr = dst.getDrawables();
		LinkedList<Drawable> restDr = rest.getDrawables();
		
		Iterator<Drawable> it_dst = dstDr.iterator();
		Iterator<Drawable> it_ent = entDr.iterator();
		Iterator<Drawable> it_rest = restDr.iterator();

		while(it_dst.hasNext())
		{
			DeformableGeometry pDst = pMM.getMesh(it_dst.next().getMeshName());
			DeformableGeometry pEnt = pMM.getMesh(it_ent.next().getMeshName());
			DeformableGeometry pRest = pMM.getMesh(it_rest.next().getMeshName());
			
			Vector3Buffer vDst = pDst.getVertices();
			Vector3Buffer vEnt = pEnt.getVertices();
			Vector3Buffer vRest = pRest.getVertices();
			for(int i = 0; i < pDst.getVertexCount(); i++)
			{
				Vector3 d = vDst.get();
				Vector3 e = vEnt.get();
				Vector3 r = vRest.get();
				
				d.x += Math.abs(e.x - r.x);
				d.y += Math.abs(e.y - r.y);
				d.z += Math.abs(e.z - r.z);
				vDst.put(i, d);
			}
			/*
			 * gerek yok set etmeye, bug mu acaba? gerek yok cunku zaten referansi kullanarak degisiklikler yapilmis c++da
			 * pDst.setVertices(vDst);
			 */
		}
	}
	
	private void normalizeEntity(Entity dst)
	{
		/*
		 * void normalizeEntity(Entity& dst) const;
		 */
		MeshManager pMM = MeshManager.getInstance();
		
		for(Drawable d : dst.getDrawables())
		{
			DeformableGeometry pDst = pMM.getMesh(d.getMeshName());
			Vector3Buffer vDst = pDst.getVertices();
			Vector3 maxVal = new Vector3(0, 0, 0);
			for(int i = 0; i < pDst.getVertexCount(); i++) /* while(vDst.hasRemaining()) de denebilirdi */
			{
				Vector3 v = vDst.get(); /* relative get munasiptir, yukarida getVertices deyince zaten position sifirlaniyor */
				if(Math.abs(v.x) > maxVal.x)
					maxVal.x = Math.abs(v.x);
				if(Math.abs(v.y) > maxVal.y)
					maxVal.y = Math.abs(v.y);
				if(Math.abs(v.z) > maxVal.z)
					maxVal.z = Math.abs(v.z);
			}
			vDst.rewind();
			for(int i = 0; i < pDst.getVertexCount(); i++)
			{
				Vector3 v = vDst.get(); /* relative get munasiptir, yukarida rewind deyince zaten position sifirlaniyor */
				if(maxVal.x != 0)
					v.x /= maxVal.x;
				if(maxVal.y != 0)
					v.y /= maxVal.y;
				if(maxVal.z != 0)
					v.z /= maxVal.z;
				vDst.put(i, v); /* set vertices yerine geciyor bu, her eleman icin put yapiyoruz */
			}
		}
	}
	
	private Entity prepareChannelPriority(final Entity channel)
	{
		/*
		 * Entity prepareChannelPriority(const Entity& channel);
		 */
		m_priorityChannel.release(true);
		m_priorityChannel.copyFrom(channel, true);
		
		MeshManager pMM = MeshManager.getInstance();
		LinkedList<Drawable> finDr = m_priorityChannel.getDrawables();
		LinkedList<Drawable> chDr = channel.getDrawables();
		Iterator<Drawable> it_fin = finDr.iterator();
		Iterator<Drawable> it_ch = chDr.iterator();
		while(it_fin.hasNext())
		{
			DeformableGeometry pFin = pMM.getMesh(it_fin.next().getMeshName());
			DeformableGeometry pCh = pMM.getMesh(it_ch.next().getMeshName());
			
			Vector3Buffer vFin = pFin.getVertices();
			Vector3Buffer vCh = pCh.getVertices();
			for(int i = 0; i < pFin.getVertexCount(); i++)
			{
				Vector3 f = vFin.get();
				Vector3 c = vCh.get();
				if(Math.abs(c.x) > 0.25f)
					f.x = 1;	// channel dominant
				else
					f.x = 0;	// channel NOT dominant
				
				if(Math.abs(c.y) > 0.05f)
					f.y = 1;
				else
					f.y = 0;
				if(Math.abs(c.z) > 0.25f)
					f.z = 1;
				else
					f.z = 0;
				
				vFin.put(i, f); /* set vertices yerine geciyor bu, her eleman icin put yapiyoruz */
			}
		}

		return m_priorityChannel;
	}
	
	public Set<Integer> getLipArea(final String meshName)
	{
		/*
		 * const std::set<unsigned short>& getLipArea(const std::string& meshName);
		 */
		Set<Integer> value = m_lipAreas.get(meshName);
		if(value != null)
			return value;
		
		// not created before, try to prepare it then..
		Set<Integer> lipArea = new HashSet<Integer>();
		lipArea.clear();
		if(m_pFDP != null)
		{
			Set<Integer> dummy = new HashSet<Integer>();
			FDPItem pItem = null;
			if((pItem = m_pFDP.findItem("2.2", meshName)) != null)
				lipArea = pItem.getAOI();
			if((pItem = m_pFDP.findItem("2.3", meshName)) != null)
			{
				dummy = pItem.getAOI();
				lipArea.addAll(dummy);
			}
			if((pItem = m_pFDP.findItem("2.7", meshName)) != null)
			{
				dummy = pItem.getAOI();
				lipArea.addAll(dummy);
			}

			if((pItem = m_pFDP.findItem("2.6", meshName)) != null)
			{
				dummy = pItem.getAOI();
				lipArea.addAll(dummy);
			}
			if((pItem = m_pFDP.findItem("2.9", meshName)) != null)
			{
				dummy = pItem.getAOI();
				lipArea.addAll(dummy);
			}
			if((pItem = m_pFDP.findItem("2.8", meshName)) != null)
			{
				dummy = pItem.getAOI();
				lipArea.addAll(dummy);
			}
			//
			if((pItem = m_pFDP.findItem("8.1", meshName)) != null)
			{
				dummy = pItem.getAOI();
				lipArea.addAll(dummy);
			}
			if((pItem = m_pFDP.findItem("8.2", meshName)) != null)
			{
				dummy = pItem.getAOI();
				lipArea.addAll(dummy);
			}
			if((pItem = m_pFDP.findItem("8.5", meshName)) != null)
			{
				dummy = pItem.getAOI();
				lipArea.addAll(dummy);
			}
			if((pItem = m_pFDP.findItem("8.6", meshName)) != null)
			{
				dummy = pItem.getAOI();
				lipArea.addAll(dummy);
			}
			if((pItem = m_pFDP.findItem("8.8", meshName)) != null)
			{
				dummy = pItem.getAOI();
				lipArea.addAll(dummy);
			}
			if((pItem = m_pFDP.findItem("8.7", meshName)) != null)
			{
				dummy = pItem.getAOI();
				lipArea.addAll(dummy);
			}
		}	
		if(!lipArea.isEmpty())
			m_lipAreas.put(meshName, lipArea);

		return lipArea;
	}
	
	public Entity prioritizeChannel(final Hashtable<String, Entity> dictionary, final Entity rest)
	{
		/*
		 * Entity prioritizeChannel(const std::map<std::string, Entity>& dictionary, const Entity& rest);
		 */
		
		/*
		 * m_restEntity = rest; demis c++
		 * burada acaba deep copy yapmama gerek var mi?
		 */
		m_restEntity = rest;
		Entity dictResult = new Entity();
		dictResult.copyFrom(m_restEntity, true); // final result go here
		
		// reset all vertices content to 0, 0, 0 for the priority maps
		resetEntity(dictResult);
		
		for(Entry<String, Entity> e: dictionary.entrySet())
		{
			if(!e.getKey().equals("Rest"))
				computeEntityDifference(m_restEntity, dictResult, e.getValue());
		}

		// normalize
		normalizeEntity(dictResult);

		return prepareChannelPriority(dictResult);
	}

	public void setFDP(FDP pFDP)
	{
		/*
		 * void setFDP(boost::shared_ptr<XFace::FDP> pFDP) {m_pFDP = pFDP;}
		 */
		/*
		 * acaba burada sakatlik oldu mu, deep copy mi gerekiyordu???
		 */
		m_pFDP = pFDP;
	}
	
	public boolean blend(Entity dst, final Entity exp)
	{
		/*
		 * bool blend(Entity& dst, const Entity& exp);
		 */
		if((exp.getDrawableCount() == 0) || (dst.getDrawableCount() == 0))
		{
			System.err.println("Blending has a problem with input entites!");
			return false;
		}
		if(m_priorityChannel.getDrawableCount() == 0)
		{
			System.err.println("Blender priorities are not ready for process!");
			return false;
		}
		MeshManager pMM = MeshManager.getInstance();
		
		// get drawables
		if(m_restEntity.getDrawableCount() == 0)
			System.err.println("Rest frame for blending is not correct!\n");
		final LinkedList<Drawable> restDr = m_restEntity.getDrawables();
		LinkedList<Drawable> finDr = dst.getDrawables();
		final LinkedList<Drawable> blendDr = m_priorityChannel.getDrawables();
		final LinkedList<Drawable> expDr = exp.getDrawables();

		// get iterators
		Iterator<Drawable> it_rst = restDr.iterator();
		Iterator<Drawable> it_fin = finDr.iterator();
		Iterator<Drawable> it_bld = blendDr.iterator();
		Iterator<Drawable> it_exp = expDr.iterator();
		while(it_fin.hasNext())
		{
			Drawable rst = it_rst.next();
			
			if(rst.getBinding().equals("LeftEye") || rst.getBinding().equals("RightEye") || rst.getBinding().equals("Hair"))
			{
				it_fin.next();
				it_bld.next();
				it_exp.next();
				continue;
			}
			
			DeformableGeometry pRst = pMM.getMesh(rst.getMeshName());
			DeformableGeometry pFin = pMM.getMesh(it_fin.next().getMeshName());
			DeformableGeometry pBld = pMM.getMesh(it_bld.next().getMeshName());
			DeformableGeometry pExp = pMM.getMesh(it_exp.next().getMeshName());
			
			// get lip area.
			Set<Integer> lipArea = getLipArea(rst.getMeshName());
				
			final Vector3Buffer vRst = pRst.getVertices();
			Vector3Buffer vFin = pFin.getDeformedVerticesVector();
			final Vector3Buffer vBld = pBld.getVertices();
			final Vector3Buffer vExp = pExp.getDeformedVerticesVector();
			
			for(int i = 0; i < pFin.getVertexCount(); i++)
			{
				Vector3 f = vFin.get();
				Vector3 r = vRst.get();
				Vector3 b = vBld.get();
				Vector3 e = vExp.get();
				
				if(b.x < 0.1f)
					f.x = e.x;
				else
					f.x = 0.25f*f.x + 0.75f*e.x;
			
				if(b.z < 0.1f)
					f.z = e.z;
				else
					f.z = 0.5f*f.z + 0.5f*e.z;
				
				if(b.y < 0.1f)
					f.y = e.y;
				else	// lips must be closed for visemes priority.
				{
					// if lip area from FDP is empty, perhaps FDP has not been set
					// so, play it safe and use visemes only for the lips
					if(!lipArea.isEmpty()) 
					{
						if(lipArea.contains(i)) // NOT in the critical area
							f.y = 0.75f*f.y + 0.25f*e.y;
						else
						{
							float diffVis_y = Math.abs(r.y - f.y);
							if(diffVis_y > m_pFDP.getMW()*102.4)	// makes sense! 1/10 of lip width
								f.y = 0.7f*f.y + 0.3f*e.y;
						}
					}	
				}
			}
			pFin.setDeformedVertices(vFin);
		}

		return true;
	}

}
