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
 * XEngine::DeformableGeometry
 * bitti.
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.selcukcihan.xfacej.xface.IFapStream;
import com.selcukcihan.xfacej.xmath.Vector3;

public class DeformableGeometry extends IndexedFaceSet
{
	public static final int MAXWEIGHT = 8;
	
	private static Vector<Vector3> m_FAPDirection = null;
	Vector<DeformationWeights> m_weights = null;
	Vector<DeformationIndices> m_weightIndices = null;
	Vector3Buffer m_def_vertices = null;
	
	DeformableGeometry(final String name)
	{
		// DeformableGeometry(const std::string& name);
		
		super(name);

		m_weights = new Vector<DeformationWeights>();
		m_weightIndices = new Vector<DeformationIndices>();
		m_def_vertices = new Vector3Buffer();
	}
	
	public static void prepareFAPLookup()
	{
		// static void prepareFAPLookup();
		
		if(m_FAPDirection == null)
		{
			m_FAPDirection = new Vector<Vector3>();
			// first two faps (high level)
			m_FAPDirection.add(new Vector3(0, 0, 0));		// 1. viseme
			m_FAPDirection.add(new Vector3(0, 0, 0));		// 2. emotion
			m_FAPDirection.add(new Vector3(0, -1, 0));	// 3. Open_jaw
			m_FAPDirection.add(new Vector3(0, -1, 0));	// 4. lower_t_midlip
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 5. Raise_b_midlip
			m_FAPDirection.add(new Vector3(1, 0, 0));		// 6. Stretch_l_cornerlip
			m_FAPDirection.add(new Vector3(-1, 0, 0));	// 7. Stretch_r_cornerlip
			m_FAPDirection.add(new Vector3(0, -1, 0));	// 8. Lower_t_lip_lm
			m_FAPDirection.add(new Vector3(0, -1, 0));	// 9. Lower_t_lip_rm
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 10. Raise_b_lip_lm
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 11. Raise_b_lip_rm
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 12. Raise_l_cornerlip
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 13. Raise_r_cornerlip
			m_FAPDirection.add(new Vector3(0, 0, 1));		// 14. Thrust_jaw
			m_FAPDirection.add(new Vector3(-1, 0, 0));	// 15. Shift_jaw
			m_FAPDirection.add(new Vector3(0, 0, 1));		// 16. Push_b_lip
			m_FAPDirection.add(new Vector3(0, 0, 1));		// 17. Push_t_lip
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 18. Depress_chin
			m_FAPDirection.add(new Vector3(0, -1, 0.5f));	// 19. Close_t_l_eyelid
			m_FAPDirection.add(new Vector3(0, -1, 0.5f));	// 20. Close_t_r_eyelid
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 21. Close_b_l_eyelid
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 22. Close_b_r_eyelid
			m_FAPDirection.add(new Vector3(1, 0, 0));		// 23. Yaw_l_eyeball
			m_FAPDirection.add(new Vector3(1, 0, 0));		// 24. Yaw_r_eyeball
			m_FAPDirection.add(new Vector3(0, -1, 0));	// 25. Pitch_l_eyeball
			m_FAPDirection.add(new Vector3(0, -1, 0));	// 26. Pitch_r_eyeball
			m_FAPDirection.add(new Vector3(0, 0, 1));		// 27. Thrust_l_eyeball
			m_FAPDirection.add(new Vector3(0, 0, 1));		// 28. Thrust_r_eyeball
			m_FAPDirection.add(new Vector3(1, 1, 1));		// 29. Dilate_l_pupil (grow)
			m_FAPDirection.add(new Vector3(1, 1, 1));		// 30. Dilate_r_pupil (grow)
	
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 31. Raise_l_i_eyebrow
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 32. Raise_r_i_eyebrow
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 33. Raise_l_m_eyebrow
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 34. Raise_r_m_eyebrow
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 35. Raise_l_o_eyebrow
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 36. Raise_r_o_eyebrow
			m_FAPDirection.add(new Vector3(-1, 0, 0));	// 37. Squeeze_l_eyebrow
			m_FAPDirection.add(new Vector3(1, 0, 0));		// 38. Squeeze_r_eyebrow
			m_FAPDirection.add(new Vector3(1, 0, 0));		// 39. Puff_l_cheek
			m_FAPDirection.add(new Vector3(-1, 0, 0));	// 40. Puff_r_cheek
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 41. Lift_l_cheek
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 42. Lift_r_cheek
			m_FAPDirection.add(new Vector3(-1, 0, 0));	// 43. Shift_tongue_tip
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 44. Raise_tongue_tip
			m_FAPDirection.add(new Vector3(0, 0, 1));		// 45. Thrust_tongue_tip
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 46. Raise_tongue
			m_FAPDirection.add(new Vector3(0, 1, 1));		// 47. Tongue_roll
			
			m_FAPDirection.add(new Vector3(0, -1, 0));	// 48. Head_pitch
			m_FAPDirection.add(new Vector3(1, 0, 0));		// 49. Head_yaw
			m_FAPDirection.add(new Vector3(-1, 0, 0));	// 50. Head_roll
			m_FAPDirection.add(new Vector3(0, -1, 0));	// 51. Lower_t_midlip_o
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 52. Raise_b_midlip_o
			m_FAPDirection.add(new Vector3(1, 0, 0));		// 53. Stretch_l_cornerlip_o
			m_FAPDirection.add(new Vector3(-1, 0, 0));	// 54. Stretch_r_cornerlip_o
			m_FAPDirection.add(new Vector3(0, -1, 0));	// 55. Lower_t_lip_lm_o
			m_FAPDirection.add(new Vector3(0, -1, 0));	// 56. Lower_t_lip_rm_o
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 57. Raise_b_lip_lm_o
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 58. Raise_b_lip_rm_o
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 59. Raise_l_cornerlip_o
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 60. Raise_r_cornerlip_o
	
			m_FAPDirection.add(new Vector3(1, 0, 0));		// 61. Stretch_l_nose
			m_FAPDirection.add(new Vector3(-1, 0, 0));	// 62. Stretch_r_nose
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 63. Raise_nose
			m_FAPDirection.add(new Vector3(-1, 0, 0));	// 64. Bend_nose
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 65. Raise_l_ear
			m_FAPDirection.add(new Vector3(0, 1, 0));		// 66. Raise_r_ear
			m_FAPDirection.add(new Vector3(1, 0, 0));		// 67. Pull_l_ear
			m_FAPDirection.add(new Vector3(-1, 0, 0));	// 68. Pull_r_ear
		}
	}
	
