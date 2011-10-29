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

package com.selcukcihan.xfacej.xface;

/*
 * struct ExpressionStruct
 * tamamlandi.
 */

public class ExpressionStruct
{
	public int expression_select1 = 0;
	public int expression_intensity1 = 0;
	public int expression_select2 = 0;
	public int expression_intensity2 = 0;
	public int init_face = 0;
	public int expression_def = 0;

	ExpressionStruct()
	{
		expression_select1 = 0;
		expression_intensity1 = 0;
		expression_select2 = 0;
		expression_intensity2 = 0;
		init_face = 0;
		expression_def = 0;
	}
	
	ExpressionStruct(final ExpressionStruct rhs)
	{
		expression_def = rhs.expression_def;
		expression_intensity1 = rhs.expression_intensity1;
		expression_intensity2 = rhs.expression_intensity2;
		expression_select1 = rhs.expression_select1;
		expression_select2 = rhs.expression_select2;
		init_face = rhs.init_face;
	}
	
	ExpressionStruct(int a, int b, int c, int d, int e, int f)
	{
		expression_select1 = a;
		expression_intensity1 = b;
		expression_select2 = c;
		expression_intensity2 = d;
		init_face = e;
		expression_def = f;
	}

/*
	struct ExpressionStruct
	{
		int expression_select1, expression_intensity1, expression_select2, expression_intensity2, 
			init_face, expression_def;
		ExpressionStruct() : expression_select1(0), expression_intensity1(0), expression_select2(0), expression_intensity2(0), 
			init_face(0), expression_def(0) {}
		ExpressionStruct(int a, int b, int c, int d, int e, int f) 
			: expression_select1(a), expression_intensity1(b), expression_select2(c), expression_intensity2(d), 
			init_face(e), expression_def(f) {}
	};
 */
}
