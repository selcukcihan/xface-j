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
 * XFace::AnimProcessor
 * bitti.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

import com.selcukcihan.android.xface.xengine.Entity;
import com.selcukcihan.android.xface.xengine.MorphChannel;
import com.selcukcihan.android.xface.xengine.MorphController;
import com.selcukcihan.android.xface.xengine.MorphTarget;
import com.selcukcihan.android.xface.xface.PHOLoader.PHOPair;

public class AnimProcessor
{
	private int m_speechDuration;
	private PhonemeDictionary m_phonDictionaries;
	private HeadChannel m_headChannel;
	
	public AnimProcessor()
	{
		/*
		 * AnimProcessor();
		 */
		m_speechDuration = 0;
		m_headChannel = new HeadChannel("Head");
		m_phonDictionaries = new PhonemeDictionary();
	}
	
	private HashMap<String, String> getPhonDictionary(final String lang)
	{
		/*
		 * const PhonemeDictionary::DictionaryContent& getPhonDictionary(const std::string& lang) const {return m_phonDictionaries.getDictionary(lang);}
		 */
		return m_phonDictionaries.getDictionary(lang);
	}
	
	
	private void pushPhoneme(final String alias, int start, int end, float weight)
	{
		/*
		 * void pushPhoneme(const std::string& alias, unsigned int start, unsigned int end, float weight) const;
		 */
		if(alias.length() == 0 || end - start <= 0)
			return;
		MorphController cont = MorphController.getInstance();
		Entity toInsert = cont.getDictionaryItem(alias);
		if(toInsert.getDrawableCount() == 0)
		{
			System.err.println("cannot find viseme: " + alias);
			return;
		}

		MorphTarget pTrg = new MorphTarget(toInsert, alias, start, end, weight);
		cont.pushTarget(pTrg, "Viseme");
	}
	
	private void prepareEyeBlink(int period, int duration)
	{
		/*
		 * void prepareEyeBlink(unsigned int period, unsigned int duration);
		 */
		MorphController cont = MorphController.getInstance();
		LinkedList<MorphTarget> eyes = new LinkedList<MorphTarget>();
		
		Entity toInsert = new Entity();
		Entity rest = new Entity();
		toInsert.copyFrom(cont.getDictionaryItem("BlinkEyes"), true);
		rest.copyFrom(cont.getDictionaryItem("Rest"), true);
		int counter = 1500;
		if(toInsert.getDrawableCount() != 0)
		{
			while (duration > counter)
			{
				eyes.add(new MorphTarget(rest, "Rest", counter, counter + 50, 1.0f));
				counter += 50;
				eyes.add(new MorphTarget(toInsert, "BlinkEyes", counter, counter + 150, 1.0f));
				counter += 150;
				eyes.add(new MorphTarget(rest, "Rest", counter, counter + 50, 1.0f));
				counter += 50;
				counter += period;
			}
			cont.addChannel((MorphChannel)(new EyeChannel("BlinkEyes")));
			cont.pushTargets(eyes, "BlinkEyes");
		}
	}
	
	private int processEyeChannel(Scanner input)
	{
		/*
		 * int processEyeChannel(std::istream& input) const;
		 */
		MorphController controller = MorphController.getInstance();
		int begin = 0, end = 0;
		float intensity = 1.0f;
		String action = "";
		String exp = "";
		
		action = input.next();
		begin = input.nextInt();
		end = input.nextInt();
		intensity = input.nextFloat();
		exp = input.next();

		if(intensity > 1.0f)
		{
			System.err.println("Warning: Intensity for expression " + exp + " is out of acceptable range, clamped.");
			intensity = 1.0f;
		}
		Entity toInsert = new Entity();
		toInsert.copyFrom(controller.getDictionaryItem(exp), true);
		Entity restTest = new Entity();
		restTest.copyFrom(controller.getDictionaryItem("Rest"), true);
		if(toInsert.getDrawableCount() != 0)
		{
			LinkedList<MorphTarget> eyes = new LinkedList<MorphTarget>();
			int delta = Math.min((end - begin) / 4 + 1, 400);
			
			eyes.add(new MorphTarget(restTest, "Rest", begin, begin + delta / 2, 1));
			eyes.add(new MorphTarget(toInsert, exp, begin + delta / 2, begin + delta, intensity));
			eyes.add(new MorphTarget(toInsert, exp, begin + delta, end - delta, intensity));
			eyes.add(new MorphTarget(toInsert, exp, end - delta, end - delta / 2, intensity));
			eyes.add(new MorphTarget(restTest, "Rest", end - delta / 2, end, 1));

			controller.addChannel((MorphChannel)(new EyeChannel("Eyes")));
			controller.pushTargets(eyes, "Eyes");
		}
		else
			System.err.println("Cannot find action keyframe " + exp +
					" for eye channel for action: " + action);

		return end;
	}
	
