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
 * XFace::FaceEntity
 * bitti.
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import xengine.DeformableGeometry;
import xengine.Drawable;
import xengine.Entity;
import xengine.KeyframeInterpolator;
import xengine.MeshManager;
import xengine.MorphController;
import xmath.Quaternion;
import xmath.Vector3;

public class FaceEntity extends Entity
{
	public FaceEntity()
	{
		/*
		 * FaceEntity(void);
		 */
		super();
	}
	
	private String expressionFAPToAlias(int id)
	{
		/*
		 * std::string expressionFAPToAlias(int id);
		 */
		switch(id) 
		{
			case 1: return "SmileClosed";
			case 2: return "Sad";
			case 3: return "Anger";
			case 4: return "Fear";
			case 5: return "Disgust";
			case 6: return "Surprise";
			default: return "Rest";
		}
	}
	
	private String visemeFAPToAlias(int id)
	{
		/*
		 * std::string visemeFAPToAlias(int id);
		 */
		switch(id) 
		{
			case 0: return "Rest";
			case 1: return "b";
			case 2: return "f";
			case 3: return "th";
			case 4: return "d";
			case 5: return "k";
			case 6: return "ch";
			case 7: return "d";
			case 8: return "n";
			case 9: return "r";
			case 10: return "aah";
			case 11: return "eh";
			case 12: return "i";
			case 13: return "oh";
			case 14: return "q";
			default: return "Rest";
		}
	}
	
	public void initEyePivots(final Vector3 leftEye, final Vector3 rightEye)
	{
		/*
		 * void initEyePivots(const Vector3& leftEye, const Vector3& rightEye);
		 */
		for(Drawable d : m_drawables)
		{
			if(d.getBinding().equals("LeftEye"))
				d.setPivot(leftEye);
			else if(d.getBinding().equals("RightEye"))
				d.setPivot(rightEye);
		}
	}
	
	public void initBindings(final HashMap<String, String> bindings)
	{
		/*
		 * void initBindings(const std::map<std::string, std::string>& bindings);
		 */
		for(Drawable d : m_drawables)
		{
			String binding = bindings.get(d.getMeshName());
			if(binding != null)
				d.setBinding(binding);
		}
	}
	
	public void initInfluenceCalculators(final LinkedList<FDPItem> items)
	{
		/*
		 * void initInfluenceCalculators(const std::list<FDPItem*>& items);
		 */
		// we have to make this function call in order to make static variables initialized in influence site
		/* RaisedCosInfluenceSph def(0, 5); */

		// clean the old (if any) drawable contents (bones)
		/* std::for_each(m_drawables.begin(), m_drawables.end(), skinResetter_functor(MeshManager::getInstance())); */
		MeshManager mm = MeshManager.getInstance();
		for(Drawable dr : m_drawables)
		{
			DeformableGeometry pMesh = mm.getMesh(dr.getMeshName());
			if(pMesh != null)
				pMesh.clearInfluences();
		}

		// calls another for_each inside for initializing weights in influence calc. and transferring them to skinned meshes
		/* std::for_each(items.begin(), items.end(), deformationInitializer_functor(MeshManager::getInstance())); */
		for(FDPItem fdp : items)
		{
			final Vector<IInfluenceCalculator> infCalcs =  fdp.getInfluenceCalculators();
			DeformableGeometry pMesh = mm.getMesh(fdp.getAffects());
			/* std::for_each(infCalcs.begin(), infCalcs.end(), influenceToSkin_functor(pMesh, fdp)); */
			for(IInfluenceCalculator inf : infCalcs)
			{
				inf.init(fdp);
				pMesh.addInfluence(fdp.getAOI(), inf.getWeights(), inf.getFapID());
			}
		}
	}
	
