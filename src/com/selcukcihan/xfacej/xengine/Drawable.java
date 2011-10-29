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
 * XEngine::Drawable
 * clone() ye bak.
 * static olayi sakat biraz
 * bir de en sakati = new Drawable(*this) denilen kisim
 * tamam ben orada new Drawable() dedim
 * ama muhtemelen memberleri kopyalamam lazim hatta muhtemelen degil harbiden kopyalamaliyim di mi??
 * simdilik dursun, c++daki behaviouru bilmedigim icin, once onu deneyip anlamak lazim
 */

import java.util.Vector;

import com.selcukcihan.android.xface.xface.IFapStream;
import com.selcukcihan.android.xface.xmath.Matrix4;
import com.selcukcihan.android.xface.xmath.Vector3;

public class Drawable
{
	private static int cloneID = -1; //bkz. clone()
	private boolean m_dataExists = false;
	private boolean m_bTextureOn =false;
	private boolean m_bNeedUpdate = false;
	private String m_binding = null;
	//! Name of the Mesh associated
	private String m_MeshName = null;
	//! Names of the textures (Up to 4 textures)
	private String [] m_TexName = null;
	//! Transformation info
	private Transform m_transform = null;
	//! Pivot (needed for eye)
	private Vector3 m_pivot = null;
	//! Animation Parameters, eg; MPEG-4 FAP values
	//std::vector<float> m_APs;
	private IFapStream m_pFAPStream = null;
	
	Drawable()
	{
		// Drawable() : m_dataExists(false), m_bTextureOn(false), m_binding("None"), m_bNeedUpdate(false) {}
		m_dataExists = false;
		m_bTextureOn = false;
		m_binding = new String("None");
		m_bNeedUpdate = false;

		m_MeshName = new String();
		m_TexName = new String[4];
		for(int i = 0; i < m_TexName.length; i++)
			m_TexName[i] = "";
		m_transform = new Transform();
		m_pivot = new Vector3();
	}
	
	Drawable(final Drawable rhs)
	{
		m_dataExists = rhs.m_dataExists;
		m_bTextureOn = rhs.m_bTextureOn;
		m_bNeedUpdate = rhs.m_bNeedUpdate;
		
		m_binding = new String(rhs.m_binding);
		m_MeshName = new String(rhs.m_MeshName);
		m_TexName = new String[4];
		for(int i = 0; i<m_TexName.length; i++)
			m_TexName[i] = new String(rhs.m_TexName[i]);
		
		m_transform = new Transform(rhs.m_transform);
		m_pivot = new Vector3(rhs.m_pivot);
	}
	
	public void resetDeformedVertices()
	{
		// void resetDeformedVertices();
		
		MeshManager pMM = MeshManager.getInstance();
		DeformableGeometry pMesh = pMM.getMesh(m_MeshName);
		pMesh.resetDeformedVertices();
	}
	
	public void setPivot(final Vector3 p)
	{
		// void setPivot(const Vector3& p) {m_pivot = p;}
		m_pivot = new Vector3(p);
	}
	
	public Vector3 getPivot()
	{
		// Vector3 getPivot() const {return m_pivot;}
		return new Vector3(m_pivot);
	}
	
	public float[] getAPs()
	{
		// const std::vector<float>& getAPs();
		if(m_pFAPStream != null)
			return m_pFAPStream.getCurrentFAP(); 

		//static std::vector<float> dummy;
		//return dummy;
		float[] dummy = new float[0]; // valla bilmiyorum ki bu karsiliyor mu amacimizi. yukarida ne yapmis ki
		return dummy;
	}
	
	public void destroyData()
	{
		// void destroyData() const;
		if(m_dataExists)
			MeshManager.getInstance().removeMesh(m_MeshName);
	}

	public Drawable clone(boolean duplicateData)
	{
		// Drawable* clone(bool duplicateData = true) const;

		//static unsigned int cloneID = 0;	// to hash the clones
		//yukaridaki isi yapmak icin bi alicengiz lazim, static member field yapicam
		if(cloneID == -1)
			cloneID = 0;
		Drawable dr = new Drawable(this);
		dr.setBinding(getBinding());
		if(duplicateData)
		{
			dr.m_MeshName = m_MeshName + "clone" + cloneID;
			cloneID++;
			MeshManager pMM = MeshManager.getInstance();
			DeformableGeometry pTargMeshSrc = pMM.getMesh(m_MeshName);
			DeformableGeometry pTargMesh = new DeformableGeometry(dr.m_MeshName);
			pTargMesh.copyFrom(pTargMeshSrc);
			dr.m_dataExists = duplicateData;
			pMM.registerMesh(pTargMesh);
		}

		return dr;
	}


	
	public void updateAnimation()
	{
		// void updateAnimation();
		
		if(m_bNeedUpdate)	// double check..
		{
			m_bNeedUpdate = false;

			DeformableGeometry pGeo = MeshManager.getInstance().getMesh(m_MeshName);
			if(pGeo != null)
				pGeo.update(m_pFAPStream);

			// update the transformation
			m_transform.reset();

			Matrix4 id = new Matrix4();
			id.loadIdentity();
			m_transform.update(id);
		}
	}
	
	public boolean needUpdate()
	{
		// bool needUpdate() const {return m_bNeedUpdate;}
		return m_bNeedUpdate;
	}
	
	public void updateAnimationParams(final IFapStream faps)
	{
		// void updateAnimationParams(const boost::shared_ptr<XFace::IFapStream>& faps) {m_pFAPStream = faps; m_bNeedUpdate = true;}
		
		m_pFAPStream = faps;
		m_bNeedUpdate = true;
	}
	
	public void enableTexture(boolean mode)
	{
		// void enableTexture(bool mode) {m_bTextureOn = mode;}
		m_bTextureOn = mode;
	}
	
	public boolean isTextureOn()
	{
		// bool isTextureOn() const {return m_bTextureOn;}
		return m_bTextureOn;
	}
	
	public String getBinding()
	{
		// const std::string& getBinding() const {return m_binding;}
		return m_binding;
	}
	
	public void setBinding(final String binding)
	{
		// void setBinding(const std::string& binding) {m_binding = binding;}
		m_binding = new String(binding);
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
	
	public void setMeshName(final String MeshName)
	{
		// void setMeshName(const std::string& MeshName) {m_MeshName = MeshName;};
		m_MeshName = new String(MeshName);
	}
	
	public String getMeshName()
	{
		// const std::string& getMeshName() const {return m_MeshName;};
		return m_MeshName;
	}
	
	public void setTexName(final String id, int layer)
	{
		// void setTexName(const std::string id, unsigned short layer = 0)
		assert(layer < 4);
		m_TexName[layer] = new String(id);
		enableTexture(true);
	}
	
	public String getTexName(int layer)
	{
		// const std::string& getTexName(unsigned short layer = 0) const
		assert(layer < 4);
		return m_TexName[layer];
	}

}
