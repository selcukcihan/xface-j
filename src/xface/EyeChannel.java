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
 * XFace::EyeChannel
 * bitti.
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import xengine.DeformableGeometry;
import xengine.Drawable;
import xengine.Entity;
import xengine.MeshManager;
import xengine.MorphChannel;
import xengine.Vector3Buffer;
import xmath.Vector3;

public class EyeChannel extends MorphChannel
{
/*
class EyeChannel :
	public XEngine::MorphChannel
{
protected:
	virtual bool updateResult(XEngine::Entity& result, const XEngine::Entity& rest);
public:
	EyeChannel(const std::string& name) : XEngine::MorphChannel(name){}

};
 */
	public EyeChannel(final String name)
	{
		super(name);
	}
	
	protected boolean updateResult(Entity result, final Entity rest)
	{
		if((result.getDrawableCount() == 0) || (m_result.getDrawableCount() == 0))
			return false;
		
		MeshManager pMM = MeshManager.getInstance();
		final LinkedList<Drawable> restDr = rest.getDrawables();
		LinkedList<Drawable> resDr = result.getDrawables();
		final LinkedList<Drawable> eyeDr = m_result.getDrawables();
		Iterator<Drawable> it_rest = restDr.iterator();
		Iterator<Drawable> it_res = resDr.iterator();
		Iterator<Drawable> it_eye = eyeDr.iterator();
		while(it_res.hasNext())
		{
			Drawable it_rest_object = it_rest.next();
			if((!m_name.equals("BlinkEyes")) && (!it_rest_object.getBinding().equals("LeftEye")) &&
					(!it_rest_object.getBinding().equals("RightEye")))
			{
				it_res.next();
				it_eye.next();
				continue;
			}
			DeformableGeometry pRes = pMM.getMesh(it_rest_object.getMeshName());
			final DeformableGeometry pEye = pMM.getMesh(it_eye.next().getMeshName());
			final DeformableGeometry pRest = pMM.getMesh(it_rest.next().getMeshName());
			
			Vector3Buffer vRest = pRest.getVertices();
			Vector3Buffer vRes = pRes.getDeformedVerticesVector();
			Vector3Buffer vResNorm = pRes.getNormals();
			final Vector3Buffer vEye = pEye.getDeformedVerticesVector();
			final Vector3Buffer vEyeNorm = pEye.getNormals();
			for(int i = 0; i < pRes.getVertexCount(); i++)
			{
				Vector3 test = vRest.get(i).opSubtract(vEye.get(i));
				if(Math.abs(test.x) + Math.abs(test.y) + Math.abs(test.z) > 0.1f)
				{
					vRes.put(i, new Vector3(vEye.get(i)));
					vResNorm.put(i, new Vector3(vEyeNorm.get(i)));
				}
			}
		}
		return true;
	}
}
