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

package com.selcukcihan.android.xface.xengine;

/*
 * XEngine::BMPFile
 * bitti.
 */

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;


public class BMPFile extends ITextureFile
{
	public boolean load(final String filename)
	{
		// bool load(const std::string& filename);
		/*
		try
		{
			File file = new File(filename);
			BufferedImage bufferedImage = ImageIO.read(file);
			m_Width = bufferedImage.getWidth();
			m_Height = bufferedImage.getHeight();
			
			int [] rgbArray = new int[m_Width*m_Height];
			//bufferedImage.getRGB(0, 0, m_Width, m_Height, rgbArray, 0, m_Width);
			bufferedImage.getRGB(0, 0, m_Width, m_Height, rgbArray, 0, m_Width);
			
			m_pData = IntBuffer.wrap(rgbArray);
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			return false;
		}
		*/
		TextureReader.Texture texture = null;
        try
        {
            texture = TextureReader.readTexture(filename);
            m_pData = texture.getPixels();
            m_Width = texture.getWidth();
            m_Height = texture.getHeight();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
		return true;
	}

}
