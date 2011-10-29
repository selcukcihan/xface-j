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
 * XFace::PHOLoader
 * bitti.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class PHOLoader
{
/*
class PHOLoader
{
	float m_total_duration;
public:
	//! returns total duration of phoneme file (in seconds)
	unsigned int getTotalDuration() const {return (unsigned int)m_total_duration*1000;}
	std::list<std::pair<std::string, unsigned int> > load(const std::string& filename);
	std::list<std::pair<std::string, unsigned int> > load(std::istream& input);
};
 */
	public class PHOPair
	{
		final public String m_alias;
		final public int m_duration;
		
		public PHOPair(final String alias, final int duration)
		{
			m_alias = alias;
			m_duration = duration;
		}
	}
	
	private float m_total_duration;
	
	public int getTotalDuration()
	{
		return (int)(m_total_duration * 1000);
	}
	
	/*
	 * std::list<std::pair<std::string, unsigned int> > load(std::istream& input);
	 */
	
	public LinkedList<PHOPair> load(Scanner fp)
	{
		LinkedList<PHOPair> phoList = new LinkedList<PHOPair>();
		String alias;
		float duration, prev_duration = 0;
		m_total_duration = 0;

		while(fp.hasNextLine()) // input have a last empty line, but it is parsed as 0 duration silence anyways, so no problem
		{
			if(fp.hasNext())
				alias = fp.next();
			else
				break;
			
			if(fp.hasNextFloat())
				m_total_duration = fp.nextFloat();
			else
				break;

			duration = m_total_duration - prev_duration;
			if(duration > 0.000001f)
			{
				phoList.add(new PHOPair(alias, (int)(duration*1000.0f))); // duration is in ms
			}
			prev_duration = m_total_duration;
		}
		return phoList;		
	}
	
	public LinkedList<PHOPair> load(final String filename)
	{
		Scanner fp = null;
		try
		{
			fp = new Scanner(new BufferedReader(new FileReader(filename)));
			fp.useLocale(Locale.US);
			return load(fp);
		}
		catch(FileNotFoundException fnfe)
		{
			return null;
		}
	}
}
