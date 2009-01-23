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

package com.selcukcihan.xfacej.xmath;

/*
 * XMath::Perlin
 * tamamlandi.
 */

import java.util.Random;

public final class Perlin
{
	public static final int SAMPLE_SIZE = 1024;
	public static final int B = SAMPLE_SIZE;
	public static final int BM = (SAMPLE_SIZE-1);
	public static final int N = 0x1000;
	public static final int NP = 12;
	public static final int NM = 0x0FFF;
	
	
	private int mOctaves = 0;
	private float mFrequency = 0;
	private float mAmplitude = 0;
	private int mSeed = 0;
	
	private int [] p = new int[SAMPLE_SIZE+SAMPLE_SIZE+2];
	private float [][] g3 = new float[SAMPLE_SIZE + SAMPLE_SIZE + 2][3];
	private float [][] g2 = new float[SAMPLE_SIZE + SAMPLE_SIZE + 2][2];
	private float [] g1 = new float[SAMPLE_SIZE + SAMPLE_SIZE + 2];
	private boolean mStart = false;
	
	private Random m_randGenerator = null;
	
	private float s_curve(float t)
	{
		/*
		 * macro. only used with float
		 */		
		return ( t * t * (3.0f - 2.0f * t) );
	}
	
	private float lerp(float t, float a, float b)
	{
		/*
		 * macro. only used with all params float
		 */
		return ( a + t * (b - a) );
	}
	
	private float setup(int i, int [] b, float [] r, float [] vec)
	{
		/*
		 * macro. b is {b0,b1} and r is {r0,r1}
		 * this must be called as "t = setup(...)"
		 */
		float t = vec[i] + N;
		b[0] = ((int)t) & BM;
		b[1] = (b[0]+1) & BM;
		r[0] = t - (int)t;
		r[1] = r[0] - 1.0f;
		return t;
	}
	
	private float at2(float rx, float ry, float [] q)
	{
		/* 
		 * #define at2(rx,ry) ( rx * q[0] + ry * q[1] )
		 * macro. rx and ry are floats. q is a reference to a float array.
		 * called only within noise2().
		 */
		return ( rx * q[0] + ry * q[1] );
	}
	
	private float at3(float rx, float ry, float rz, float [] q)
	{
		/*
		 * #define at3(rx,ry,rz) ( rx * q[0] + ry * q[1] + rz * q[2] )
		 * macro. rx, ry and rz are floats. q is a reference to a float array.
		 * called only within noise3().
		 */
		return ( rx * q[0] + ry * q[1] + rz * q[2] );
	}
	
	public Perlin(int octaves, float freq, float amp, int seed)
	{
		mOctaves = octaves;
		mFrequency = freq;
		mAmplitude = amp;
		mSeed = seed;
		mStart = true;		
	}
	
	public float Get1D(float x)
	{
		return perlin_noise_1D(x);
	}
	
	public float Get2D(float x, float y)
	{
		float [] vec = {x,y};
		
		return perlin_noise_2D(vec);		
	}
	
	public float Get3D(float x, float y, float z)
	{
	    float [] vec = {x,y,z};

	    return perlin_noise_3D(vec);		
	}

	public float Get(float x, float y)
	{
	    float [] vec = {x,y};

	    return perlin_noise_2D(vec);		
	}
	
	private void init_perlin(int n, float p)
	{
		// c++da bu metod implement edilmemis sadece declared
	}
	
	private float perlin_noise_1D(float vec)
	{
		int terms    = mOctaves;
		float freq   = mFrequency;
		float result = 0.0f;
		float amp = mAmplitude;

		vec*=mFrequency;
		for( int i=0; i<terms; i++ ) {
			result += noise1(vec)*amp;
			vec*=2.0f;
			amp*=0.5f;
	  }

	  return result;		
	}
	
	private float perlin_noise_2D(float [] vec)
	{
		int terms    = mOctaves;
		float freq   = mFrequency;
		float result = 0.0f;
		float amp = mAmplitude;

		vec[0]*=mFrequency;
		vec[1]*=mFrequency;

		for( int i=0; i<terms; i++ )
		{
			result += noise2(vec)*amp;
			vec[0] *= 2.0f;
			vec[1] *= 2.0f;
		    amp*=0.5f;
		}

		return result;		
	}
	
	private float perlin_noise_3D(float [] vec)
	{
		int terms    = mOctaves;
		float freq   = mFrequency;
		float result = 0.0f;
		float amp = mAmplitude;
		
		vec[0]*=mFrequency;
		vec[1]*=mFrequency;
		vec[2]*=mFrequency;
		
		for( int i=0; i<terms; i++ ) {
			result += noise3(vec)*amp;
		    vec[0] *= 2.0f;
		    vec[1] *= 2.0f;
		    vec[2] *= 2.0f;
		    amp*=0.5f;
		}
		return result;		
	}

	private float noise1(float arg)
	{
		int [] bx = {0,0};
		float [] rx = {0,0};
		float sx, t, u, v;
		float [] vec = {0};

		vec[0] = arg;

		if (mStart)
		{
			m_randGenerator = new Random(mSeed);
			mStart = false;
			init();
		}

		t = setup(0, bx, rx, vec);

		sx = s_curve(rx[0]);

		u = rx[0] * g1[ p[ bx[0] ] ];
		v = rx[1] * g1[ p[ bx[1] ] ];

		return lerp(sx, u, v);		
	}
	
