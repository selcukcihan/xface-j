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
 * XFace::FDPLoader
 * bitti.
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//ANDROID
//import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import com.selcukcihan.android.xface.xengine.MeshInfo;
import com.selcukcihan.android.xface.xmath.Vector3;

public class FDPLoader
{
	private FDP m_pFDP;
	private String m_version;
	private LinkedList<MeshInfo> m_faceEntityMeshInfo;
	private HashMap<String, String> m_bindings;
	/* private HashMap<String, Entity> m_morphTargets; */
	private HashMap<String, LinkedList<MeshInfo>> m_morphTargetsMeshInfos;
	/* private Entity m_faceEntity; */

	public FDPLoader()
	{
		/*
		 * FDPLoader(void);
		 */
		/* m_faceEntity = new Entity(); */
		m_morphTargetsMeshInfos = new HashMap<String, LinkedList<MeshInfo>>();
		/* m_morphTargets = new HashMap<String, Entity>(); */
		m_bindings = new HashMap<String, String>();
		m_faceEntityMeshInfo = new LinkedList<MeshInfo>();
		m_version = "0.2";
	}
	
	/*
	 * public bool load(std::istream& input, boost::shared_ptr<FDP> pFDP);
	 */

	public HashMap<String, String> getBindings()
	{
		/*
		 * std::map<std::string, std::string> getBindings() const {return m_bindings;}
		 */
		return (HashMap<String, String>) m_bindings.clone(); /* deep copy shallow copy olaylarinda sorun yoktur umarim */
		/* ama zaten String immutable, sorun olmamasi lazim */
	}
	
	public LinkedList<MeshInfo> getFaceEntityMeshList()
	{
		/*
		 * std::list<MeshInfo> getFaceEntityMeshList() const {return m_faceEntityMeshInfo;}	// yes make a full copy in return
		 */
		/*
		 * full copy yap denmis, biz de oyle yapalim, ama c++da MeshInfo struct, asagidaki koddan emin degilim
		 */
		LinkedList<MeshInfo> retVal = new LinkedList<MeshInfo>();
		for(MeshInfo m : m_faceEntityMeshInfo)
			retVal.add(new MeshInfo(m));
		return retVal;
	}
	
	public HashMap<String, LinkedList<MeshInfo>> getMorphTargetsMeshInfos()
	{
		/*
		 * const std::map<std::string, std::list<MeshInfo> >& getMorphTargetsMeshInfos(){return m_morphTargetsMeshInfos;}
		 */
		return m_morphTargetsMeshInfos;
	}
	
	public boolean load(final String filename, FDP pFDP)
	{
		/*
		 * bool load(const std::string& filename, boost::shared_ptr<FDP> pFDP);
		 */
		boolean retVal = false; /* assume the worst case, we couldnt load the fdp */
		m_pFDP = pFDP;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
        // parser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", true);
        // parser.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);
        try
        {
        	DocumentBuilder parser = dbf.newDocumentBuilder();
	        Document document = parser.parse(filename);
	        retVal = parse(document);
        }
        catch(ParserConfigurationException pce)
        {
        	pce.printStackTrace();
        }
        catch(SAXException se)
        {
        	se.printStackTrace();
        }
        catch(IOException ioe)
        {
        	ioe.printStackTrace();
        }
        
        return retVal;
	}
	
	private boolean parse(Document doc)
	{
		/*
		 * bool parse();
		 */
		m_bindings.clear();
		m_morphTargetsMeshInfos.clear();
		m_faceEntityMeshInfo.clear();

		// get the header
		NodeList nodes = doc.getElementsByTagName("head");

		if((nodes == null) || (nodes.getLength() != 1)) // sth wrong
			return false;
		if(!parseHeader(nodes.item(0)))
			return false;
		
		// get the source info
		nodes = doc.getElementsByTagName("source");
		if((nodes == null) || (nodes.getLength() != 1)) // sth wrong
			return false;
		
		parseSource(nodes.item(0));
			
		// process fdp's now
		nodes = doc.getElementsByTagName("fdp");
		for(int i = 0; i < nodes.getLength(); i++)
			loadFDPItem(nodes.item(i));
			
		return true;
	}
	
