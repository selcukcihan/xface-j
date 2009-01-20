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
 * XFace::HeadChannel
 * bitti.
 */

import java.util.LinkedList;

import xengine.Entity;
import xengine.MorphChannel;
import xengine.MorphController;
import xengine.Transform;
import xmath.Perlin;
import xmath.Quaternion;

public class HeadChannel extends MorphChannel
{
	static private float r1 = 0;
	static private float r2 = 0;
	static private float r3 = 0;
	
	private class Pointing
	{
		/*
		 * 	struct Pointing
			{
				float x, y, z, w;
				unsigned int start, end;
				Pointing() : x(0), y(0), z(0), w(1.0f), start(0), end(0) {}
				Pointing(unsigned int _start, unsigned int _end, float _x, float _y, float _z, float _w = 1.0f) 
					: x(_x), y(_y), z(_z), w(_w), start(_start), end(_end) {}
				unsigned int getDuration() const {return end - start;}
			};
		 */
		public float x;
		public float y;
		public float z;
		public float w;
		public int start, end;
		public Pointing()
		{
			x = 0;
			y = 0;
			z = 0;
			w = 1.0f;
			start = 0;
			end = 0;
		}
		
		public Pointing(int _start, int _end, float _x, float _y, float _z, float _w)
		{
			x = _x;
			y = _y;
			z = _z;
			w = _w;
			start = _start;
			end = _end;
		}
		
		public int getDuration()
		{
			return end - start;
		}
	}
	
	private Perlin m_pPerlinX;
	private Perlin m_pPerlinY;
	private Perlin m_pPerlinZ;
	
	LinkedList<int []> m_stills; /* std::list<std::pair<unsigned int, unsigned int> > m_stills; */
	LinkedList<Pointing> m_pointing; /* typedef std::list<Pointing> PointingList; */
	
	public HeadChannel(final String name)
	{
		/*
		 * HeadChannel(const std::string& name);
		 */
		m_pPerlinX = new Perlin(7, 8, 0.5f, 42);
		m_pPerlinY = new Perlin(7, 8, 0.5f, 43);
		m_pPerlinZ = new Perlin(7, 8, 0.5f, 44);
		
		m_stills = new LinkedList<int[]>();
		m_pointing = new LinkedList<Pointing>();
	}
	
	protected boolean updateResult(Entity result, final Entity rest)
	{
		/*
		 * virtual bool updateResult(XEngine::Entity& result, const XEngine::Entity& rest);
		 */
		return true;
	}
	
	protected boolean updateSelf(int elapsed_time)
	{
		/*
		 * virtual bool updateSelf(unsigned int elapsed_time);
		 */
		super.updateSelf(elapsed_time); // update base class for total time update, will return immediately afterwards
		if(!isStayStill() && !updatePointing(elapsed_time))
			updateIdleHeadMovements((float)elapsed_time);

		return true;
	}
	
	protected void updateIdleHeadMovements(float inc)
	{
		/*
		 * void updateIdleHeadMovements(float inc);
		 */
		/* static float r1 = 0, r2 = 0, r3 = 0; */
		float rx, ry, rz;
		rx = m_pPerlinX.Get1D(r1);
		ry = m_pPerlinY.Get1D(r2);
		rz = m_pPerlinZ.Get1D(r3);

		// below code makes head movements fps dependent, removed on 12.10.2007
		inc /= 3000.0f;
		//inc = 0.0075;
		r1 += inc;
		r2 += inc;
		r3 += inc;

		Transform tr = MorphController.getInstance().getTransform();
		tr.rotate(new Quaternion (rx, ry, rz));
		tr.updateLocalTransform();
		MorphController.getInstance().setTransform(tr);
	}

	protected boolean isStayStill()
	{ 
		/*
		 * bool isStayStill();
		 */
		// could be optimized, no need to traverse all the elements, it's fast anyhow..
		for(int [] still : m_stills)
		{
			if((still[0] < m_totalTimePassed) && (still[1] > m_totalTimePassed))
				return true;
		}
		return false;
	}
	
	protected boolean updatePointing(int elapsed_time)
	{
		/*
		 * bool updatePointing(unsigned int elapsed_time);
		 */
		// update pointing
		for(Pointing it : m_pointing)
		{
			if(it.start < m_totalTimePassed && it.end > m_totalTimePassed)
			{
				// 1/4 of the time to rotate to the direction
				// 2/4 of the time to stay there
				// 1/4 of the time to rotate back to original position
				float dur4 = it.getDuration() / 4.0f;
				
				// no rotation when in between durations below
				float inc = 0.0f;
				
				// rotating to target
				if(it.start + dur4 > m_totalTimePassed)
					inc = (float)elapsed_time / dur4;
				// going back to original position
				else if (it.end - dur4 < m_totalTimePassed)
					inc = -(float)elapsed_time / dur4;
				else	// stay there!
					return true;
				
				// consider also weight if any
				inc *= it.w;

				Transform tr = MorphController.getInstance().getTransform();
				tr.rotate(new Quaternion(inc*it.x, inc*it.y, inc*it.z));
				tr.updateLocalTransform();
				MorphController.getInstance().setTransform(tr);
				
				return true;
			}
		}

		// if no matching pointing found for the current time..
		return false;
	}
	
	public void clear()
	{
		/*
		 * void clear(); // inherited from parent
		 */
		m_stills.clear();
		m_pointing.clear();
		super.clear();
	}
	
	public void addPointing(int start, int end, float x, float y, float z, float w)
	{
		/*
		 * void addPointing(unsigned int start, unsigned int end, float x, float y, float z, float w = 1.0f);
		 */
		m_pointing.add(new Pointing(start, end, x, y, z, w));
	}
	
	public void addStayStill(int start, int end)
	{
		/*
		 * void addStayStill(unsigned int start, unsigned int end);
		 */
		/* m_stills.push_back(std::make_pair(start, end)); */
		m_stills.add(new int [] {start, end});
	}
}
