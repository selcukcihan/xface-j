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

package com.selcukcihan.xfacej.xengine;

/*
 * XEngine::RenderManager
 * bitti.
 */

import java.util.LinkedList;

import javax.media.opengl.GL;

public class RenderManager
{
	private Transform m_globalTransform = new Transform();
	private RenderList m_renderList = new RenderList();
	private IRenderer m_pRenderer;
	
	public void resetDrawables()
	{
		/*
		 * void resetDrawables() {m_renderList.clearList();}
		 */
		m_renderList.clearList();
	}
	
	public void addDrawables(final LinkedList<Drawable> dr)
	{
		/*
		 * void addDrawables(const std::list<boost::shared_ptr<Drawable> >& dr) {m_renderList.addDrawables(dr);}
		 */
		m_renderList.addDrawables(dr);
	}
	
	public void setRenderer(IRenderer pRend)
	{
		/*
		 * void setRenderer(boost::shared_ptr<IRenderer> pRend) {m_pRenderer = pRend;}
		 */
		m_pRenderer = pRend;
	}

	public void setGlobalTransform(final Transform tr)
	{
		/*
		 * void setGlobalTransform(const Transform& tr) {m_globalTransform = tr;}
		 */
		m_globalTransform = new Transform(tr);
	}
	
	public void render(GL p_gl)
	{
		/*
		 * void render() const;
		 */
		m_pRenderer.setTransform(m_globalTransform);
		m_renderList.renderList(m_pRenderer, p_gl);
	}
	
	public void update()
	{
		/*
		 * void update();
		 * 
		 * ici bos, unutulmus bisey yok, hersey kontrol altinda
		 */
	}
}
