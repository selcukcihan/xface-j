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
 * - Selcuk Cihan (selcukcihan@gmail.com)
 * ***** END LICENSE BLOCK ***** */

package xengine;

import java.util.Enumeration;
import java.util.Vector;

import xmath.Vector3;

public class VectorWrapper
{
	
	public static Vector<Vector3> WrapVector3(final Vector<Vector3> rhs)
	{
		Vector<Vector3> retVal = new Vector<Vector3>();
		for(Enumeration<Vector3> e = rhs.elements(); e.hasMoreElements();)
		{
			Vector3 curElement = (Vector3)e.nextElement();
			retVal.add(new Vector3(curElement));
		}
		return retVal;
	}
	
	public static Vector<Vertex2D> WrapVertex2D(final Vector<Vertex2D> rhs)
	{
		Vector<Vertex2D> retVal = new Vector<Vertex2D>();
		for(Enumeration<Vertex2D> e = rhs.elements(); e.hasMoreElements();)
		{
			Vertex2D curElement = (Vertex2D)e.nextElement();
			retVal.add(new Vertex2D(curElement));
		}
		return retVal;
	}
	
	public static Vector<Integer> WrapInteger(final Vector<Integer> rhs)
	{
		Vector<Integer> retVal = new Vector<Integer>();
		for(Enumeration<Integer> e = rhs.elements(); e.hasMoreElements();)
		{
			Integer curElement = (Integer)e.nextElement();
			retVal.add(new Integer(curElement));
		}
		return retVal;
	}

	public static Vector<DeformationIndices> WrapDeformationIndices(Vector<DeformationIndices> indices)
	{
		Vector<DeformationIndices> retVal = new Vector<DeformationIndices>();
		for(Enumeration<DeformationIndices> e = indices.elements(); e.hasMoreElements();)
		{
			DeformationIndices curElement = (DeformationIndices)e.nextElement();
			retVal.add(new DeformationIndices(curElement));
		}
		return retVal;
	}

	public static Vector<DeformationWeights> WrapDeformationWeights(Vector<DeformationWeights> m_weights)
	{
		Vector<DeformationWeights> retVal = new Vector<DeformationWeights>();
		for(Enumeration<DeformationWeights> e = m_weights.elements(); e.hasMoreElements();)
		{
			DeformationWeights curElement = (DeformationWeights)e.nextElement();
			retVal.add(new DeformationWeights(curElement));
		}
		return retVal;
	}
}
