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
 * XFace::RaisedCosInfluenceWaveY
 * bitti.
 */

import xmath.Vector3;

public class RaisedCosInfluenceWaveY extends RaisedCosInfluence
{
	public RaisedCosInfluenceWaveY()
	{
		/* needed for Class c.newInstance() since it uses nullary constructor */
		super();
	}
	
	
	public RaisedCosInfluenceWaveY(float weight, int fapID)
	{
		super(weight, fapID);
	}

	protected float calculateDistance(final Vector3 p1, final Vector3 p2)
	{
		return (float)Math.abs(p1.y - p2.y);
	}

	public String getTypeName()
	{
		return this.getClass().getName();
	}

}