	private int processFaceChannel(Scanner input)
	{
		/*
		 * int processFaceChannel(std::istream& input) const;
		 */
		MorphController controller = MorphController.getInstance();
		int begin = 0, end = 0;
		String exp = "";
		float intensity = 1.0f;
		
		exp = input.next();
		begin = input.nextInt();
		end = input.nextInt();
		intensity = input.nextFloat();

		if((intensity > 1.0f) || (intensity < 0))
		{
			System.err.println("Warning: Intensity for expression " + exp + " is out of acceptable range, clamped.");
			intensity = 1.0f;
		}
		LinkedList<MorphTarget> expressions = new LinkedList<MorphTarget>();
		Entity toInsert = new Entity();
		toInsert.copyFrom(controller.getDictionaryItem(exp), true);
		if(toInsert.getDrawableCount() != 0)
		{
			int delta = Math.min((end - begin) / 4 + 1, 400);
			expressions.add(new MorphTarget(toInsert, exp, begin, begin + delta, intensity));
			expressions.add(new MorphTarget(toInsert, exp, begin + delta, end - delta, intensity));
			expressions.add(new MorphTarget(toInsert, exp, end - delta, end - delta / 2, intensity));
			Entity restTest = new Entity();
			restTest.copyFrom(controller.getDictionaryItem("Rest"), true);
			expressions.add(new MorphTarget(restTest, "Rest", end - delta / 2, end, 1));

			controller.addChannel((MorphChannel)(new ExpressionChannel("Expression")));
			controller.pushTargets(expressions, "Expression");
		}
		else
			System.err.println("Cannot find expression " + exp + " in dictionary! Ignoring...");
		
		return end;
	}
	
	private int processHeadChannel(Scanner input)
	{
		/*
		 * int processHeadChannel(std::istream& input) const;
		 */
		String type = "";
		float x, y, z, w;
		int begin, end;
		
		type = input.next();
		begin = input.nextInt();
		end = input.nextInt();
		w = input.nextFloat();
		x = input.nextFloat();
		y = input.nextFloat();
		z = input.nextFloat();

		if(type.equals("pointing"))
		{
			m_headChannel.addPointing(begin, end, x, y, z, w);
		}

		return end;
	}
	
	public void addPhonemeDictionary(final String dic)
	{
		/*
		 * void addPhonemeDictionary(const std::string& dic) {m_phonDictionaries.loadDictionary(dic);}
		 */
		m_phonDictionaries.loadDictionary(dic);
	}
	
	public void addPhonemeDictionary(final InputStream dic)
	{
		/*
		 * void addPhonemeDictionary(const std::string& dic) {m_phonDictionaries.loadDictionary(dic);}
		 */
		m_phonDictionaries.loadDictionary(dic);
	}
	
	public int getSpeechDuration()
	{
		/*
		 * unsigned int getSpeechDuration() const {return m_speechDuration;}
		 */
		return m_speechDuration;
	}
	
	public String getErrorString(boolean clear)
	{
		/*
		 * std::string getErrorString(bool clear = true);
		 */
		/*
		 * c++da implement edilmemis mi ne anlamadim, headerde prototipi var ama implementasyonunu goremedim?
		 */
		throw new UnsupportedOperationException();
	}
	
