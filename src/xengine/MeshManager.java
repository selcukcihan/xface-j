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

/*
 * sakatlik var gibi
 * singleton falan demis, static self pointer var bana gereksizmis gibi geldi
 * ama olmayabilir de
 */

import java.util.Hashtable;

public class MeshManager
{
	Hashtable<String, DeformableGeometry> m_Storage = new Hashtable<String, DeformableGeometry>();
	static MeshManager m_pThis = null; //???
	
	MeshManager()
	{
		// MeshManager(void);
	}
	
	public static void releaseInstance()
	{
		// static void releaseInstance();
		m_pThis = null;
	}
	
	public static MeshManager getInstance()
	{
		// static MeshManager* getInstance();
		if(m_pThis == null)
		{
			m_pThis = new MeshManager();
			DeformableGeometry.prepareFAPLookup();
		}
		return m_pThis;
	}
	
	public void destroyAll()
	{
		// void destroyAll();
	}
	
	public boolean removeMesh(final String name)
	{
		// bool removeMesh(const std::string& name);
		return (m_Storage.remove(name) != null);
	}
	
	public void registerMesh(DeformableGeometry pMesh)
	{
		// void registerMesh(DeformableGeometry* pMesh);
		removeMesh(pMesh.getName());
		m_Storage.put(pMesh.getName(), pMesh);
	}
	
	public DeformableGeometry getMesh(final String name)
	{
		// DeformableGeometry* const getMesh(const std::string& name) const;
		return m_Storage.get(name);
	}
	
	public String getPrefix()
	{
		// std::string getPrefix() const;
/*!
    This is a bit of a hack which returns the most recently 'read'
	filename prefix. I.e. it returns "alice" or "kenneth" based on the
	fact that the loaded models start with "alice_BlahBlah.wrl"
	Function added by Almer S. Tigelaar, patched on 17.06.2007
 */
		String prefix = new String();

		// Determine the model name prefix.
		// We either split on the first _ or . encountered in the filename
		// Downside of placing this here
		if(m_Storage.size() > 1)
		{
			String name = new String(m_Storage.elements().nextElement().getName());
			//String name = (++(m_Storage.rbegin()))->second->getName(); // Always take the last loaded model file as reference
			
			int pos1 = name.indexOf('_');
			int pos2 = name.indexOf('.');

			if((pos2 != -1)  && ((pos1 == -1) || (pos2 < pos1))) //c++dan azicik farkli oldu kod bilerek(mecbur, npos olayi)
			{
				prefix = name.substring(0, pos2);
			}
			else if(pos1 != -1)
			{
				prefix = name.substring(0, pos1);
			}
		}
		System.err.println("prefix: "+prefix);

		return prefix;
	}

}