	public void readBinary(RandomAccessFile fp) throws IOException
	{
		// void readBinary(FILE* fp);
		
		super.readBinary(fp);
		if(m_def_vertices.size() == 0)
			m_def_vertices = new Vector3Buffer(m_vertices);
	}
	
	public void writeBinary(RandomAccessFile fp) throws IOException
	{
		// void writeBinary(FILE* fp);
		
		super.writeBinary(fp);
	}
	
	public DeformableGeometry copyFrom(final DeformableGeometry rhs)
	{
		// virtual DeformableGeometry& copyFrom(const DeformableGeometry& rhs);
		
		// self assignment control
		if (this == rhs) 
			return this;

		super.copyFrom(rhs);

		m_def_vertices = new Vector3Buffer(rhs.m_def_vertices);
		m_weightIndices = VectorWrapper.WrapDeformationIndices(rhs.m_weightIndices);
		m_weights = VectorWrapper.WrapDeformationWeights(rhs.m_weights);

		return this;
	}
	
	public void resetDeformedVertices()
	{
		// void resetDeformedVertices();
		
		if(m_def_vertices.size() == 0)
			m_def_vertices = new Vector3Buffer(m_vertices);
		else
		{
			m_def_vertices.rewind();
			m_vertices.rewind();
			while(m_def_vertices.hasRemaining())
				m_def_vertices.put(m_vertices.get());
		}

		computeVertexNormals();
	}
	
	public FloatBuffer getDeformedVerticesGL()
	{
		// const Vector3* const getDeformedVertices() const {return &m_def_vertices[0];}
		//gl fonksiyonlarina veriyoruz return degerini
		m_def_vertices.rewind();
		return m_def_vertices.floatBuffer();
	}

