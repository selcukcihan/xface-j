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
 * XFace::ExpressionChannel
 * bitti.
 */

import com.selcukcihan.xfacej.xengine.Entity;
import com.selcukcihan.xfacej.xengine.MorphChannel;
import com.selcukcihan.xfacej.xengine.MorphController;

public class ExpressionChannel extends MorphChannel
{
/*
class ExpressionChannel :
	public XEngine::MorphChannel
{
protected:
	virtual bool updateResult(XEngine::Entity& result, const XEngine::Entity& rest);
public:
	ExpressionChannel(const std::string& name) : XEngine::MorphChannel(name){}

};
 */
	protected boolean updateResult(Entity result, final Entity rest)
	{
		return MorphController.getInstance().getBlender().blend(result, m_result);
	}
	
	public ExpressionChannel(final String name)
	{
		super(name);
	}
}
