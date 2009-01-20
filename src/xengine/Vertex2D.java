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

package xengine;

import java.nio.FloatBuffer;

/*
 * XEngine::Vertex2D
 * bitti.
 */
public class Vertex2D
{
/*
 *	class Vertex2D
 *	{
 *	public:
 *		union{
 *			float s;
 *			float x;
 *		};
 *		union{
 *			float t;
 *			float y;
 *		};
 *	};	
 */
	public float x = 0;
	public float y = 0;
	
	Vertex2D(final Vertex2D rhs)
	{
		// bunu ben ekledim
		x = rhs.x;
		y = rhs.y;
	}
	
	public Vertex2D()
	{
		// bunu ben ekledim
		x = 0;
		y = 0;
	}

	public Vertex2D(float p_x, float p_y)
	{
		/*
		 * ben ekledim, Vertex2DBuffer icin
		 */
		x = p_x;
		y = p_y;
	}
	
}
