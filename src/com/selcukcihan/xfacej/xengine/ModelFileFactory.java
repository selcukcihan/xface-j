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

package com.selcukcihan.xfacej.xengine;

/*
 * XEngine::ModelFileFactory
 * saveAllFilesBinary metodu haric tamam. o da zaten simdilik kullanilmiyor, sanirim XFaceED'de kullanilmis
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

import javax.media.opengl.GL;

public class ModelFileFactory
{
	/* typedef std::list<MeshInfo> FILEMESHES; */
	private static LinkedList<MeshInfo> s_filenames = new LinkedList<MeshInfo>();
	private static BinaryModelBatchLoader s_pBinaryBatchLoader;

    //! returns the list of MeshInfo for the files already loaded.
	static MeshInfo getMeshInfo(final String meshName)
	{
		/*
		 * static const MeshInfo* getMeshInfo(const std::string& meshName);
		 */
		for(MeshInfo info : s_filenames)
		{
			for(Drawable dr : info.drawables)
			{
				if(dr.getMeshName().equals(meshName))
					return info;
			}
		}
		return null;
	}
	
	public static boolean initBinaryLoader(final String filename, final String path, GL gl)
	{
		/*
		 * static bool initBinaryLoader(const std::string& filename, const std::string& path);
		 */
		s_pBinaryBatchLoader = new BinaryModelBatchLoader();
		return s_pBinaryBatchLoader.init(filename, path, gl);
	}
	
	public static void releaseBinaryLoader()
	{
		/*
		 * static void releaseBinaryLoader();
		 */
		s_pBinaryBatchLoader = null;
	}
	
	public static LinkedList<MeshInfo> getFileMeshes()
	{
		/*
		 * static const FILEMESHES& getFileMeshes(){return s_filenames;}
		 */
		return s_filenames;
	}
	
	public static boolean empty()
	{
		/*
		 * static bool empty() {return s_filenames.empty();}
		 */
		return s_filenames.isEmpty();
	}
	public static LinkedList<Drawable> loadModelFile(final String filename, final String path, GL p_gl)
	{
		/*
		 * static std::list<boost::shared_ptr<Drawable> > loadModelFile(const std::string& filename, const std::string& path = "./");
		 */
		LinkedList<Drawable> drawables = new LinkedList<Drawable>();
		MeshInfo info = new MeshInfo();
		IModelLoader pLoader = null;
		String ext = filename.substring(filename.lastIndexOf('.') + 1, filename.length());
		
		boolean loaderReady = false;
		if((s_pBinaryBatchLoader != null))
		{
			if(s_pBinaryBatchLoader.isLoaded())
			{
				loaderReady = true;
				pLoader = s_pBinaryBatchLoader;
			}
		}
		if(loaderReady)
			;
		else if(ext.equals("obj"))
		{
			pLoader = new OBJLoader();
		}
		else if(ext.equals("wrl") || ext.equals("WRL"))
		{
			String modelfile = path + filename;
			Scanner fp = null;
			try
			{
				fp = new Scanner(new BufferedReader(new FileReader(modelfile)));
				fp.useLocale(Locale.US);
			}
			catch(FileNotFoundException fnfe)
			{
				;
			}
			if(fp == null)
				return drawables;

			String format, version;
			format = fp.next();
			version = fp.next();
			fp.close();
			if(format != "#VRML")
				return drawables;

			if(version.equals("V1.0"))
				pLoader = new VRML1Loader();
			else
				pLoader = new VRML97Loader();
		}
		else 
			return drawables;

		// load the model, return value stores the drawables
		if(pLoader != null)
			drawables = pLoader.loadModel(filename, path, p_gl);
		
		// if the file is not loaded correctly, this list is empty
		if(drawables.isEmpty())
			return drawables;
		
		// save the load info
		info.format = ext;
		info.file = filename;
		info.drawables = drawables;

		s_filenames.add(info);

		return drawables;
	}
	
	public static MeshInfo unloadModelFile(final String filename)
	{
		/*
		 * static MeshInfo unloadModelFile(const std::string& filename);
		 */
		MeshManager pMM = MeshManager.getInstance();

		Iterator<MeshInfo> it = s_filenames.iterator();
		while (it.hasNext())
		{
			MeshInfo info = it.next();
			if(info.file.equals(filename))
			{
				for(Drawable dr : info.drawables)
					pMM.removeMesh(dr.getMeshName());

				it.remove();
				return info;
			}
		}
		return null;
	}
	
	public static MeshInfo isFileLoaded(final String filename)
	{
		/*
		 * static const MeshInfo* isFileLoaded(const std::string& filename);
		 */
		for(MeshInfo info : s_filenames)
		{
			if(info.file.equals(filename))
				return info;
		}
		return null;
	}
	
	public static void unloadAllFiles()
	{
		/*
		 * static void unloadAllFiles();
		 */
		Iterator<MeshInfo> it = s_filenames.iterator();
		while(it.hasNext() && !s_filenames.isEmpty())
		{
			unloadModelFile(it.next().file);
			it = s_filenames.iterator();
		}
		s_filenames.clear();
	}
	
	public static void saveAllFilesBinary(final String filename) throws NoSuchMethodException
	{
		/*
		 * static void saveAllFilesBinary(const std::string& filename);
		 */
		throw new NoSuchMethodException();
	}
}
