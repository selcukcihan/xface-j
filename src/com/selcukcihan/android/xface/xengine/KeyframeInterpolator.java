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
 * XEngine::KeyframeInterpolator
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import com.selcukcihan.android.xface.xmath.Vector3;

public class KeyframeInterpolator
{
	private static void interpolateDrawables(Drawable pKey1, Drawable pKey2, Drawable pTarg, float w)
	{
		/*
		 * static void interpolateDrawables(boost::shared_ptr<Drawable> pKey1, boost::shared_ptr<Drawable> pKey2, boost::shared_ptr<Drawable> pTarg, float w);
		 */
		MeshManager pMM = MeshManager.getInstance();
		DeformableGeometry pKeyMesh1 = pMM.getMesh(pKey1.getMeshName());
		DeformableGeometry pKeyMesh2 = pMM.getMesh(pKey2.getMeshName());
		DeformableGeometry pTargMesh = pMM.getMesh(pTarg.getMeshName());
//		std::cerr << pTarg->getMeshName() << std::endl;
		
		int vertCount = pKeyMesh1.getVertexCount();
		final Vector3Buffer pVert1 = pKeyMesh1.getDeformedVerticesVector();
		final Vector3Buffer pVert2 = pKeyMesh2.getDeformedVerticesVector();
		final Vector3Buffer pNorm1 = pKeyMesh1.getNormals();
		final Vector3Buffer pNorm2 = pKeyMesh2.getNormals();
		
		assert(vertCount == pKeyMesh2.getVertexCount()) : "vertex counts not matching in drawable morphing";
		
		Vector<Vector3> pVertTarg = new Vector<Vector3>(vertCount);
		Vector<Vector3> pNormTarg = new Vector<Vector3>(vertCount);
		/*
		for(int i = 0; i < vertCount; i++)
		{
			pVertTarg.add(new Vector3(0,0,0));
			pNormTarg.add(new Vector3(0,0,0));
		}*/
	/*
		std::transform(pVert1.begin(), pVert1.end(), pVert2.begin(), pVertTarg.begin(), linearInt_functor(w));
		std::transform(pNorm1.begin(), pNorm1.end(), pNorm2.begin(), pNormTarg.begin(), linearInt_functor(w));
	*/
		float w_2cube = (2*w*w*w);
		float w_3sqr = (3*w*w);
		
		/* std::transform(pVert1.begin(), pVert1.end(), pVert2.begin(), pVertTarg.begin(), cubicInt_functor(w)); */
		int hh=0;
		while(pVert1.hasRemaining())
		{
			Vector3 v1 = pVert1.get();
			//if(!pVert2.hasRemaining())
			//	System.err.println("olamazki");
			Vector3 v2;
			if(pVert1 != pVert2)
				v2 = pVert2.get();
			else
				v2 = new Vector3(v1);
			Vector3 v3 = v1.opMultiplyScalar(w_2cube - w_3sqr + 1).opAdd(v2.opMultiplyScalar(w_3sqr - w_2cube));
			pVertTarg.add(v3);
			hh++;
		}
		
		assert(pNorm1.size() == pNorm2.size()) : "yeni: normal vertex counts not matching in drawable morphing";
		/* std::transform(pNorm1.begin(), pNorm1.end(), pNorm2.begin(), pNormTarg.begin(), cubicInt_functor(w)); */
		while(pNorm1.hasRemaining())
		{
			Vector3 v1 = pNorm1.get();
			Vector3 v2;
			if(pNorm1 != pNorm2)
				v2 = pNorm2.get();
			else
				v2 = new Vector3(v1);
			Vector3 v3 = v1.opMultiplyScalar(w_2cube - w_3sqr + 1).opAdd(v2.opMultiplyScalar(w_3sqr - w_2cube));
			pNormTarg.add(v3);
			hh++;
		}

		pTargMesh.setDeformedVertices(pVertTarg);
		pTargMesh.setNormals(pNormTarg);
	}
	public static Entity interpolate(final Entity fromEnt1, final Entity fromEnt2,
			Entity toEnt, float w)
	{
		/*
		 * static const Entity& interpolate(const Entity& fromEnt1, const Entity& fromEnt2, Entity& toEnt, float w);
		 */
		LinkedList<Drawable> src1 = fromEnt1.getDrawables();
		LinkedList<Drawable> src2 = fromEnt2.getDrawables();
		if(toEnt.getDrawableCount() != fromEnt1.getDrawableCount())
		{
			toEnt.release(true);
			toEnt.copyFrom(fromEnt1, true);
		}
		
		LinkedList<Drawable> dst  = toEnt.getDrawables();
		Iterator<Drawable> it1 = src1.iterator();
		Iterator<Drawable> it2 = src2.iterator();
		Iterator<Drawable> it3 = dst.iterator();
		while(it1.hasNext())
		{
			assert(it2.hasNext()) : "Keyframe Morpher, src2 drawable count not matching src1's";
			assert(it3.hasNext()) : "Keyframe Morpher, dst drawable count not matching src1's";
		//	if(it3 == dst.end() )
		//		dst.push_back(Drawable(*it1));
			interpolateDrawables(it1.next(), it2.next(), it3.next(), w);
		}
		return toEnt;
	}

}