	private boolean parseHeader(Node header)
	{
		/*
		 * bool parseHeader(XERCES_CPP_NAMESPACE::DOMNode* pHeader);
		 */
		NodeList nodes = header.getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++)
		{
			/* there are "file", "model", "fapu", "translation" and "rotation" elements in this chunk */
			Node n = nodes.item(i);
			if(n.getNodeName().equals("file"))
			{
				/* sample: <file version="0.2" /> */
				NamedNodeMap attr = n.getAttributes();
				if(attr.getLength() == 0)
					return false;
				Node versionAttr = attr.getNamedItem("version");
				if(!m_version.equals(versionAttr.getNodeValue()))
					return false; /* versions not matching, flee!! */
			}
			else if(n.getNodeName().equals("fapu"))
			{
				/* sample: <fapu ES0="69.9977" IRISD0="16.0424" ENS0="51.8036" MNS0="30.1538" MW0="50.6392" /> */
				NamedNodeMap attrList = n.getAttributes();
				if(attrList.getLength() == 0) /* sth wrong */
					return false;

				/////////////// ES0
				Node attr = attrList.getNamedItem("ES0");
				if(attr != null)
					m_pFDP.setES0(Float.parseFloat(attr.getNodeValue()));
				/////////////// IRISD0
				attr = attrList.getNamedItem("IRISD0");
				if(attr != null)
					m_pFDP.setIRISD0(Float.parseFloat(attr.getNodeValue()));
				/////////////// ENS0
				attr = attrList.getNamedItem("ENS0");
				if(attr != null)
					m_pFDP.setENS0(Float.parseFloat(attr.getNodeValue()));
				/////////////// MNS0
				attr = attrList.getNamedItem("MNS0");
				if(attr != null)
					m_pFDP.setMNS0(Float.parseFloat(attr.getNodeValue()));
				/////////////// MW0
				attr = attrList.getNamedItem("MW0");
				if(attr != null)
					m_pFDP.setMW0(Float.parseFloat(attr.getNodeValue()));
			}
			else if(n.getNodeName().equals("translation"))
			{
				// sample: <translation x="0" y="-1" z="-659" />
	        
				NamedNodeMap attrList = n.getAttributes();
				if(attrList.getLength() == 0) // sth wrong
					return false;

				float x = 0, y = 0, z = 0;
				/////////////// x translation
				Node attr = attrList.getNamedItem("x");
				if(attr != null)
					x = Float.parseFloat(attr.getNodeValue());
				
				/////////////// y translation
				attr = attrList.getNamedItem("y");
				if(attr != null)
					y = Float.parseFloat(attr.getNodeValue());
			
				/////////////// z translation
				attr = attrList.getNamedItem("z");
				if(attr != null)
					z = Float.parseFloat(attr.getNodeValue());
			
				m_pFDP.setGlobalTranslation(x, y, z);
			}
			else if(n.getNodeName().equals("rotation"))
			{
				// sample: <rotation axis_x="-0.998192" axis_y="0.0596591" axis_z="0.00728935" axis_angle="0.444541" />

				NamedNodeMap attrList = n.getAttributes();
				if(attrList.getLength() == 0) // sth wrong
					return false;

				float x = 0, y = 0, z = 0, a = 0;
				/////////////// x rotation
				Node attr = attrList.getNamedItem("axis_x");
				if(attr != null)
					x = Float.parseFloat(attr.getNodeValue());
				
				/////////////// y rotation
				attr = attrList.getNamedItem("axis_y");
				if(attr != null)
					y = Float.parseFloat(attr.getNodeValue());
			
				/////////////// z rotation
				attr = attrList.getNamedItem("axis_z");
				if(attr != null)
					z = Float.parseFloat(attr.getNodeValue());
			
				/////////////// z rotation
				attr = attrList.getNamedItem("axis_angle");
				if(attr != null)
					a = Float.parseFloat(attr.getNodeValue());
			
				m_pFDP.setGlobalRotation(x, y, z, a);
			}
		}
		return true;
	}

	private void parseSource(Node source)
	{
		NodeList nodes = source.getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++)
		{
			Node n = nodes.item(i);
			if(n.getNodeName().equals("entity"))
			{
				loadEntity(n);
			}
		}
	}

	private void loadEntity(Node entity)
	{
		/*
		 * void loadEntity(XERCES_CPP_NAMESPACE::DOMNode* pSource);
		 */
		NamedNodeMap attrList = entity.getAttributes();
		String alias = new String("");
		String category = new String("");
		
		Node attr = attrList.getNamedItem("alias");
		if(attr != null)
			alias = attr.getNodeValue();

		attr = attrList.getNamedItem("category");
		if(attr != null)
			category = attr.getNodeValue();
		
		NodeList nodes = entity.getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++)
		{
			/* there are "file", "model", "fapu", "translation" and "rotation" elements in this chunk */
			Node n = nodes.item(i);
			if(n.getNodeName().equals("mesh"))
			{
				MeshInfo info = new MeshInfo();
				
				attrList = n.getAttributes();
				
				attr = attrList.getNamedItem("file");
				if(attr != null)
					info.file = attr.getNodeValue();
				
				attr = attrList.getNamedItem("format");
				if(attr != null)
					info.format = attr.getNodeValue();
				
				attr = attrList.getNamedItem("path");
				if(attr != null)
					info.path = attr.getNodeValue();
				else
					info.path = new String("");
				
				info.keyframe_alias = alias;
				info.keyframe_category = category;

				if(alias.equals("Rest"))
					m_faceEntityMeshInfo.add(info);
				// push the Rest state into the morph target dictionary as well
				LinkedList<MeshInfo> meshinfo = m_morphTargetsMeshInfos.get(alias);
				if(meshinfo == null) /* the entry will be created now, since it does not exist yet */
					m_morphTargetsMeshInfos.put(alias, new LinkedList<MeshInfo>());
				m_morphTargetsMeshInfos.get(alias).add(info);
			}
			else if(n.getNodeName().equals("bind"))
			{
				/* MeshInfo info; */
				String submesh = new String("");
				String item = new String("");
				
				attrList = n.getAttributes();
				attr = attrList.getNamedItem("submesh");
				if(attr != null)
					submesh = attr.getNodeValue();
				attr = attrList.getNamedItem("item");
				if(attr != null)
					item = attr.getNodeValue();
				if(item.equals("LeftEye") || item.equals("RightEye")) // eye pivots
				{
					Vector3 eye = new Vector3(0, 0, 0);
					//std::string x, y, z;
					attr = attrList.getNamedItem("pivotX");
					if(attr != null)
						eye.x = Float.parseFloat(attr.getNodeValue());
					attr = attrList.getNamedItem("pivotY");
					if(attr != null)
						eye.y = Float.parseFloat(attr.getNodeValue());
					attr = attrList.getNamedItem("pivotZ");
					if(attr != null)
						eye.z = Float.parseFloat(attr.getNodeValue());
					if(item.equals("LeftEye"))
						m_pFDP.setLeftEyePivot(eye);
					else
						m_pFDP.setRightEyePivot(eye);
				}
				
				m_bindings.put(submesh, item);
			}
		}
	}

	private void loadFDPItem(Node item)
	{
		/*
		 * void loadFDPItem(XERCES_CPP_NAMESPACE::DOMNode* pFDPItem);
		 */
		NamedNodeMap attrList = item.getAttributes();
		FDPItem pItem = null;
		// get the name (id)
		Node attr = attrList.getNamedItem("name");
		if(attr != null)
			pItem = new FDPItem(attr.getNodeValue());
		else
			return;
		
		// get the control vertex index
		attr = attrList.getNamedItem("index");
		if(attr != null)
			pItem.setControlPoint(Integer.parseInt(attr.getNodeValue()));

		// get the affecting mesh name
		attr = attrList.getNamedItem("affects");
		if(attr != null)
			pItem.setAffects(attr.getNodeValue());
		
		NodeList nodes = item.getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++)
		{
			Node n = nodes.item(i);
			if(n.getNodeName().equals("indices"))
			{
				NodeList children = n.getChildNodes();
 
				// we should have only one child, text node, just a safety net here
				if((children.getLength() == 1) && (children.item(0).getNodeType() == Node.TEXT_NODE))
					processIndices(children.item(0).getNodeValue(), pItem);
			}
			else if(n.getNodeName().equals("influence"))
			{
				// sample: <influence weight="0.25" fap="3" type="RaisedCosInfluenceWaveY" />
				attrList = n.getAttributes();
				// get the weight
				float w = Float.parseFloat(attrList.getNamedItem("weight").getNodeValue());
				int fap = (int)Float.parseFloat(attrList.getNamedItem("fap").getNodeValue());
				
				try
				{
					Class c = Class.forName("xface." + attrList.getNamedItem("type").getNodeValue());
					IInfluenceCalculator pInfluence = (IInfluenceCalculator)c.newInstance();
					if(pInfluence != null)
					{
						pInfluence.setFields(w, fap);
						pItem.addInfluenceCalculator(pInfluence);
					}
				}
				catch(ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				catch(InstantiationException ie)
				{
					ie.printStackTrace();
				}
				catch(IllegalAccessException iae)
				{
					iae.printStackTrace();
				}
			}
		}

		m_pFDP.insertItem(pItem);
	}

	private void processIndices(final String p_indices, FDPItem pItem)
	{
		/*
		 * void processIndices(const std::string& indices, FDPItem* pItem);
		 */
		
		/*
		 * p_indices is something like : "10 21 341 98" and we are retrieving the individual integers in this method
		 */
		String [] indices = p_indices.split(" ");
		for(String i : indices)
			pItem.addAOIIndex(Integer.parseInt(i));
	}
	
}
