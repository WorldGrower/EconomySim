/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import main.SocietyFactory;
import society.Society;

public class UTestGameSave {

	@Test
	public void testSaveLoad() throws IOException {
		File tempFile = File.createTempFile("economysim", ".sav");
		tempFile.deleteOnExit();
		Society society = SocietyFactory.createDefaultInstance();
		GameSave.save(tempFile.getAbsolutePath(), society);
		
		Society loadedSociety = GameSave.load(tempFile.getAbsolutePath());
		
		assertEquals(society.size(), loadedSociety.size());
		assertEquals(true, tempFile.length() > 0);
	}
}
