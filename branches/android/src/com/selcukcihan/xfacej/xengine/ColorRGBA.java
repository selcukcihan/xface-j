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
 * XEngine::ColorRGBA
 * bitti.
 */

public class ColorRGBA
{
/*
	class ColorRGBA
	{
	public:
		operator float*() {return &r;};
		operator const float*() const {return &r;};
		ColorRGBA(){}
		ColorRGBA(float _r, float _g, float _b, float _a = 1)
			: r(_r), g(_g), b(_b), a(_a){}
		float r, g, b, a;
	};
 */
	public float r = 0;
	public float g = 0;
	public float b = 0;
	public float a = 0;
	ColorRGBA()
	{
		
	}
	
	ColorRGBA(float _r, float _g, float _b, float _a)
	{
		r = _r;
		g = _g;
		b = _b;
		a = _a;
	}

	ColorRGBA(float _r, float _g, float _b)
	{
		r = _r;
		g = _g;
		b = _b;
		a = 1;
	}
}
