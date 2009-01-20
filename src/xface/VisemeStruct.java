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
 * struct VisemeStruct
 * tamamlandi.
 */

public class VisemeStruct
{
	public int viseme_select1 = 0;
	public int viseme_select2 = 0;
	public int viseme_blend = 0;
	public int viseme_def = 0;
	
	VisemeStruct()
	{
		// VisemeStruct() : viseme_select1(0), viseme_select2(0), viseme_blend(0), viseme_def(0) {}
		viseme_select1 = 0;
		viseme_select2 = 0;
		viseme_blend = 0;
		viseme_def = 0;
	}
	
	VisemeStruct(final VisemeStruct rhs)
	{
		viseme_blend = rhs.viseme_blend;
		viseme_def = rhs.viseme_def;
		viseme_select1 = rhs.viseme_select1;
		viseme_select2 = rhs.viseme_select2;
	}
	
	VisemeStruct(int a, int b, int c, int d)
	{
		//VisemeStruct(int a, int b, int c, int d = 0) : viseme_select1(a), viseme_select2(b), viseme_blend(c), viseme_def(d) {}
		viseme_select1 = a;
		viseme_select2 = b;
		viseme_blend = c;
		viseme_def = d;
	}

	VisemeStruct(int a, int b, int c)
	{
		viseme_select1 = a;
		viseme_select2 = b;
		viseme_blend = c;
		viseme_def = 0;
	}

}
