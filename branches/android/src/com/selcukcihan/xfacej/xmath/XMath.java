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


import com.selcukcihan.android.xface.xengine.IndexedFaceSet;
import com.selcukcihan.android.xface.xengine.IntegerBuffer;
import com.selcukcihan.android.xface.xengine.Vector3Buffer;

/*
 * XMath namespacesinde tanimli fonksiyonlar intersect_triangle* burada implement edildi(raytri.cpp)
 * ayrica intersectMeshRay3 ve lengthSqrRayPnt3 fonksiyonlari implement edildi
 * tamamlandi.
 */

public class XMath
{
	private static final double EPSILON = 0.0000000001;
	
	private static final void CROSS(double [] dest, double [] v1, double [] v2)
	{
		dest[0]=v1[1]*v2[2]-v1[2]*v2[1];
		dest[1]=v1[2]*v2[0]-v1[0]*v2[2];
		dest[2]=v1[0]*v2[1]-v1[1]*v2[0];
	}
	
	private static final double DOT(double [] v1, double [] v2)
	{
		return (v1[0]*v2[0]+v1[1]*v2[1]+v1[2]*v2[2]);
	}
	
	private static final void SUB(double [] dest, double [] v1, double [] v2)
	{
		dest[0]=v1[0]-v2[0];
		dest[1]=v1[1]-v2[1];
		dest[2]=v1[2]-v2[2];
	}
/*
namespace XMath{
	
	bool intersectMeshRay3(const IndexedFaceSet* pMesh, const Ray3& ray, unsigned short& index);
	bool intersectMeshRay3(const IndexedFaceSet* pMesh, const Ray3& ray, Vector3& pnt);

	// by Tomas Moller, May 2000, see raytri.cpp for more info
	int intersect_triangle(double orig[3], double dir[3],
		       double vert0[3], double vert1[3], double vert2[3],
		       double *t, double *u, double *v);
	int intersect_triangle1(double orig[3], double dir[3],
		       double vert0[3], double vert1[3], double vert2[3],
		       double *t, double *u, double *v);
	int intersect_triangle2(double orig[3], double dir[3],
		       double vert0[3], double vert1[3], double vert2[3],
		       double *t, double *u, double *v);
	int intersect_triangle3(double orig[3], double dir[3],
		       double vert0[3], double vert1[3], double vert2[3],
		       double *t, double *u, double *v);

}
 */
	
	public static final float lengthSqrRayPnt3(final Ray3 ray, final Vector3 pnt)
	{
		// float lengthSqrRayPnt3(const Ray3& ray, const Vector3& pnt);
		float dist;
		Vector3 d = new Vector3(ray.getDirection());
		Vector3 o = new Vector3(ray.getOrigin());
		float t0 = d.dot(pnt.opSubtract(o)) / d.dot(d);
		if(t0 <= 0)
			dist = (pnt.opSubtract(o)).lengthSqr();
		else
			dist = (pnt.opSubtract(o.opAdd(d.opMultiplyScalar(t0)))).lengthSqr();
		
		return dist;
	}

	public boolean intersectMeshRay3(final IndexedFaceSet pMesh, final Ray3 ray, int [] index)
	{
		// bool intersectMeshRay3(const IndexedFaceSet* pMesh, const Ray3& ray, unsigned short& index);
		Vector3Buffer pVert = pMesh.getVertices();
		IntegerBuffer pInd = pMesh.getIndices();
		double [] t = new double[1];
		double [] u = new double[1];
		double [] v = new double[1]; // unused vars for now
		for(int i = 0; i < pMesh.getIndexCount(); i+=3)
		{
			Vector3 a = pVert.get(pInd.get(i));
			Vector3 b = pVert.get(pInd.get(i + 1));
			Vector3 c = pVert.get(pInd.get(i + 2));
			// back facing tris eliminated
			if((c.opSubtract(b)).cross(c.opSubtract(a)).dot(ray.getDirection()) < 0)
				continue;

			// double precision conversion
			Vector3 o = ray.getOrigin();
			Vector3 d = ray.getDirection();
			double [] orig = {o.x, o.y, o.z};
			double [] dir = {d.x, d.y, d.z};

			double [] vert0 = {a.x, a.y, a.z};
			double [] vert1 = {b.x, b.y, b.z};
			double [] vert2 = {c.x, c.y, c.z};
			
			// go for ray-tri test
			float dist, distmin = 1000000;
			if(intersect_triangle(orig, dir, vert2, vert0, vert1, t, u, v) != 0)
			{
				index[0] = pInd.get(i);
				// calculate the minimum distanced vertex from the three vertices to the ray
				for (int k = 0; k < 3; ++k)
				{
					dist = lengthSqrRayPnt3(ray, pVert.get(pInd.get(i+k)));
					if( dist < distmin )
					{
						distmin = dist;
						index[0] = pInd.get(i + k);
					}
				}
				
				return true;
			}
		}
		return false;
	}
	
