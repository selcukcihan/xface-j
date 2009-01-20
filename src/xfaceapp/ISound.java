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

package xfaceapp;

public interface ISound
{
/*
 * 	class ISound
	{
	public:
		virtual bool loadWAV(const std::string& filename) = 0;
		virtual void jump(float percentage) = 0;
		virtual void mute(bool) = 0;
		virtual void unload() = 0;
		virtual void play() = 0;
		virtual void pause() = 0;
		virtual void stop() = 0;
		virtual ~ISound(void){}
	};
 */
	abstract public boolean loadWAV(final String filename);
	abstract public void jump(float percentage);
	abstract public void mute(boolean nosound);
	abstract public void unload();
	abstract public void play();
	abstract public void pause();
	abstract public void stop();
}
