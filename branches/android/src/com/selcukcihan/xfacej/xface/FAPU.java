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

package com.selcukcihan.android.xface.xface;

/*
 * XFace::FAPU
 * tamamlandi.
 */

public class FAPU
{
	public float ES = 0;
	public float IRISD = 0;
	public float ENS = 0;
	public float MNS = 0;
	public float MW = 0;
	public float AU = 0;
	
	public FAPU()
	{
		// FAPU() : ES(0), IRISD(0), ENS(0), MNS(0), MW(0), AU(0.00001f) {};
		ES = 0;
		IRISD = 0;
		ENS = 0;
		MNS = 0;
		MW = 0;
		AU = 0.00001f;
	}
	
	public FAPU(final FAPU rhs)
	{
		// bu yok c++dakinde
		ES = rhs.ES;
		IRISD = rhs.IRISD;
		ENS = rhs.ENS;
		MNS = rhs.MNS;
		MW = rhs.MW;
		AU = rhs.AU;
	}

}