	public boolean intersectMeshRay3(final IndexedFaceSet pMesh, final Ray3 ray, Vector3 pnt)
	{
		// bool intersectMeshRay3(const IndexedFaceSet* pMesh, const Ray3& ray, Vector3& pnt);
		Vector3Buffer pVert = pMesh.getVertices();
		IntegerBuffer pInd = pMesh.getIndices();
		double [] t = new double[1];
		double [] u = new double[1];
		double [] v = new double[1]; // unused vars for now
		for(int i = 0; i < pMesh.getIndexCount(); i+=3)
		{
			Vector3 a = pVert.get(pInd.get(i));
			Vector3 b = pVert.get(pInd.get(i + 1));
			Vector3 c = pVert.get(pInd.get(i + 2));
			
			// back facing tris eliminated
			if((c.opSubtract(b)).cross(c.opSubtract(a)).dot(ray.getDirection()) < 0)
				continue;

			// double precision conversion
			Vector3 o = ray.getOrigin();
			Vector3 d = ray.getDirection();
			double [] orig = {o.x, o.y, o.z};
			double [] dir = {d.x, d.y, d.z};

			double [] vert0 = {a.x, a.y, a.z};
			double [] vert1 = {b.x, b.y, b.z};
			double [] vert2 = {c.x, c.y, c.z};
			
			// go for ray-tri test
			if(intersect_triangle(orig, dir, vert2, vert0, vert1, t, u, v) != 0)
			{
				pnt.setVector(c.opMultiplyScalar((float)(1-u[0]-v[0])).opAdd(
						a.opMultiplyScalar((float)u[0])).opAdd(b.opMultiplyScalar((float)v[0])));
				return true;
			}
		}
		return false;
	}

	
	public static final int intersect_triangle(double [] orig, double [] dir,
			double [] vert0, double [] vert1, double [] vert2,
			double [] t, double [] u, double [] v)
	{
		double [] edge1 = new double[3];
		double [] edge2 = new double[3];
		double [] tvec = new double[3];
		double [] pvec = new double[3];
		double [] qvec = new double[3];
		double det,inv_det;
		/* find vectors for two edges sharing vert0 */
		SUB(edge1, vert1, vert0);
		SUB(edge2, vert2, vert0);
		/* begin calculating determinant - also used to calculate U parameter */
		CROSS(pvec, dir, edge2);
		/* if determinant is near zero, ray lies in plane of triangle */
		det = DOT(edge1, pvec);
		if (det > -EPSILON && det < EPSILON)
			return 0;
		inv_det = 1.0 / det;
		
		/* calculate distance from vert0 to ray origin */
		SUB(tvec, orig, vert0);
		/* calculate U parameter and test bounds */
		u[0] = DOT(tvec, pvec) * inv_det;
		if (u[0] < 0.0 || u[0] > 1.0)
			return 0;
		
		/* prepare to test V parameter */
		CROSS(qvec, tvec, edge1);
		
		/* calculate V parameter and test bounds */
		v[0] = DOT(dir, qvec) * inv_det;
		if (v[0] < 0.0 || u[0] + v[0] > 1.0)
			return 0;
		
		/* calculate t, ray intersects triangle */
		t[0] = DOT(edge2, qvec) * inv_det;
		return 1;
	}

	public static final int intersect_triangle1(double [] orig, double [] dir,
			double [] vert0, double [] vert1, double [] vert2,
			double [] t, double [] u, double [] v)
	{
		double [] edge1 = new double[3];
		double [] edge2 = new double[3];
		double [] tvec = new double[3];
		double [] pvec = new double[3];
		double [] qvec = new double[3];
		double det,inv_det;

		/* find vectors for two edges sharing vert0 */
		SUB(edge1, vert1, vert0);
		SUB(edge2, vert2, vert0);

		/* begin calculating determinant - also used to calculate U parameter */
		CROSS(pvec, dir, edge2);

		/* if determinant is near zero, ray lies in plane of triangle */
		det = DOT(edge1, pvec);

		if (det > EPSILON)
		{
			/* calculate distance from vert0 to ray origin */
			SUB(tvec, orig, vert0);
  
			/* calculate U parameter and test bounds */
			u[0] = DOT(tvec, pvec);
			if (u[0] < 0.0 || u[0] > det)
				return 0;
  
			/* prepare to test V parameter */
			CROSS(qvec, tvec, edge1);
  
			/* calculate V parameter and test bounds */
			v[0] = DOT(dir, qvec);
			if (v[0] < 0.0 || u[0] + v[0] > det)
				return 0;
		}
		else if(det < -EPSILON)
		{
			/* calculate distance from vert0 to ray origin */
			SUB(tvec, orig, vert0);
			/* calculate U parameter and test bounds */
			u[0] = DOT(tvec, pvec);
			/*      printf("*u=%f\n",(float)*u); */
			/*      printf("det=%f\n",det); */
			if (u[0] > 0.0 || u[0] < det)
				return 0;
  
			/* prepare to test V parameter */
			CROSS(qvec, tvec, edge1);
			/* calculate V parameter and test bounds */
			v[0] = DOT(dir, qvec) ;
			if (v[0] > 0.0 || u[0] + v[0] < det)
				return 0;
		}
		else
			return 0;  /* ray is parallell to the plane of the triangle */
		
		inv_det = 1.0 / det;

		/* calculate t, ray intersects triangle */
		t[0] = DOT(edge2, qvec) * inv_det;
		(u[0]) *= inv_det;
		(v[0]) *= inv_det;
		return 1;
	}
	
