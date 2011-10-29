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

import java.util.LinkedList;

/*
 * struct MeshInfo
 */

public class MeshInfo
{
	public String file = new String();
	public String format = new String();
	public String path = new String();
	public String keyframe_alias = new String();
	public String keyframe_category = new String();
	public LinkedList<Drawable> drawables = new LinkedList<Drawable>();
	
	public MeshInfo()
	{
		
	}
	
	public MeshInfo(final MeshInfo rhs)
	{
		/*
		 * copy ctor gibin
		 */
		file = new String(rhs.file);
		format = new String(rhs.format);
		path = new String(rhs.path);
		keyframe_alias = new String(rhs.keyframe_alias);
		keyframe_category = new String(rhs.keyframe_category);
		drawables = new LinkedList<Drawable>();
		for(Drawable d : rhs.drawables)
		{
			// ANDROID
			//drawables.push(new Drawable(d));
			drawables.add(0, new Drawable(d));
		}
	}
}
