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
 * - Selcuk Cihan (selcukcihan@gmail.com)
 * ***** END LICENSE BLOCK ***** */

import xengine.ITimer;


public class XFaceTimer implements ITimer
{
	private long m_startTime;
	private long m_lastMark;
	
	public XFaceTimer()
	{
		startTimer();
	}

	public long getElapsedTime(boolean mark)
	{
		long now = System.currentTimeMillis();//System.nanoTime();
		long retVal = (now - m_lastMark);
		if(mark)
			m_lastMark = now;
		return retVal;
	}

	public long getTotalElapsedTime(boolean mark)
	{
		long now = System.currentTimeMillis();//System.nanoTime();
		if(mark)
			m_lastMark = now;
		return (now - m_startTime);
	}

	public long startTimer()
	{
		m_lastMark = m_startTime = System.currentTimeMillis();//System.nanoTime();
		return 0;
	}

	public void waitFor(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
