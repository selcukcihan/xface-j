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

package com.selcukcihan.android.xface.xmath;

/*
 * XMath::Quaternion
 * tamamlandi.
 */

public class Quaternion
{
	// Quaternion& operator=(const Quaternion& rhs);
	
	public float x = 0;
	public float y = 0;
	public float z = 0;
	public float w = 0;
	
	public Quaternion(float pitch, float yaw, float roll)
	{
		// Quaternion(float pitch, float yaw, float roll);
		float ax = (float)Math.toRadians(pitch)*0.5f;
		float ay = (float)Math.toRadians(yaw)*0.5f;
		float az = (float)Math.toRadians(roll)*0.5f;
		final float sinx = (float)Math.sin(ax);
		final float cosx = (float)Math.cos(ax);
		final float siny = (float)Math.sin(ay);
		final float cosy = (float)Math.cos(ay);
		final float sinz = (float)Math.sin(az);
		final float cosz = (float)Math.cos(az);
		final float cosxcosy = cosx*cosy;
		final float sinxsiny = sinx*siny;

		x = sinz * cosxcosy     - cosz * sinxsiny;
		y = cosz * sinx * cosy + sinz * cosx * siny;
		z = cosz * cosx * siny - sinz * sinx * cosy;
		w = cosz * cosxcosy     + sinz * sinxsiny;		
	}

	public Quaternion(float ix, float iy, float iz, float iw)
	{
		// Quaternion(float ix, float iy, float iz, float iw);
		x = ix;
		y = iy;
		z = iz;
		w = iw;		
	}

	public Quaternion(final AxisAngle axisAngle)
	{
		// Quaternion(const AxisAngle& axisAngle);
		FromAxisAngle(axisAngle);		
	}
	
	public Quaternion(final Quaternion rhs)
	{
		// Quaternion(const Quaternion & rhs);
		x = rhs.x;
		y = rhs.y;
		z = rhs.z;
		w = rhs.w;
	}

	public Quaternion()
	{
		// Quaternion() : x(0), y(0), z(0), w(1.0f) {};
		x = 0;
		y = 0;
		z = 0;
		w = 1;
	}

	public Quaternion Inverse()
	{
		// Quaternion Inverse() const;
	    float norm = Norm();
	    if ( norm > 0.0f )
	    {
	        float invNorm = 1.0f/norm;
	        return new Quaternion(-x*invNorm,-y*invNorm,-z*invNorm, w*invNorm);
	    }
	    else
	    {
	        return new Quaternion();
	    }		
	}
	
	public Quaternion Conjugate()
	{
		// Quaternion Conjugate() const;
		return new Quaternion(-x, -y, -z, w);
	}
	
	public float Norm()
	{
		// float Norm() const;
		return x*x + y*y + z*z + w*w;
	}

	public AxisAngle ToAxisAngle()
	{
		// AxisAngle ToAxisAngle() const;
		float t   = w*w;
		float rsa = t<0.99999999 ? 1.0f/(float)Math.sqrt(1.0f-t) : 1.0f;

		return new AxisAngle(new Vector3(x*rsa, y*rsa, z*rsa),
				(float)Math.acos(w)*2.0f);		
	}
	
	public Matrix4 ToRotationMatrix()
	{
		// Matrix4 ToRotationMatrix() const;
		Matrix4 mat = new Matrix4();
	    float fTx  = 2.0f*x;
	    float fTy  = 2.0f*y;
	    float fTz  = 2.0f*z;
	    float fTwx = fTx*w;
	    float fTwy = fTy*w;
	    float fTwz = fTz*w;
	    float fTxx = fTx*x;
	    float fTxy = fTy*x;
	    float fTxz = fTz*x;
	    float fTyy = fTy*y;
	    float fTyz = fTz*y;
	    float fTzz = fTz*z;

	    mat.opIndexSet(0, 1.0f - (fTyy+fTzz));
	    mat.opIndexSet(1, fTxy - fTwz);
	    mat.opIndexSet(2, fTxz + fTwy);
	    
		mat.opIndexSet(4, fTxy + fTwz);
	    mat.opIndexSet(5, 1.0f - (fTxx+fTzz));
	    mat.opIndexSet(6, fTyz - fTwx);
	    
		mat.opIndexSet(8, fTxz - fTwy);
	    mat.opIndexSet(9, fTyz + fTwx);
	    mat.opIndexSet(10, 1.0f - (fTxx+fTyy));

		mat.opIndexSet(3, 0);
		mat.opIndexSet(7, 0);
		mat.opIndexSet(11, 0);
		mat.opIndexSet(12, 0);
		mat.opIndexSet(13, 0);
		mat.opIndexSet(14, 0);
		mat.opIndexSet(15, 1);

		return mat;		
	}
	
