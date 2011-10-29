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


package com.selcukcihan.xfacej.xengine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Enumeration;
import java.util.Vector;

import com.selcukcihan.xfacej.xmath.Vector3;

public class BufferWrapper
{

	public static FloatBuffer WrapVector3Vector(Vector<Vector3> vertices)
	{
		FloatBuffer retVal = FloatBuffer.allocate(vertices.size()*3);
		for(Enumeration<Vector3> e = vertices.elements(); e.hasMoreElements();)
		{
			final Vector3 curVector = e.nextElement();
			retVal.put(curVector.x);
			retVal.put(curVector.y);
			retVal.put(curVector.z);
		}
		retVal.rewind();
		return retVal;
	}

	public static FloatBuffer WrapVertex2DVector(Vector<Vertex2D> coords)
	{
		FloatBuffer retVal = FloatBuffer.allocate(coords.size()*2);
		for(Enumeration<Vertex2D> e = coords.elements(); e.hasMoreElements();)
		{
			final Vertex2D curVertex = e.nextElement();
			retVal.put(curVertex.x);
			retVal.put(curVertex.y);
		}
		return retVal;
	}

	public static IntBuffer WrapIntegerVector(Vector<Integer> indices)
	{
		IntBuffer retVal = IntBuffer.allocate(indices.size());
		for(Enumeration<Integer> e = indices.elements(); e.hasMoreElements();)
			retVal.put(e.nextElement());
		retVal.rewind();
		return retVal;
	}

}
