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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestAssetType {

	@Test
	public void testAssetTraits() {
		assertEquals(true, AssetType.FOOD.hasTrait(AssetTrait.PERISHABLE));
		assertEquals(false, AssetType.FOOD.hasTrait(AssetTrait.LIGHT_SOURCE));
		
		assertArrayEquals(new AssetTrait[] { AssetTrait.FOOD_SOURCE, AssetTrait.PERISHABLE }, AssetType.FOOD.getAssetTraits());
		assertArrayEquals(new AssetTrait[] { }, AssetType.TOOL.getAssetTraits());
	}
	
	@Test
	public void testCanBePublic() {
		assertEquals(true, AssetType.RIVER.canBePublic());
		assertEquals(false, AssetType.FOOD.canBePublic());
		assertEquals(false, AssetType.WATER.canBePublic());
		assertEquals(true, AssetType.WHEAT.canBePublic());
		assertEquals(false, AssetType.FLOUR.canBePublic());
		assertEquals(false, AssetType.BREAD.canBePublic());
		assertEquals(false, AssetType.ANIMAL_HIDE_CLOTHES.canBePublic());
		assertEquals(false, AssetType.COTTON_CLOTHES.canBePublic());
		assertEquals(false, AssetType.CLAY_POT.canBePublic());
		assertEquals(false, AssetType.GLAZED_POT.canBePublic());
		assertEquals(true, AssetType.KILN.canBePublic());
		assertEquals(true, AssetType.DOG.canBePublic());
		assertEquals(false, AssetType.CAMP_FIRE.canBePublic());
	}
}
