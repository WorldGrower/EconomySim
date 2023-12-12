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
package asset;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestTools {

	@Test
	public void testCalculateProductionNoTools() {
		Tools tools = Tools.NONE;
		assertEquals(100, tools.calculateProduction(100));
		
		tools = new Tools();
		assertEquals(100, tools.calculateProduction(100));
	}
	
	@Test
	public void testCalculateProductionOneTool() {
		Tools tools = new Tools();
		tools.add(new SimpleAsset(AssetType.STONE_SICKLE, 1));
		assertEquals(110, tools.calculateProduction(100));
	}
	
	@Test
	public void testCalculateProductionMultipleToolq() {
		Tools tools = new Tools();
		tools.add(new SimpleAsset(AssetType.STONE_SICKLE, 1));
		tools.add(new LivingAsset(AssetType.DOG));
		assertEquals(120, tools.calculateProduction(100));
	}
}
