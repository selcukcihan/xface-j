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

package xface;

/*
 * XFace::RaisedCosInfluence
 * bitti.
 */

import java.util.HashSet;
import java.util.Vector;

import xengine.Vector3Buffer;
import xmath.Vector3;

public abstract class RaisedCosInfluence extends IInfluenceCalculator
{
	protected Vector<Float> m_weights;
	
	public RaisedCosInfluence()
	{
		/* needed for Class c.newInstance() since it uses nullary constructor */
		super();
		m_weights = new Vector<Float>();
	}
	
	public RaisedCosInfluence(float weight, int fapID)
	{
		/*
		 * RaisedCosInfluence(float weight, unsigned short fapID) :  IInfluenceCalculator(weight, fapID){}
		 */
		super(weight, fapID);
		m_weights = new Vector<Float>();
	}
	
	/* virtual float calculateDistance(const Vector3& p1, const Vector3& p2) = 0;  */
	protected abstract float calculateDistance(final Vector3 p1, final Vector3 p2);
	
	private float calculateDistances(final FDPItem pFDP)
	{
		/*
		 * float calculateDistances(const FDPItem* const pFDP);
		 */
		float maxDist = 0;
		m_weights.clear();
		
		final Vector3Buffer pV = xengine.MeshManager.getInstance().getMesh(pFDP.getAffects()).getVertices();
		assert(pV.size() != 0) : "RaisedCosInfluenceSph::calculateDistances() could not find any vertices";

		Vector3 pivot = pV.get(pFDP.getIndex());
		
		HashSet<Integer> indices = pFDP.getAOI();
		// for each vertex in area of interest, calculates the distance to the pivot (control) point
		// also calculates the farthest point
		for(int i : indices)
		{
			float val = calculateDistance(pivot, pV.get(i));
			m_weights.add(val);
			if(val > maxDist)
				maxDist = val;
		}
		// calculate directly the raised cosine, not the distance only (nice optimization)
		// std::for_each(m_weights.begin(), m_weights.end(), weight_functor(maxDist, m_coef));
		for(int i = 0; i < m_weights.size(); i++)
			m_weights.set(i, (float)(Math.cos((Math.PI*(m_weights.get(i))/maxDist)) + 1)*m_coef);
		
		return maxDist;
	}

	public Vector<Float> getWeights()
	{
		/*
		 * const std::vector<float>& getWeights() const {return m_weights;}
		 */
		return m_weights;
	}

	public void init(final FDPItem pFDP)
	{
		/*
		 * void init(const FDPItem* const pFDP);
		 */
		calculateDistances(pFDP);
	}

}