	public void Identity()
	{
		// void Identity();
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
		w = 1.0f;		
	}
	
	public Quaternion opMultiply(final Quaternion rhs)
	{
		// Quaternion operator*(const Quaternion& rhs) const;
		float rx, ry, rz, rw;

		rw = rhs.w*w - rhs.x*x - rhs.y*y - rhs.z*z;

		rx = rhs.w*x + rhs.x*w + rhs.y*z - rhs.z*y;
		ry = rhs.w*y + rhs.y*w + rhs.z*x - rhs.x*z;
		rz = rhs.w*z + rhs.z*w + rhs.x*y - rhs.y*x;

		return new Quaternion(rx, ry, rz, rw);		
	}
	
	public Quaternion opAdd(final Quaternion rhs)
	{
		// Quaternion operator+(const Quaternion& rhs) const;
		return new Quaternion(rhs.x + x, rhs.y + y, rhs.z + z, rhs.w + w);
	}
	
	public Quaternion FromRotationMatrix(Matrix4 mat)
	{
		// Quaternion& FromRotationMatrix(Matrix4 &mat);
		// Calculate the trace of the matrix T from the equation:
		//  T = 4 - 4xx  - 4yy  - 4zz
	    float T = 1.0f + mat.opIndex(0) + mat.opIndex(5) + mat.opIndex(10);

		double s;

		// If the trace of the matrix is greater than zero, then
		//perform an "instant" calculation.
		if ( T > 0.00000001 ) 
		{
			s = Math.sqrt(T) * 2.0;
			x = (float)(( mat.opIndex(9) - mat.opIndex(6) ) / s);
			y = (float)(( mat.opIndex(2) - mat.opIndex(8) ) / s);
			z = (float)(( mat.opIndex(4) - mat.opIndex(1) ) / s);
			w = (float)(0.25 * s);
		}
	    //If the trace of the matrix is equal to zero then identify
	    //which major diagonal element has the greatest value.
	    else if ( mat.opIndex(0) > mat.opIndex(5) && mat.opIndex(0) > mat.opIndex(10) )  
		{	
			// Column 0: 
	        s  = Math.sqrt( 1.0f + mat.opIndex(0) - mat.opIndex(5) - mat.opIndex(10) ) * 2.0;
	        x = (float)(0.25f * s);
	        y = (float)((mat.opIndex(4) + mat.opIndex(1)) / s);
	        z = (float)((mat.opIndex(2) + mat.opIndex(8)) / s);
	        w = (float)((mat.opIndex(9) - mat.opIndex(6)) / s);
		
	    } 
		else if ( mat.opIndex(5) > mat.opIndex(10) ) 
		{			
			// Column 1: 
	        s  = Math.sqrt( 1.0f + mat.opIndex(5) - mat.opIndex(0) - mat.opIndex(10) ) * 2.0;
	        x = (float)((mat.opIndex(4) + mat.opIndex(1) ) / s);
	        y = (float)(0.25f * s);
	        z = (float)((mat.opIndex(9) + mat.opIndex(6) ) / s);
	        w = (float)((mat.opIndex(2) - mat.opIndex(8) ) / s);

	    } 
		else 
		{						
			// Column 2:
	        s  = Math.sqrt( 1.0f + mat.opIndex(10) - mat.opIndex(0) - mat.opIndex(5) ) * 2.0;
	        x = (float)((mat.opIndex(2) + mat.opIndex(8) ) / s);
	        y = (float)((mat.opIndex(9) + mat.opIndex(6) ) / s);
	        z = (float)(0.25f * s);
	        w = (float)((mat.opIndex(4) - mat.opIndex(1) ) / s);
	    }

		return this;		
	}
	public Quaternion FromAxisAngle(final AxisAngle axisAngle)
	{
		// Quaternion& FromAxisAngle(const AxisAngle& axisAngle);
		Vector3 axis = axisAngle.getAxis();
		float sinang = (float)Math.sin(axisAngle.getAngle() / 2);
		x = axis.x * sinang;
		y = axis.y * sinang;
		z = axis.z * sinang;
		w = (float)Math.cos(axisAngle.getAngle() / 2);
		
		return this;		
	}
}
