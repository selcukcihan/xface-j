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
 * XEngine::TextureManager
 * tamamlandi.
 */

import java.util.Enumeration;
import java.util.Hashtable;

import javax.media.opengl.GL;

public class TextureManager
{
	private Hashtable<String, ITexture> m_Storage = new Hashtable<String, ITexture>();
	private static TextureManager m_pThisTexMan;
	private ITextureLoader m_pTexLoader;
	
	TextureManager()
	{
		m_pTexLoader = new TextureLoaderGL();
	}
	
	public ITexture load(final String filename, final String texname, GL p_gl)
	{
		if(m_Storage.containsKey(texname))
			return m_Storage.get(texname);
		
		ITexture pTexture = new Texture2D(texname);
		if(m_pTexLoader.load(filename, pTexture, p_gl))
			registerTexture(pTexture);
		else
			pTexture = null;
		
		return pTexture;
	}
	
	public void unLoad(final ITexture pTexture, GL p_gl)
	{
		m_pTexLoader.unLoad(pTexture, p_gl);
		unregisterTexture(pTexture, p_gl);
	}
	
	public void unLoad(final String name, GL p_gl)
	{
		unLoad(getTexture(name), p_gl);
	}
	
	public static void releaseInstance()
	{
		m_pThisTexMan = null;
	}
	
	public static TextureManager getInstance()
	{
		if(m_pThisTexMan == null)
			m_pThisTexMan = new TextureManager();
		return m_pThisTexMan;
	}
	
	public void destroyAll(GL p_gl)
	{
		for(Enumeration<String> e = m_Storage.keys(); e.hasMoreElements();)
		{
			String curkey = (String) e.nextElement();
			m_pTexLoader.unLoad(m_Storage.get(curkey), p_gl);
		}
	}
	
	public void registerTexture(final ITexture pTexture)
	{
		m_Storage.put(pTexture.getName(), pTexture);
	}
	
	public void unregisterTexture(final ITexture pTexture, GL p_gl)
	{
		for(Enumeration<String> e = m_Storage.keys(); e.hasMoreElements();)
		{
			String curkey = (String) e.nextElement();
			if(m_Storage.get(curkey) == pTexture)
				m_pTexLoader.unLoad(pTexture, p_gl);
		}
	}
	
	public ITexture getTexture(final String name)
	{
		return m_Storage.get(name);
	}
/*
	class TextureManager
	{
		typedef std::map<std::string, const ITexture*> TEXSTORAGE;
		TEXSTORAGE m_Storage;
		static TextureManager* m_pThisTexMan;
		ITextureLoader* m_pTexLoader;
		
		TextureManager(void);
		~TextureManager(void);
	public:
		const ITexture* load(const std::string& filename, const std::string& texname );
		void unLoad(const ITexture* pTexture);
		void unLoad(const std::string& name);

		static void releaseInstance();
		static TextureManager* getInstance();
		void destroyAll();
		void registerTexture(const ITexture* pTexture);
		void unregisterTexture(const ITexture* pTexture);
		const ITexture* getTexture(const std::string& name) const;
	};
 */
}
