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
 * XMath::Vector3
 * tamamlandi.
 */

public class Vector3
{
	// asagidaki operatorlerin karsiligi implement edilmedi
	// inline Vector3 &Vector3::operator=(const Vector3 &rhs)
	// inline Vector3& Vector3::operator/= (float scalar)
	// inline Vector3& Vector3::operator-= (const Vector3& rhs)
	// inline Vector3& Vector3::operator+= (const Vector3& rhs)
	// inline Vector3& Vector3::operator *=(const float scalar)
	
	//! float* type cast operator
	// inline operator float*() {return &x;};
	//! const float* type cast operator
	// inline operator const float*() const {return &x;};	
	
	// #define INFINITY 3.402823466e+38F
	public static final float INFINITY = 3.402823466e+38F;
	public float x = 0;
	public float y = 0;
	public float z = 0;
	
	public Vector3()
	{
		
	}
	public Vector3(final float ix, final float iy, final float iz)
	{
		// Vector3(const float ix, const float iy, const float iz);
		x = ix;
		y = iy;
		z = iz;
	}
	public Vector3(final Vector3 pCopy)
	{
		// Vector3(const Vector3& pCopy);
		x = pCopy.x;
		y = pCopy.y;
		z = pCopy.z;		
	}

	public void setVector(final Vector3 pCopy)
	{
		//bu metod c++da yok
		x = pCopy.x;
		y = pCopy.y;
		z = pCopy.z;
	}
	public void setVector(final float ix, final float iy, final float iz)
	{
		//bu metod c++da yok
		x = ix;
		y = iy;
		z = iz;
	}	
	public Vector3 opDivideScalar(final float scalar)
	{
		Vector3 result = new Vector3(INFINITY, INFINITY, INFINITY);

		// A small optimization since multiplication is faster than division.
		if( scalar != 0.0 )
		{
			result = new Vector3();
			float inv_scalar = 1.0f/scalar;
			result.x = inv_scalar*x;
			result.y = inv_scalar*y;
			result.z = inv_scalar*z;
		}
		return result;
	}
	
	public Vector3 opMultiplyScalar(final float scalar)
	{
		// inline Vector3 Vector3::operator*(const float scalar) const
		Vector3 res = new Vector3();
		res.x = x*scalar;
		res.y = y*scalar;
		res.z = z*scalar;
		return res;
	}
	
	public Vector3 opAdd(final Vector3 rhs)
	{
		// inline Vector3 Vector3::operator+ (const Vector3& rhs) const
		Vector3 result = new Vector3();
	    result.x = x + rhs.x;
	    result.y = y + rhs.y;
	    result.z = z + rhs.z;
	    return result;		
	}
	
	public Vector3 opSubtract(final Vector3 rhs)
	{
		// inline Vector3 Vector3::operator- (const Vector3& rhs) const
		Vector3 result = new Vector3();
	    result.x = x - rhs.x;
	    result.y = y - rhs.y;
	    result.z = z - rhs.z;
	    return result;		
	}
	
	public Vector3 opSubtract()
	{
		// inline Vector3 Vector3::operator- () const
		return new Vector3(-x,-y,-z);
	}
	
	public boolean opIsEqual(final Vector3 rhs)
	{
		// inline bool Vector3::operator== (const Vector3& rhs) const
		return ( x == rhs.x && y == rhs.y && z == rhs.z );
	}
	
	public boolean opIsNotEqual(final Vector3 rhs)
	{
		// inline bool Vector3::operator!= (const Vector3& rhs) const
		return ( x != rhs.x || y != rhs.y || z != rhs.z );
	}
	
	public Vector3 normalize(float tolerance)
	{
		// inline Vector3& Vector3::normalize(float tolerance)
		float mag = length();
		
		if ( mag > tolerance )
		{
		    float inv_mag = 1.0f/mag;
		    x *= inv_mag;
		    y *= inv_mag;
		    z *= inv_mag;
		}
		else
		{
		    mag = tolerance;
		}
		
		return this;		
	}
	
	public Vector3 normalize()
	{
		// mecburen overloaded versiyonunu yaptim cunku c++da default parameter kullanilmis tolerance icin
		return normalize(1e-06f);
	}
	
	public float dot(final Vector3 rhs)
	{
		// inline float Vector3::dot(const Vector3& rhs) const
		return x*rhs.x + y*rhs.y + z*rhs.z;
	}
	
	public float lengthSqr()
	{
		// inline float Vector3::lengthSqr() const
		return x*x + y*y + z*z;
	}
	
	public float length()
	{
		// inline float Vector3::length() const
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vector3 unitCross(final Vector3 rhs)
	{
		// inline Vector3 Vector3::unitCross (const Vector3& rhs) const
		Vector3 result = new Vector3();
		
		result.x = y*rhs.z - z*rhs.y;
		result.y = z*rhs.x - x*rhs.z;
		result.z = x*rhs.y - y*rhs.x;
		result.normalize();
		
		return result;		
	}
	
	public Vector3 cross(final Vector3 rhs)
	{
		// inline Vector3 Vector3::cross(const Vector3& rhs) const
		return new Vector3(	y*rhs.z - z*rhs.y,
							z*rhs.x - x*rhs.z,
							x*rhs.y - y*rhs.x);		
	}
}
