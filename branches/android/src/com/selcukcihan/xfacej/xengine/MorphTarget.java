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
 * XEngine::MorphTarget
 * bitti. ama
 * Entity x = y demis c++da, buradakiyle semantik farki olustu
 * orada shallow copy yapiliyor ama sonucta entitynin linklistesi yenileniyor
 * biz simdilik x = y biraktik ama dedigim gibi semantik farkli, yan etkisi olma olasiligi var
 */

public class MorphTarget
{
	private Entity m_entity;
	private float m_weight;
	private int m_duration;
	private int m_start;
	private int m_end;
	private String m_alias;
	
	public void setEntity(final Entity ent)
	{
		/*
		 * void setEntity(const Entity& ent) {m_entity = ent;}
		 */
		m_entity = ent;
	}

	public int getStart()
	{
		/*
		 * unsigned int getStart() const {return m_start;}
		 */
		return m_start;
	}
	
	public int getEnd()
	{
		/*
		 * unsigned int getEnd() const {return m_end;}
		 */
		return m_end;
	}
	
	public String getAlias()
	{
		/*
		 * const std::string& getAlias() const {return m_alias;}
		 */
		return m_alias;
	}
	
	public float getWeight()
	{
		/*
		 * float getWeight() const {return m_weight;}
		 */
		return m_weight;
	}
	
	public int getDuration()
	{
		/*
		 * unsigned int getDuration() const {return m_duration;}
		 */
		return m_duration;
	}
	
	public Entity getEntity()
	{
		/*
		 * Entity& getEntity() {return m_entity;}
		 */
		return m_entity;
	}
	
	public MorphTarget(final Entity entity, final String alias, int start, int end, float weight) 
	{
		/*
		 * MorphTarget(const Entity& entity, const std::string& alias, unsigned int start, unsigned int end, float weight) 
		 * : m_entity(entity), m_alias(alias), m_weight(weight), m_duration(end - start), m_start(start), m_end(end){}
		 */
		m_entity = entity;
		m_alias = alias;
		m_weight = weight;
		m_duration = end - start;
		m_start = start;
		m_end = end;
	}
}
