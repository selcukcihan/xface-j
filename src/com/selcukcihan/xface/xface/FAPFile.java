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
 * XFace::FAPFile
 * bitti.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

public class FAPFile extends IFapStream
{
	private VisemeStruct [] m_visemes;
	private ExpressionStruct [] m_expressions;
	private boolean m_bLoaded;
	private float [][] m_FAPs;
	private int m_currentFAPID;
	
	public FAPFile()
	{
		/*
		 * FAPFile(void);
		 */
		super();
		m_bLoaded = false;
		m_currentFAPID = 0;
		m_FAPs = null;
		m_expressions = null;
		m_visemes = null;
	}
	
	private void printFAPs(final String filename)
	{
		/*
		 * void printFAPs(const std::string& filename);
		 */
		PrintWriter fp;
		try
		{
			fp = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			for(float [] vOuter : m_FAPs)
			{
				for(float vInner : vOuter)
					fp.print("" + vInner + " ");
				fp.println();
			}
			fp.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void scaleFAPs(final FAPU fapu)
	{
		/*
		 * void scaleFAPs(const FAPU& fapu);
		 */
		for(float [] fap : m_FAPs)
			scaleFAPVect(fap, fapu);
	}
	
	private static void scaleFAPVect(float [] FAPs, final FAPU fapu)
	{
		/*
		 * static void scaleFAPVect(std::vector<float>& faps, const FAPU& fapu);
		 */
		for(int i = 0; i < 68; ++i)
		{
			if( ( (i > 1) && (i < 5) ) || 
				( (i > 6) && (i < 14)) ||
				( (i > 14)&& (i < 19)) ||
				(i == 43) || (i == 45) || (i == 50) || (i == 51) ||
				( (i > 53) && (i < 60)) )
			{
				FAPs[i] *= fapu.MNS;
			}
			else if ( (i == 5) || (i == 6) || (i == 14) || (i == 42) || 
				(i == 44) || (i == 52) || (i == 53) )
				FAPs[i] *= fapu.MW;
			else if ( ( (i > 17) && (i < 22) ) || (i == 28) || (i == 29))
				FAPs[i] *= fapu.IRISD;
			else if ( ( (i > 21) && (i < 26) ) || 
						( (i > 45) && (i < 50) ) )
				FAPs[i] *= fapu.AU;
			else if ( ( (i > 25) && (i < 28) ) || 
						( (i > 35) && (i < 40) ) )
				FAPs[i] *= fapu.ES;
			else if( ( (i > 29) && (i < 36) ) || 
						( (i > 39) && (i < 42) ) || (i > 59) )
				FAPs[i] *= fapu.ENS;
		}
	}
	
	private static void unscaleFAPVect(float [] FAPs, final FAPU fapu)
	{
		/*
		 * static void unscaleFAPVect(std::vector<float>& faps, const FAPU& fapu);
		 */
		for(int i = 0; i < 68; ++i)
		{
			if( ( (i > 1) && (i < 5) ) || 
				( (i > 6) && (i < 14)) ||
				( (i > 14)&& (i < 19)) ||
				(i == 43) || (i == 45) || (i == 50) || (i == 51) ||
				( (i > 53) && (i < 60)) )
			{
				FAPs[i] /= fapu.MNS;
			}
			else if ( (i == 5) || (i == 6) || (i == 14) || (i == 42) || 
				(i == 44) || (i == 52) || (i == 53) )
				FAPs[i] /= fapu.MW;
			else if ( ( (i > 17) && (i < 22) ) || (i == 28) || (i == 29))
				FAPs[i] /= fapu.IRISD;
			else if ( ( (i > 21) && (i < 26) ) || 
						( (i > 45) && (i < 50) ) )
				FAPs[i] /= fapu.AU;
			else if ( ( (i > 25) && (i < 28) ) || 
						( (i > 35) && (i < 40) ) )
				FAPs[i] /= fapu.ES;
			else if( ( (i > 29) && (i < 36) ) || 
						( (i > 39) && (i < 42) ) || (i > 59) )
				FAPs[i] /= fapu.ENS;
		}
	}
	
	private void adjustFAPs()
	{
		/*
		 * void adjustFAPs();
		 */
		for(int i = 0; i < m_FAPs.length; i++)
		{
			if ( m_FAPs[i][51]!=0 && m_FAPs[i][4]==0)		//	8.2 vert
				m_FAPs[i][4]=m_FAPs[i][51];
			if ( m_FAPs[i][4]!=0 && m_FAPs[i][51]==0 )	//	2.3 vert
				m_FAPs[i][51]=m_FAPs[i][4];
			if ( m_FAPs[i][57]!=0 && m_FAPs[i][10]==0)	//	8.8 vert
				m_FAPs[i][10]=m_FAPs[i][57];
			if ( m_FAPs[i][10]!=0 && m_FAPs[i][57]==0)	//	2.9 vert
				m_FAPs[i][57]=m_FAPs[i][10];
			if ( m_FAPs[i][56]!=0 && m_FAPs[i][9]==0)	//	8.7 vert
				m_FAPs[i][9]=m_FAPs[i][56];
			if ( m_FAPs[i][9]!=0 && m_FAPs[i][56]==0)	//	2.8 vert
				m_FAPs[i][56]=m_FAPs[i][9];
		
			if ( m_FAPs[i][50]!=0 && m_FAPs[i][3]==0)	//	8.1 vert
				m_FAPs[i][3]=m_FAPs[i][50];
			if ( m_FAPs[i][3]!=0 && m_FAPs[i][50]==0 )	//	2.2 vert
				m_FAPs[i][50]=m_FAPs[i][3];
			if ( m_FAPs[i][55]!=0 && m_FAPs[i][8]==0)	//	8.6 vert
				m_FAPs[i][8]=m_FAPs[i][55];
			if ( m_FAPs[i][8]!=0 && m_FAPs[i][55]==0)	//	2.7 vert
				m_FAPs[i][55]=m_FAPs[i][8];
			if ( m_FAPs[i][54]!=0 && m_FAPs[i][7]==0)	//	8.5 vert
				m_FAPs[i][7]=m_FAPs[i][54];
			if ( m_FAPs[i][7]!=0 && m_FAPs[i][54]==0)	//	2.6 vert
				m_FAPs[i][54]=m_FAPs[i][7];
				
			if ( m_FAPs[i][5]!=0 && m_FAPs[i][52]==0)			//	2.4 ORIZ. 8.3 ORIZ. =>angoli della bocca
				m_FAPs[i][52]=m_FAPs[i][5];
			if ( m_FAPs[i][52]!=0 && m_FAPs[i][5]==0)
				m_FAPs[i][5]=m_FAPs[i][52];

			if ( m_FAPs[i][6]!=0 && m_FAPs[i][53]==0)			//	2.5 ORIZ. 8.4 ORIZ. =>angoli della bocca
				m_FAPs[i][53]=m_FAPs[i][6];
			if ( m_FAPs[i][53]!=0 && m_FAPs[i][6]==0)
				m_FAPs[i][6]=m_FAPs[i][53];

			if ( m_FAPs[i][11]!=0 && m_FAPs[i][58]==0)			//	2.4 VERT. 8.3 VERT. =>angoli della bocca
				m_FAPs[i][58]=m_FAPs[i][11];
			//if ( m_FAPs[i][58]!=0 && m_FAPs[i][11]==0)
				//{ m_FAPs[i][11]=m_FAPs[i][58]; }							

			if ( m_FAPs[i][12]!=0 && m_FAPs[i][59]==0)			//	2.5 VERT. 8.4 VERT. =>angoli della bocca
				m_FAPs[i][59]=m_FAPs[i][12];
			//if ( m_FAPs[i][59]!=0 && m_FAPs[i][12]==0)
			//	{ m_FAPs[i][12]=m_FAPs[i][59]; }							
			
			//Le seguenti linee permettono di controllare che l'intrusione delle labbra non sia eccessiva
			//arrivando a far vedere i denti.Da notare che come riferimento utilizzo la coordinata z 
			//degli fdp 2.2 e 2.3 parametrizzate rispetto all'unità MNS.Il fattore 1/4 di cui viene scalato 
			//il valore originario è empirico.
			//NB: nessun limite per ora (6/6) sulla protusione.
		//	if ( (m_FAPs[i][16]*MNS) < (fdp[2][2][2]*MNS) ) { m_FAPs[i][16]=m_FAPs[i][16]/4; }
		//	if ( (m_FAPs[i][15]*MNS) < (fdp[2][3][2]*MNS) ) { m_FAPs[i][15]=m_FAPs[i][15]/4; }

		}
	}

	public boolean open(Scanner input, FAPU fapu)
	{
		/*
		 * bool open(std::istream& input, const FAPU& fapu);
		 */
		m_FAPs = null;
		m_bLoaded = false;

		boolean init = true;
		boolean isMask = true;
		int [] mask = new int[68];
		int row = 0;
		input.useLocale(Locale.US);
		while(input.hasNext())
		{
			input.skip("(#.*$)*"); /* skip comment line */
			if(!input.hasNext())
				break;
			if(init) // header info
			{
				int nFAPs;
				m_version = input.nextFloat();
				input.next(); /* stupidname */
				float fps = 0;
				fps = input.nextFloat();
				m_FPS = (short)fps;
				nFAPs = input.nextInt();

				m_FAPs = new float [nFAPs][];
				m_visemes = new VisemeStruct [nFAPs];
				m_expressions = new ExpressionStruct [nFAPs];
				for(int i = 0; i < nFAPs; i++)
				{
					float [] fap_set = new float [68];
					for(int j = 0; j < 68; j++)
						fap_set[j] = 0;

					m_FAPs[i] = fap_set;
					m_visemes[i] = new VisemeStruct();
					m_expressions[i] = new ExpressionStruct();
				}
				init = false;
			}
			else if(isMask) // mask
			{
				for(int i = 0; i < 68; i++)
					mask[i] = input.nextInt();
				isMask = false;	
			}
			else // fap
			{
				row = input.nextInt(); // first item is the index of the current FAP (ensures synchronization)

				float [] fap_set = m_FAPs[row];
				// then the values
				for(int i = 0; i < 68; i++)
				{
					if(mask[i] != 0)
					{
						if(i == 0) //viseme to decode
						{
							// Reading a Viseme
							// ok we have a collection of four values to read ....
							// (three of which are actually used)

							int f1 = input.nextInt();
							int f2 = input.nextInt();
							int f3 = input.nextInt();
							input.next(); /* 4th value is not used */
							m_visemes[row] = new VisemeStruct(f1, f2, f3);
							fap_set[i] = 1f; // boolean to say there exists a viseme
	 					}
						else if (i == 1) // expression
						{
							int [] f = new int[6];
							for(int j = 0; j < 6; j++)
							{
								f[j] = input.nextInt();
							}
							m_expressions[row] = new ExpressionStruct(f[0], f[1], f[2], f[3], f[4], f[5]);
							fap_set[i] = 1f; // boolean to say there exists a expression
						}
						else // low level FAPs
						{
							// Simple variant for optimal performance, assumes that the input FAPs
							// are properly encoded by definition!
							fap_set[i] = input.nextFloat();
						}
					}
				}
				isMask = true;
			}
		}

		scaleFAPs(fapu);
		adjustFAPs();
		
		rewind();
		m_bLoaded = true;
		
		return true;
	}

	public boolean isEnd()
	{
		/*
		 * bool isEnd() const {return m_currentFAPID == m_FAPs.size() - 1;}
		 */
		return m_currentFAPID == m_FAPs.length - 1;
	}
	
	public boolean isOpen()
	{
		/*
		 * bool isOpen() const {return m_bLoaded;}
		 */
		return m_bLoaded;
	}
	
	public void next()
	{
		/*
		 * void next();
		 */
		// clear if we are at the end (will cause crash somewhere probably, if not handled correctly)
		if(m_currentFAPID >= (m_FAPs.length - 1))
			m_currentFAP = new float[0];
		else // get the next one
		{
			m_currentFAP = m_FAPs[++m_currentFAPID];
			m_currentViseme = m_visemes[m_currentFAPID];
			m_currentExpression = m_expressions[m_currentFAPID];
		}
	}
	
	public void rewind()
	{
		/*
		 * void rewind();
		 */
		m_currentFAPID = 0;
		m_currentFAP = m_FAPs[0];
		m_currentViseme = m_visemes[0];
		m_currentExpression = m_expressions[0];
	}
	
	public int getFAPCount()
	{
		/*
		 * virtual size_t getFAPCount() const {return m_FAPs.size();}
		 */
		return m_FAPs.length;
	}
	
	public long getCurrentFAPId()
	{
		/*
		 * virtual unsigned long getCurrentFAPId() const{return m_currentFAPID;}
		 */
		return m_currentFAPID;
	}

	public static boolean save(IFapStream faps, final FAPU fapu, final String filename)
	{
		/*
		 * static bool save(IFapStream& faps, const FAPU& fapu, const std::string& filename);
		 */
		try
		{
			PrintWriter fap_file = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			
			fap_file.print(faps.getVersion());
			fap_file.print(" Xface::FapFile ");
			fap_file.print(faps.getFPS());
			fap_file.print(" ");
			fap_file.println(faps.getFAPCount());
			
			faps.rewind();
			float [] fap_set = faps.getCurrentFAP();
			int lineId = 0;
			while(fap_set.length != 0)
			{
				unscaleFAPVect(fap_set, fapu);
				for(int i = 0; i < 68; i++)
				{
					if(fap_set[i] != 0)
						fap_file.print("1 ");
					else
						fap_file.print("0 ");
				}
				fap_file.println();
	
				fap_file.print(lineId++);
				fap_file.print(" ");
				for(int i = 0; i < 68; i++)
				{
					if(fap_set[i] != 0)
						fap_file.print("" + fap_set[i] + " ");
				}
				fap_file.println();
	
				faps.next();
				fap_set = faps.getCurrentFAP();
			}
	
			fap_file.close();
			return true;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
