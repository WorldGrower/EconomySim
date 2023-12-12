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
package knowledge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class UTestKnowledge {

	@Test
	public void testSortOnHoursToMaster() {
		List<Knowledge> values = new ArrayList<>(Arrays.asList(Knowledge.VALUES));
		Knowledge.sortOnHoursToMaster(values);
		int previousHoursToMaster = 0;
		for(Knowledge knowledge : values) {
			String message = "previousHoursToMaster " + previousHoursToMaster + " should be less or equal to " + knowledge.getHoursRequiredToMaster();
			assertTrue(message, previousHoursToMaster <= knowledge.getHoursRequiredToMaster());
			previousHoursToMaster = knowledge.getHoursRequiredToMaster();
		}
	}
	
	@Test
	public void testSortOnName() {
		List<Knowledge> values = Arrays.asList(Knowledge.STONE_TOOLING, Knowledge.CONTROL_OF_FIRE, Knowledge.FARMING);
		Knowledge.sortOnName(values);
		
		assertEquals(Arrays.asList(Knowledge.CONTROL_OF_FIRE, Knowledge.FARMING, Knowledge.STONE_TOOLING), values);
	}
	
	@Test
	public void testGetPrerequisiteCount() {
		assertEquals(0, Knowledge.STONE_TOOLING.getPrerequisiteCount());
		assertEquals(1, Knowledge.POTTERY.getPrerequisiteCount());
		assertEquals(2, Knowledge.FARMING.getPrerequisiteCount());
		assertEquals(3, Knowledge.WRITING.getPrerequisiteCount());
		assertEquals(3, Knowledge.ARITHMETIC.getPrerequisiteCount());
		
	}
}
