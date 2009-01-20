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
 * XFace::PhonemeDictionary
 * bitti.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class PhonemeDictionary
{
/*
class PhonemeDictionary
{
public:
	typedef std::map<std::string, std::string> DictionaryContent;
private:
	typedef std::map<std::string, DictionaryContent> DictionaryMap;
	DictionaryMap m_dictionaries;
public:
	bool empty() const {return m_dictionaries.empty();}
	const DictionaryContent& getDictionary(const std::string& lang) const;
	std::string loadDictionary(const std::string& filename);
	std::string loadDictionary(std::istream& input);
};
 */
	private HashMap<String, HashMap<String, String>> m_dictionaries;
	
	public PhonemeDictionary()
	{
		m_dictionaries = new HashMap<String, HashMap<String,String>>();
	}
	
	public boolean empty()
	{
		return m_dictionaries.isEmpty();
	}
	
	public HashMap<String, String> getDictionary(final String lang)
	{
		/* static DictionaryContent invalidVal; */
		assert(!m_dictionaries.isEmpty());
		return m_dictionaries.get(lang);
	}
	
	public String loadDictionary(final String filename)
	{
		Scanner fp = null;

		try
		{
			fp = new Scanner(new BufferedReader(new FileReader(filename)));
			fp.useLocale(Locale.US);
		}
		catch(FileNotFoundException fnfe)
		{
			return new String("");
		}
		
		HashMap<String, String> dict = new HashMap<String, String>();
		String lang;
		String src;
		String trg;
		
		lang = fp.next();
		
		while(fp.hasNext())
		{
			src = fp.next();
			trg = fp.next();
			dict.put(src, trg);
		}
		m_dictionaries.put(lang, dict);
		
		return lang;
	}
}
