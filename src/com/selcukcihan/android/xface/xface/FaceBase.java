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
 * XFace::FaceBase
 * bitti.
 */

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import javax.microedition.khronos.opengles.GL11;

import com.selcukcihan.android.xface.xengine.Drawable;
import com.selcukcihan.android.xface.xengine.Entity;
import com.selcukcihan.android.xface.xengine.MeshInfo;
import com.selcukcihan.android.xface.xengine.ModelFileFactory;
import com.selcukcihan.android.xface.xengine.MorphController;
import com.selcukcihan.android.xface.xengine.TextureManager;
import com.selcukcihan.android.xface.xengine.Transform;
import com.selcukcihan.android.xface.xmath.Vector3;

public class FaceBase
{
	private AnimProcessor m_animProcessor;
	
	// typedef std::map<std::string, std::pair<std::string, Entity> > MorphTargetMap;
	// typedef std::list<XEngine::MorphTarget*> MorphTargetList;
	
	private FDP m_pFDP;
	private String m_errorString;
	private FaceEntity m_face;
	private LinkedList<String> m_filenames;

	//private GL m_gl;
	
	public FaceBase()
	{
		/*
		 * FaceBase();
		 */
		m_errorString = "";
		m_filenames = new LinkedList<String>();
		//m_gl = gl;
		m_face = new FaceEntity();
		m_animProcessor = new AnimProcessor();
	}
	
	private boolean initMorphTargets(final HashMap<String, LinkedList<MeshInfo>> targets, final String path, GL11 p_gl)
	{
		/*
		 * bool initMorphTargets(const std::map<std::string, std::list<XEngine::MeshInfo> >& targets, const std::string& path );
		 */
		if(targets.isEmpty())
			return false;
		
		String category = "";
		for(Map.Entry<String, LinkedList<MeshInfo>> it : targets.entrySet())
		{
			Entity pEnt = new Entity();
			for(MeshInfo it_meshInfos : it.getValue())
			{
				String total_path = path + it_meshInfos.path;
				// we do not use the format, we resolve it inside the loader
				LinkedList<Drawable> drawables = ModelFileFactory.loadModelFile(it_meshInfos.file, total_path, p_gl);
				if(drawables.size() == 0)
					m_errorString += "Could not load mesh: " + it_meshInfos.file + "\n";
				else			
					pEnt.addDrawables(drawables);
				category = it_meshInfos.keyframe_category;
			}
			if(pEnt.getDrawableCount() != 0)
				MorphController.getInstance().addDictionaryTarget(it.getKey(), category, pEnt);
		}
		
		return true;
	}
	
	
	public void resetDeformedVertices()
	{
		/*
		 * void resetDeformedVertices() {m_face.resetDeformedVertices();}
		 */
		m_face.resetDeformedVertices();
	}
	
	public void addPhonemeDictionary(final String dic)
	{
		/*
		 * void addPhonemeDictionary(const std::string& dic) {m_animProcessor.addPhonemeDictionary(dic);}
		 */
		m_animProcessor.addPhonemeDictionary(dic);
	}

	public void addPhonemeDictionary(final InputStream dic)
	{
		/*
		 * void addPhonemeDictionary(const std::string& dic) {m_animProcessor.addPhonemeDictionary(dic);}
		 */
		m_animProcessor.addPhonemeDictionary(dic);
	}
	
	public void reset(GL11 p_gl)
	{
		/*
		 * void reset();
		 */
		releaseMeshes();
		TextureManager.getInstance().destroyAll(p_gl); // clear the textures
		m_face.release(true);
		m_pFDP = new FDP();
		m_errorString = "";
	}
	
	public int getSpeechDuration()
	{
		/*
		 * unsigned int getSpeechDuration() const {return m_animProcessor.getSpeechDuration();}
		 */
		return m_animProcessor.getSpeechDuration();
	}
	
	public int processAnims(Scanner input)
	{
		/*
		 * int processAnims(std::istream& input);
		 */
		return m_animProcessor.processAnim(input);
	}
	
	public int processPhonemes(Scanner input, final String lang)
	{
		/*
		 * int processPhonemes(std::istream& input, const std::string& lang);
		 */
		return m_animProcessor.processPhonemes(input, lang);
	}
	
	public void rewindKeyframeAnimation()
	{
		/*
		 * void rewindKeyframeAnimation();
		 */
		MorphController.getInstance().rewind();
	}
	
	public void setTransform(final Transform tr) 
	{
		/*
		 * void setTransform(const XEngine::Transform& tr)
		 */
		m_face.setTransform(tr);
		m_pFDP.setGlobalTranslation(tr.getTranslation());
		m_pFDP.setGlobalRotation(tr.getRotation());
	}
	