	private float noise2(float [] vec)
	{
		int [] bx = {0,0};
		int [] by = {0,0};
		int b00, b10, b01, b11;
		float [] rx = {0,0};
		float [] ry = {0,0};
		float sx, sy, a, b, t, u, v;
		float [] q = null;
		int i, j;

		if (mStart)
		{
			m_randGenerator = new Random(mSeed);
			mStart = false;
			init();
		}

		t = setup(0,bx,rx,vec);
		t = setup(1,by,ry,vec);

		i = p[bx[0]];
		j = p[bx[1]];

		b00 = p[i + by[0]];
		b10 = p[j + by[0]];
		b01 = p[i + by[1]];
		b11 = p[j + by[1]];

		sx = s_curve(rx[0]);
		sy = s_curve(ry[0]);

		q = g2[b00];
		u = at2(rx[0],ry[0],q);
		q = g2[b10];
		v = at2(rx[1],ry[0],q);
		a = lerp(sx, u, v);

		q = g2[b01];
		u = at2(rx[0],ry[1],q);
		q = g2[b11];
		v = at2(rx[1],ry[1],q);
		b = lerp(sx, u, v);

		return lerp(sy, a, b);		
	}
	
	private float noise3(float [] vec)
	{
		int [] bx = {0,0};
		int [] by = {0,0};
		int [] bz = {0,0};
		int b00, b10, b01, b11;
		float [] rx = {0,0};
		float [] ry = {0,0};
		float [] rz = {0,0};
		float [] q = null;
		float sy, sz, a, b, c, d, t, u, v;
		int i, j;

		if (mStart)
		{
			m_randGenerator = new Random(mSeed);
			mStart = false;
			init();
		}

		t = setup(0, bx, rx, vec);
		t = setup(1, by, ry, vec);
		t = setup(2, bz, rz, vec);

		i = p[ bx[0] ];
		j = p[ bx[1] ];

		b00 = p[ i + by[0] ];
		b10 = p[ j + by[0] ];
		b01 = p[ i + by[1] ];
		b11 = p[ j + by[1] ];

		t  = s_curve(rx[0]);
		sy = s_curve(ry[0]);
		sz = s_curve(rz[0]);

	  

		q = g3[ b00 + bz[0] ] ; u = at3(rx[0],ry[0],rz[0],q);
		q = g3[ b10 + bz[0] ] ; v = at3(rx[1],ry[0],rz[0],q);
		a = lerp(t, u, v);

		q = g3[ b01 + bz[0] ] ; u = at3(rx[0],ry[1],rz[0],q);
		q = g3[ b11 + bz[0] ] ; v = at3(rx[1],ry[1],rz[0],q);
		b = lerp(t, u, v);

		c = lerp(sy, a, b);

		q = g3[ b00 + bz[1] ] ; u = at3(rx[0],ry[0],rz[1],q);
		q = g3[ b10 + bz[1] ] ; v = at3(rx[1],ry[0],rz[1],q);
		a = lerp(t, u, v);

		q = g3[ b01 + bz[1] ] ; u = at3(rx[0],ry[1],rz[1],q);
		q = g3[ b11 + bz[1] ] ; v = at3(rx[1],ry[1],rz[1],q);
		b = lerp(t, u, v);

		d = lerp(sy, a, b);

		return lerp(sz, c, d);		
	}
	
	private void normalize2(float [] v)
	{
		float s;

		s = (float)Math.sqrt(v[0] * v[0] + v[1] * v[1]);
		s = 1.0f/s;
		v[0] = v[0] * s;
		v[1] = v[1] * s;		
	}
	
	private void normalize3(float [] v)
	{
		float s;

		s = (float)Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
		s = 1.0f/s;

		v[0] = v[0] * s;
		v[1] = v[1] * s;
		v[2] = v[2] * s;		
	}
	
	private void init()
	{
		/*
		 * pre: m_randGenerator != null
		 */
		int i, j, k;

		for (i = 0 ; i < B ; i++)
		{
			p[i] = i;
			g1[i] = (float)((m_randGenerator.nextInt(B + B)) - B) / B;
			for (j = 0 ; j < 2 ; j++)
				g2[i][j] = (float)((m_randGenerator.nextInt(B + B)) - B) / B;
			normalize2(g2[i]);
			for (j = 0 ; j < 3 ; j++)
				g3[i][j] = (float)((m_randGenerator.nextInt(B + B)) - B) / B;
			normalize3(g3[i]);
		}

		while ((--i)>0)
		{
			k = p[i];
			p[i] = p[j = m_randGenerator.nextInt(B)];
			p[j] = k;
		}

		for (i = 0 ; i < B + 2 ; i++)
		{
			p[B + i] = p[i];
			g1[B + i] = g1[i];
			for (j = 0 ; j < 2 ; j++)
				g2[B + i][j] = g2[i][j];
			for (j = 0 ; j < 3 ; j++)
				g3[B + i][j] = g3[i][j];
		}		
	}
}
