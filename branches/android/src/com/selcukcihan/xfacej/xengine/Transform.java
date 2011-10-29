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
 * XEngine::Transform
 * tamamlandi.
 */

import java.nio.FloatBuffer;

import com.selcukcihan.android.xface.xmath.Matrix4;
import com.selcukcihan.android.xface.xmath.Quaternion;
import com.selcukcihan.android.xface.xmath.Vector3;

public class Transform
{
	private boolean m_bNeedUpdate;
	private Quaternion m_rotation = new Quaternion();
	private Vector3 m_translation = new Vector3();
	private Vector3 m_scale = new Vector3();
	private Matrix4 m_localTransform = new Matrix4();
	private Matrix4 m_worldTransform = new Matrix4();
	
	public Transform()
	{
		reset(); //yav c++da bunu cagirmamis acaba isteyerek mi yoksa oylesine mi
		m_worldTransform.loadIdentity();
		m_localTransform.loadIdentity();
	}
	
	public Transform(final Transform rhs)
	{
		/*
		 * ben ekledim, sanki copy constructormus gibi.
		 * bu cok onemli bunu koymasam, yani lhs = rhs diye kullansak c++daki kodu,
		 * o zaman lhs ve rhsnin memberlerindeki referanslar ayni objelere olur, yanlis olur
		 * 
		 * ama belki de yan etkisi olmaz, yani tam emin degilim belki de zaten c++daki amac da yeni obje olusturmaktan ziyade
		 * rhsyi disarida artik kullanmayacaktir al artik senin olsun demistir
		 * eger oyleyse bu copy constructora hic gerek yok, lhs = rhs de gec git. overhead azalir
		 */
		m_bNeedUpdate = rhs.m_bNeedUpdate;
		m_rotation = new Quaternion(rhs.m_rotation);
		m_translation = new Vector3(rhs.m_translation);
		m_scale = new Vector3(rhs.m_scale);
		m_localTransform = new Matrix4(rhs.m_localTransform);
		m_worldTransform = new Matrix4(rhs.m_worldTransform);
	}
	
	public void reset()
	{
		// void reset();
		m_scale = new Vector3(1,1,1);
		m_translation = new Vector3(0,0,0);
		m_rotation = new Quaternion(0,0,0,1);
		m_bNeedUpdate = false;
	}
	
	public FloatBuffer getWorldTransform()
	{
		// const Matrix4& getWorldTransform() const {return m_worldTransform;}
		return m_worldTransform.createBuffer();
	}
	
	public FloatBuffer getLocalTransform()
	{
		// const Matrix4& getLocalTransform() const {return m_localTransform;}
		return m_localTransform.createBuffer();
	}
	
	public void updateLocalTransform()
	{
		// void updateLocalTransform();
		if(m_bNeedUpdate)
		{
			Matrix4 rotMat = m_rotation.ToRotationMatrix();
			Matrix4 scaleMat = new Matrix4();
			scaleMat.loadIdentity();
			scaleMat.opIndexSet(0, m_scale.x);
			scaleMat.opIndexSet(5, m_scale.y);
			scaleMat.opIndexSet(10, m_scale.z);

			m_localTransform = rotMat.opMultiply(scaleMat);
			m_localTransform.opIndexSet(12, m_translation.x);
			m_localTransform.opIndexSet(13, m_translation.y);
			m_localTransform.opIndexSet(14, m_translation.z);

			m_bNeedUpdate = false;
		}
	}
	
	public void update(final Matrix4 parentMat)
	{
		// void update(const Matrix4& parentMat);
		updateLocalTransform();
		m_worldTransform = m_localTransform.opMultiply(parentMat); 
	}
	
	public void rotate(final Quaternion qRot)
	{
		// void rotate(const Quaternion& qRot);
		m_rotation = m_rotation.opMultiply(qRot);
		m_bNeedUpdate = true;
	}
	
	public void translate(final Vector3 trans)
	{
		// void translate(const Vector3& trans);
		m_translation = m_translation.opAdd(trans);
		m_bNeedUpdate = true;
	}
	
	public Quaternion getRotation()
	{
		// Quaternion getRotation() const {return m_rotation;}
		return m_rotation;
	}
	
	public Vector3 getTranslation()
	{
		// Vector3 getTranslation() const {return m_translation;}
		return m_translation;
	}
	
	public void setTranslation(final Vector3 trans)
	{
		// void setTranslation(const Vector3& trans) {m_translation = trans;m_bNeedUpdate = true;}
		m_translation = new Vector3(trans);
		m_bNeedUpdate = true;
	}
	
	public void setTranslation(float x, float y, float z)
	{
		// void setTranslation(float x, float y, float z) {m_translation = Vector3(x, y, z);m_bNeedUpdate = true;}
		m_translation = new Vector3(x,y,z);
		m_bNeedUpdate = true;
	}
	
	public void setRotation(final Quaternion qRot)
	{
		// void setRotation(const Quaternion& qRot) {m_rotation = qRot;m_bNeedUpdate = true;}
		m_rotation = new Quaternion(qRot);
		m_bNeedUpdate = true;
	}
	
	public void setScale(final Vector3 scale)
	{
		// void setScale(const Vector3& scale) {m_scale = scale; m_bNeedUpdate = true;}
		m_scale = new Vector3(scale);
		m_bNeedUpdate = true;
	}
	
}