	public int processAnim(Scanner input)
	{
		/*
		 * int processAnim(std::istream& input);
		 */
		MorphController controller = MorphController.getInstance();
		controller.reset();
		String channel = "";
		int duration = 0;
		int blinkEyes = 0;
		m_speechDuration = 0;

		while(input.hasNext())
		{
			channel = input.next();
			if(channel.equals("Expression"))
			{
				duration = Math.max(processFaceChannel(input), duration);
			}
			else if(channel.equals("Eyes"))
			{
				duration = Math.max(processEyeChannel(input), duration);
			}
			else if(channel.equals("Viseme"))
			{
				controller.addChannel("Viseme");
				String filename;
				String lang;
				int end = 0;
				
				filename = input.next();
				end = input.nextInt();
				lang = input.next();
				
				Scanner inputPho = null;

				try
				{
					inputPho = new Scanner(new BufferedReader(new FileReader(filename)));
					inputPho.useLocale(Locale.US);
				}
				catch(FileNotFoundException fnfe)
				{
					fnfe.printStackTrace();
				}
				
				processPhonemes(inputPho, lang);
				inputPho.close();
				duration = Math.max(duration, end);
			}
			else if (channel.equals("Head"))
			{
				duration = Math.max(processHeadChannel(input), duration);
			}
			else if (channel.equals("BlinkEyes"))
			{
				blinkEyes = input.nextInt();
			}
			
			channel = "";
		}
		
		if(blinkEyes != 0)
			prepareEyeBlink(blinkEyes, duration);
		
		// idle and controlled head movements go here..
		controller.addChannel(m_headChannel);
		controller.rewind();

		return duration;
	}
	
	public int processPhonemes(Scanner input, final String lang)
	{
		/*
		 * int processPhonemes(std::istream& input, const std::string& lang);
		 */
		MorphController cont = MorphController.getInstance();
		// cleanup
		m_speechDuration = 0;
		MorphChannel ch = cont.findChannel("Viseme");
		if(ch == null)
			cont.addChannel("Viseme");
		cont.clearChannel("Viseme");
		
		if(!input.hasNext())
			return 0;
		
		// Do the processing of the content
		float smoothFactor = 0.6f;
		
		PHOLoader phonemeLoader = new PHOLoader();
		LinkedList<PHOPair> phoSeq = phonemeLoader.load(input);
		//std::list<std::pair<std::string, unsigned int> >::const_iterator it = phoSeq.begin();
		m_speechDuration = phonemeLoader.getTotalDuration();
		int currentDuration = 0;
		HashMap<String, String> phonDict = getPhonDictionary(lang);
		
		//while (it != phoSeq.end())
		for(PHOPair phoPair : phoSeq)
		{
			// if there is silence for a long period of time, we should prevent perlin head movement.
			// injecting kind of a hack here
			String it_phon = phonDict.get(phoPair.m_alias);
			// if it is a silence and lasts longer than 200 ms
			if(it_phon == null)
			{
				System.err.println("Could not find alias for phoneme: " + phoPair.m_alias);
				pushPhoneme("Rest", currentDuration, currentDuration + phoPair.m_duration, smoothFactor); 
			}
			else if(it_phon.equals("Rest") && (phoPair.m_duration > 200)) 
			{
				// split the rest frame into two so that interpolation is correctly done, introduce a 50 ms rest phoneme
				pushPhoneme(it_phon, currentDuration, currentDuration + phoPair.m_duration - 50, smoothFactor); 
				pushPhoneme(it_phon, currentDuration + phoPair.m_duration - 50, currentDuration + phoPair.m_duration, smoothFactor); 
				m_headChannel.addStayStill(currentDuration, currentDuration + phoPair.m_duration);
			}
			else
				pushPhoneme(it_phon, currentDuration, currentDuration + phoPair.m_duration, smoothFactor); 
			
			currentDuration += phoPair.m_duration;
		}
		
		return m_speechDuration;
	}
}
