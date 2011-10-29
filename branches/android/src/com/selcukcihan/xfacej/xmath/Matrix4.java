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

package com.selcukcihan.xfacej.xmath;

import java.nio.FloatBuffer;

/*
 * XMath::Matrix4
 * tamamlandi.
 */

public class Matrix4
{
	// operator float*() {return &m_data[0];};
	//! float* cast overloaded
	// operator const float*() const {return &m_data[0];}
	//! * operator overloaded (matrix)
	// Matrix4d operator *(const Matrix4d& rhs);
	//Matrix4& operator=(const Matrix4 & rhs);
	//! * operator overloaded (vector)
	//! Copy Constructor
	
	
	private float [] m_data = null;

	public Matrix4()
	{
		// Matrix4();
		m_data = new float[16];
		loadIdentity(); // bunu ben ekledim c++da yapilmamis bu
	}
	
	public Matrix4(final Matrix4 pCopy)
	{
		// Matrix4(const Matrix4& pCopy);
		m_data = new float[16];
		System.arraycopy(pCopy.getMatrix(), 0, m_data, 0, m_data.length);
	}

	Matrix4(float f00, float f01, float f02, float f03,
			float f10, float f11, float f12, float f13, 
			float f20, float f21, float f22, float f23,
			float f30, float f31, float f32, float f33)
	{
		m_data = new float[16];
		m_data[0] = f00; m_data[1] = f01; m_data[2] = f02; m_data[3] = f03;
		m_data[4] = f10; m_data[5] = f11; m_data[6] = f12; m_data[7] = f13;
		m_data[8] = f20; m_data[9] = f21; m_data[10] = f22; m_data[11] = f23;
		m_data[12] = f30; m_data[13] = f31; m_data[14] = f32; m_data[15] = f33;
	}

	Matrix4(final float[] mat)
	{
		// Matrix4(const float mat[16]);
		m_data = new float[16];
		System.arraycopy(mat, 0, m_data, 0, mat.length);
	}
	
	public FloatBuffer createBuffer()
	{
		// c++da yok. float* overloaded op yerine geciyor diye dusun
		// gl fonksiyonlari buffer objesi istiyor o yuzden yazmak zorunda kaldim umarim fazla overhead yoktur
		return FloatBuffer.wrap(m_data);
	}
	
	public void negate()
	{
		// void negate(void);
		for (int i = 0; i < 16; i++)
			m_data[i] = -m_data[i];		
	}
	
	public Matrix4 getInterseTransform()
	{
		// Matrix4 getInverseTransform() const;
		return new Matrix4(	m_data[0],
				m_data[4],
				m_data[8],
				0.0f,
				m_data[1],
				m_data[5],
				m_data[9],
				0.0f,
				m_data[2],
				m_data[6],
				m_data[10],
				0.0f,
				-(m_data[0]*m_data[12]+m_data[1]*m_data[13]+m_data[2]*m_data[14]),
				-(m_data[4]*m_data[12]+m_data[5]*m_data[13]+m_data[6]*m_data[14]),
				-(m_data[8]*m_data[12]+m_data[9]*m_data[13]+m_data[10]*m_data[14]),
				1.0f);		
	}
	
	public void loadIdentity()
	{
		// void loadIdentity();
		for(int i = 0;i<16;i++)
			m_data[i] = 0;
		m_data[0] = m_data[5] = m_data[10] = m_data[15] = 1.0f;
	}
	
	public Matrix4 getTranspose()
	{
		// Matrix4 getTranspose() const;
		return new Matrix4(m_data[0], m_data[4], m_data[8], m_data[12],
				m_data[1], m_data[5], m_data[9], m_data[13],
				m_data[2], m_data[6], m_data[10], m_data[14],
				m_data[3], m_data[7], m_data[11], m_data[15]);		
	}
	
	public Matrix4 opMultiply(final Matrix4 rhs)
	{
		// Matrix4 operator *(const Matrix4& rhs);
		Matrix4 result = new Matrix4();

		double	sum;
		int	index, alpha, beta;		// loop vars
		
		for (index = 0; index < 4; index++)			// perform multiplcation the slow and safe way
		{
			for (alpha = 0; alpha < 4; alpha++)
			{
				sum = 0.0f;

				for (beta = 0; beta < 4; beta++)
					sum += m_data[index + beta*4] * rhs.opIndex(alpha*4 + beta);

				result.opIndexSet(index + alpha*4, (float)sum);
			}	// end for alpha
		}	// end for index

		return result;		
	}
	
	public Matrix4 opMultiply(final float scalar)
	{
		// Matrix4 operator *(const float scalar) const;
		Matrix4 result = new Matrix4();

		for(int i = 0; i < 16; ++i)
			result.opIndexSet(i, m_data[i]*scalar);

		return result;		
	}
	
	public Vector3 opMultiply(final Vector3 rhs)
	{
		// Vector3 operator *(const Vector3& rhs) const;
		float tx = 
			  m_data[0] * rhs.x 
			+ m_data[4] * rhs.y 
			+ m_data[8] * rhs.z 
			+ m_data[12];
		float ty = 
			  m_data[1] * rhs.x 
			+ m_data[5] * rhs.y 
			+ m_data[9] * rhs.z 
			+ m_data[13];
		float tz = 
			  m_data[2] * rhs.x 
			+ m_data[6] * rhs.y 
			+ m_data[10]* rhs.z 
			+ m_data[14];
		return new Vector3(tx,ty,tz);		
	}
	
	public float opIndex(int i, int j)
	{
		// float& operator() (unsigned int i, unsigned int j){return m_data[i*4 + j];};
		return m_data[i*4 + j];
	}
	
	public float opIndex(int i)
	{
		return m_data[i];
	}
	
	public void opIndexSet(int i, float value)
	{
		// lhs[i]=value; diyebiliyordu c++da
		// burada lhs.opIndexSet(i,value); demeli
		m_data[i] = value;
	}

	public float [] getMatrix()
	{
		// const float* getMatrix(){return &m_data[0];};
		return m_data;
	}
	
}