	public static final int intersect_triangle2(double [] orig, double [] dir,
			double [] vert0, double [] vert1, double [] vert2,
			double [] t, double [] u, double [] v)
	{
		double [] edge1 = new double[3];
		double [] edge2 = new double[3];
		double [] tvec = new double[3];
		double [] pvec = new double[3];
		double [] qvec = new double[3];
		double det,inv_det;

		/* find vectors for two edges sharing vert0 */
		SUB(edge1, vert1, vert0);
		SUB(edge2, vert2, vert0);

		/* begin calculating determinant - also used to calculate U parameter */
		CROSS(pvec, dir, edge2);

		/* if determinant is near zero, ray lies in plane of triangle */
		det = DOT(edge1, pvec);

		/* calculate distance from vert0 to ray origin */
		SUB(tvec, orig, vert0);
		inv_det = 1.0 / det;
		if (det > EPSILON)
		{
			/* calculate U parameter and test bounds */
			u[0] = DOT(tvec, pvec);
			if (u[0] < 0.0 || u[0] > det)
				return 0;
  
			/* prepare to test V parameter */
			CROSS(qvec, tvec, edge1);
			/* calculate V parameter and test bounds */
			v[0] = DOT(dir, qvec);
			if (v[0] < 0.0 || u[0] + v[0] > det)
				return 0;
		}
		else if(det < -EPSILON)
		{
			/* calculate U parameter and test bounds */
			u[0] = DOT(tvec, pvec);
			if (u[0] > 0.0 || u[0] < det)
				return 0;
			/* prepare to test V parameter */
			CROSS(qvec, tvec, edge1);
			/* calculate V parameter and test bounds */
			v[0] = DOT(dir, qvec) ;
			if (v[0] > 0.0 || u[0] + v[0] < det)
				return 0;
		}
		else
			return 0;  /* ray is parallell to the plane of the triangle */

		/* calculate t, ray intersects triangle */
		t[0] = DOT(edge2, qvec) * inv_det;
		(u[0]) *= inv_det;
		(v[0]) *= inv_det;
		return 1;
	}
	
	public static final int intersect_triangle3(double [] orig, double [] dir,
			double [] vert0, double [] vert1, double [] vert2,
			double [] t, double [] u, double [] v)
	{
		double [] edge1 = new double[3];
		double [] edge2 = new double[3];
		double [] tvec = new double[3];
		double [] pvec = new double[3];
		double [] qvec = new double[3];
		double det,inv_det;
		/* find vectors for two edges sharing vert0 */
		SUB(edge1, vert1, vert0);
		SUB(edge2, vert2, vert0);

		/* begin calculating determinant - also used to calculate U parameter */
		CROSS(pvec, dir, edge2);

		/* if determinant is near zero, ray lies in plane of triangle */
		det = DOT(edge1, pvec);

		/* calculate distance from vert0 to ray origin */
		SUB(tvec, orig, vert0);
		inv_det = 1.0 / det;
		CROSS(qvec, tvec, edge1);
		if (det > EPSILON)
		{
			u[0] = DOT(tvec, pvec);
			if (u[0] < 0.0 || u[0] > det)
				return 0;

			/* calculate V parameter and test bounds */
			v[0] = DOT(dir, qvec);
			if (v[0] < 0.0 || u[0] + v[0] > det)
				return 0;
		}
		else if(det < -EPSILON)
		{
			/* calculate U parameter and test bounds */
			u[0] = DOT(tvec, pvec);
			if (u[0] > 0.0 || u[0] < det)
				return 0;
			/* calculate V parameter and test bounds */
			v[0] = DOT(dir, qvec) ;
			if (v[0] > 0.0 || u[0] + v[0] < det)
				return 0;
		}
		else return 0;  /* ray is parallell to the plane of the triangle */

		t[0] = DOT(edge2, qvec) * inv_det;
		(u[0]) *= inv_det;
		(v[0]) *= inv_det;
		
		return 1;
	}
	
}