	public Transform getTransform()
	{
		/*
		 * const XEngine::Transform& getTransform() const {return m_face.getTransform();}
		 */
		return m_face.getTransform();
	}
	
	public FaceEntity getRestFrame()
	{
		/*
		 * const FaceEntity& getRestFrame() const {return m_face;}
		 */
		return m_face;
	}
	
	public LinkedList<Drawable> getDrawables()
	{
		/*
		 * const std::list<boost::shared_ptr<XEngine::Drawable> >& getDrawables() const {return m_face.getDrawables();}
		 */
		return m_face.getDrawables();
	}
	
	public FAPU getFAPU()
	{
		/*
		 * const FAPU& getFAPU() const {return m_pFDP->getFAPU();};
		 */
		return m_pFDP.getFAPU();
	}
	
	public FDP getFDP()
	{
		/*
		 * boost::shared_ptr<FDP> getFDP() const {return m_pFDP;}
		 */
		return m_pFDP;
	}
	
	public void setFDP(FDP pFDP)
	{
		/*
		 * void setFDP(boost::shared_ptr<FDP> pFDP) {m_pFDP = pFDP;}
		 */
		m_pFDP = pFDP;
	}

	public String getErrorString(boolean clear)
	{
		/*
		 * std::string getErrorString(bool clear = true);
		 */
		String retVal = m_errorString;
		if(clear)
			m_errorString = "";
		return retVal;
	}
	
	public void initEyePivots(final Vector3 leftEye, final Vector3 rightEye)
	{
		/*
		 * void initEyePivots(const Vector3& leftEye, const Vector3& rightEye);
		 */
		m_face.initEyePivots(leftEye, rightEye);
	}
	
	public void initInfluenceCalculators()
	{
		/*
		 * virtual void initInfluenceCalculators();
		 */
		if(m_pFDP != null)
			m_face.initInfluenceCalculators(m_pFDP.getItems());
	}
	
	public boolean init(final String filename, final String path, GL11 p_gl)
	{
		/*
		 * virtual bool init(const std::string& filename, const std::string& path = "./");
		 */
		// load configuration and fdp
		// FDP items load
		// if we already have an object and re-initing
		reset(p_gl);

		FDPLoader fdp_file = new FDPLoader();
		
		if(fdp_file.load(path + filename, m_pFDP))
		{
			String datname = filename.substring(0, filename.length() - 3) + "dat";

			ModelFileFactory.unloadAllFiles();
			ModelFileFactory.initBinaryLoader(datname, path, p_gl);
			MorphController.getInstance().clearDictionary();
			
			// initialize the textures and meshes
			if(!initMeshes(fdp_file.getFaceEntityMeshList(), path, p_gl))
				return false;

			if(initMorphTargets(fdp_file.getMorphTargetsMeshInfos(), path, p_gl))
			{
				// also resets the controller
				MorphController.getInstance().setFDP(m_pFDP);
			}
		
			m_face.initInfluenceCalculators(m_pFDP.getItems());
			m_face.initBindings(fdp_file.getBindings());
			m_face.initEyePivots(m_pFDP.getLeftEyePivot(), m_pFDP.getRightEyePivot());
			ModelFileFactory.releaseBinaryLoader();
		}
		else
		{
			m_errorString += "Unable to load FDP file: " + filename + " at " + path + "\n";
			return false;
		}

		// initialized without loading the FDP file (first time creation)
		return true;	
	}
	
	public boolean initMeshes(final LinkedList<MeshInfo> filenames, final String path, GL11 p_gl)
	{
		/*
		 * bool initMeshes(const std::list<XEngine::MeshInfo>& filenames, const std::string& path);
		 */
		// load new ones
		for(MeshInfo meshInfo : filenames)
		{
			String total_path = path + meshInfo.path;
			// we do not use the format, we resolve it inside the loader
			LinkedList<Drawable> drawables = ModelFileFactory.loadModelFile(meshInfo.file, total_path, p_gl);
			if(!drawables.isEmpty())
			{
				m_face.addDrawables(drawables); 
				m_filenames.add(meshInfo.file);
			}
		}

		if(m_face.getDrawableCount() == 0)
			return false;

		return true;
	}
	
	public void releaseMeshes()
	{
		/*
		 * void releaseMeshes();
		 */
		for(String filename : m_filenames)
			ModelFileFactory.unloadModelFile(filename);
		m_filenames.clear();
	}
	
	public void update(final IFapStream faps)
	{
		/*
		 * void update(const boost::shared_ptr<XFace::IFapStream>& faps);
		 */
		m_face.update(faps);
	}
	
	public Entity update(int elapsed_time)
	{
		/*
		 * const Entity& update(unsigned int elapsed_time);
		 */
		MorphController.getInstance().update(elapsed_time);
		setTransform(MorphController.getInstance().getTransform());
		return MorphController.getInstance().getResult();
	}
}