	public void update(IFapStream faps)
	{
		/*
		 * void update(const boost::shared_ptr<XFace::IFapStream>& faps);
		 */
		boolean highLevel = false;
		final float [] FAPs = faps.getCurrentFAP();
		MorphController morpher = MorphController.getInstance();

		if(FAPs[0] != 0) // we have a viseme
		{
			highLevel = true;
			VisemeStruct vis = faps.getCurrentViseme();
			// do the interpolation
			// final viseme = (viseme 1) * (viseme_blend / 63) + (viseme 2) * (1 - viseme_blend / 63)
			float weight = 0.5f*(1.0f - (vis.viseme_blend / 63.0f));
			Entity v1 = morpher.getDictionaryItem(visemeFAPToAlias(vis.viseme_select1));
			Entity v2 = morpher.getDictionaryItem(visemeFAPToAlias(vis.viseme_select2));
			
			if((v1.getDrawableCount() != 0) && (v2.getDrawableCount() != 0))
				KeyframeInterpolator.interpolate(v1, v2, this, weight);
			else
			{
				if(v1.getDrawableCount() == 0)
					System.err.println("Cannot find viseme " + vis.viseme_select1 + " named " +
							visemeFAPToAlias(vis.viseme_select1));
				if(v2.getDrawableCount() == 0)
					System.err.println("Cannot find viseme " + vis.viseme_select2 + " named " +
							visemeFAPToAlias(vis.viseme_select2));
			}
		}

		if(FAPs[1] != 0) // we have an expression
		{
			highLevel = true;
			ExpressionStruct exp = faps.getCurrentExpression();
			// do the interpolation
			// final expression = expression1 * (expression_intensity1 / 63)+ expression2 * (expression_intensity2 / 63)
		/*	// test data
				exp.expression_select1 = 3;
				exp.expression_intensity1 = 31;
				exp.expression_select2 = 5;
				exp.expression_intensity2 = 31;
		*/	// test data end
			Entity rest = morpher.getDictionaryItem("Rest");
			String exp1 = expressionFAPToAlias(exp.expression_select1);
			String exp2 = expressionFAPToAlias(exp.expression_select2);
					
			if(!exp1.equals("Rest")) // no need to use rest frame
			{
				Entity v1 = morpher.getDictionaryItem(exp1);
				
				if((v1.getDrawableCount() != 0) && (exp.expression_intensity1 != 0))
				{
					Entity result1 = new Entity();
					result1.copyFrom(rest, true);

					// smooth
					KeyframeInterpolator.interpolate(rest, v1, result1, exp.expression_intensity1 / 63.0f);
					
					// blend
					morpher.getBlender().blend(this, result1);
				}
				else if(v1.getDrawableCount() == 0)
					System.err.println(expressionFAPToAlias(exp.expression_select1) + " has no drawables");
			}
			if(!exp2.equals("Rest")) // no need to use rest frame
			{
				Entity v2 = morpher.getDictionaryItem(exp2);
				if((v2.getDrawableCount() != 0) && (exp.expression_intensity2 != 0))
				{
					Entity result2 = new Entity();
					result2.copyFrom(rest, true);
					
					// smooth
					KeyframeInterpolator.interpolate(rest, v2, result2, exp.expression_intensity2 / 63.0f);
					
					// blend
					morpher.getBlender().blend(this, result2);
				}
				else if(v2.getDrawableCount() == 0)
					System.err.println(expressionFAPToAlias(exp.expression_select2) + " has no drawables");
			}
		}
		
		if(!highLevel)
		{
			/* std::for_each(m_drawables.begin(), m_drawables.end(), skinDeformer_functor(MeshManager.getInstance(), faps)); */
			for(Drawable dr : m_drawables)
				dr.updateAnimationParams(faps);
		}

		m_transform.setRotation(new Quaternion(-FAPs[48]*100.001f, FAPs[49]*100.001f, -FAPs[47]*100.001f));
	///	m_transform.rotate(Quaternion(FAPs[48]*3.0f, FAPs[49]*3.0f, FAPs[47]*3.0f));
		m_transform.updateLocalTransform();
	}

}