	public Vector3Buffer getDeformedVerticesVector()
	{
		/*
		 * const std::vector<Vector3>& getDeformedVerticesVector() const {return m_def_vertices;}
		 */
		m_def_vertices.rewind();
		return m_def_vertices;
	}

	public void setVertices(Vector3 [] pVert, int size)
	{
		// void setVertices(Vector3* pVert, unsigned int size);
		
		// let base class has to do whatever it has to do
		/*
		super.setVertices(pVert, size);
		if(m_def_vertices.isEmpty())
			m_def_vertices = VectorWrapper.WrapVector3(m_vertices);
			*/
		throw new UnsupportedOperationException();

		//clearInfluences();
	}
	
	public void setVertices(final Vector<Vector3> vertices)
	{
		// void setVertices(const std::vector<Vector3> &vertices);
		
		// let base class has to do whatever it has to do
		super.setVertices(vertices);
		if(m_def_vertices.size() == 0)
			m_def_vertices = new Vector3Buffer(vertices);

		//clearInfluences();
	}
	
	public void setDeformedVertices(final Vector<Vector3> vertices)
	{
		// void setDeformedVertices(const std::vector<Vector3> &vertices);
		
		m_def_vertices = new Vector3Buffer(vertices);
		computeVertexNormals();
	}
	public void setDeformedVertices(final Vector3Buffer vertices)
	{
		/*
		 * bunu mu cagiricam yukaridaki sekilde mi kullanmali bilmiyorum zaman gosterecek
		 */
		if(m_def_vertices != vertices)
			m_def_vertices = new Vector3Buffer(vertices);
		computeVertexNormals();
	}
	
	public void addInfluence(final Set<Integer> aoi, final Vector<Float> w, int fap)
	{
		// void addInfluence(const std::set<unsigned short>& aoi, const std::vector<float>& w, unsigned short fap);
		
		// set the weight and bone id from the data
		int i = 0;
		for(Iterator<Integer> it = aoi.iterator(); (it.hasNext() && (i < w.size())); i++)
		{
			// get an empty slot in weight (limit is MAXWEIGHT weights per vertex)
			int slot = 0;
			int curIndex = it.next();
			while((slot < MAXWEIGHT) && (m_weights.elementAt(curIndex).w[slot] != 0))
				slot++;
			if(slot == MAXWEIGHT) // this vertex is full of weights, ignore the rest..
				continue;

			// assign finally!
			m_weights.elementAt(curIndex).w[slot] = w.elementAt(i);
			m_weightIndices.elementAt(curIndex).id[slot] = fap;
		}
	}
	
	public void clearInfluences()
	{
		// void clearInfluences();
		
		m_weights.clear();
		m_weightIndices.clear();
		for(int i = 0; i<m_vertices.size(); i++)
		{
			m_weights.add(new DeformationWeights());
			m_weightIndices.add(new DeformationIndices());
		}
	}
	
	public void update(final IFapStream faps)
	{
		// void update(const boost::shared_ptr<XFace::IFapStream>& faps);
		
		final float[] animationParams = faps.getCurrentFAP();
		for(int i = 0; i < m_def_vertices.size(); i++)
		{
			m_def_vertices.put(i,new Vector3(m_vertices.get(i)));
			for(int j = 0; j < MAXWEIGHT; j++)
			{
				if(m_weightIndices.elementAt(i).id[j] == 0)
					continue;
				// float fap = animationParams.elementAt(m_weightIndices.elementAt(i).id[j]);
				float fap = animationParams[(m_weightIndices.elementAt(i).id[j])];
				if(fap != 0)
				{
					/*
					 * perform the below cpp 7-hit combo
					 * m_def_vertices[i] += m_FAPDirection[ m_weightIndices[i][j] ] * (fap * m_weights[i][j]);
					 */
					Vector3 temp = m_FAPDirection.elementAt(
							m_weightIndices.elementAt(i).id[j]).opMultiplyScalar(fap*m_weights.elementAt(i).w[j]);
					m_def_vertices.put(i, m_def_vertices.get(i).opAdd(temp));
				}
			}
		}

//		computeVertexNormals();
	}
	
}
