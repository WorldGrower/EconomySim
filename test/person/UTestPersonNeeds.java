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
package person;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import asset.AssetType;
import asset.Assets;
import asset.SimpleAsset;

public class UTestPersonNeeds {

	@Test
	public void testCheckFoodAndWaterStarvation() {
		PersonNeeds personNeeds = new PersonNeeds();
		Assets assets = new Assets();
		
		assertEquals(null, personNeeds.checkFoodAndWater(assets));
		assertEquals(null, personNeeds.checkFoodAndWater(assets));
		assertEquals(CauseOfDeath.STARVATION, personNeeds.checkFoodAndWater(assets));
	}
	
	@Test
	public void testCheckFoodAndWaterDehydration() {
		PersonNeeds personNeeds = new PersonNeeds();
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.FOOD, 1000));
		
		assertEquals(null, personNeeds.checkFoodAndWater(assets));
		assertEquals(null, personNeeds.checkFoodAndWater(assets));
		personNeeds.consumeFoodAndWater(new Assets[] { assets });
		assertEquals(CauseOfDeath.DEHYDRATION, personNeeds.checkFoodAndWater(assets));
	}
	
	@Test
	public void testCheckFoodAndWaterBoundaries() {
		PersonNeeds personNeeds = new PersonNeeds();
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.FOOD, 1));
		assets.addAsset(new SimpleAsset(AssetType.WATER, 1));
		assertEquals(null, personNeeds.checkFoodAndWater(assets));
		assertEquals("100 / 200", personNeeds.getFoodDescription());
		assertEquals("100 / 200", personNeeds.getWaterDescription());
		
		personNeeds.consumeFoodAndWater(new Assets[] { assets });
		assertEquals("101 / 200", personNeeds.getFoodDescription());
		assertEquals("101 / 200", personNeeds.getWaterDescription());
		
		assertEquals(null, personNeeds.checkFoodAndWater(assets));
		
		assertEquals("1 / 200", personNeeds.getFoodDescription());
		assertEquals("1 / 200", personNeeds.getWaterDescription());
		
		assertEquals(CauseOfDeath.STARVATION, personNeeds.checkFoodAndWater(assets));
	}
	
	@Test
	public void testConsumeFoodAndWater() {
		PersonNeeds personNeeds = new PersonNeeds();
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.FOOD, 1000));
		assets.addAsset(new SimpleAsset(AssetType.WATER, 1000));
		
		personNeeds.consumeFoodAndWater(new Assets[] { assets });
		assertEquals(null, personNeeds.checkFoodAndWater(assets));
		assertEquals(1000, assets.getQuantityFor(AssetType.FOOD));
		assertEquals(1000, assets.getQuantityFor(AssetType.WATER));
		
		personNeeds.consumeFoodAndWater(new Assets[] { assets });
		assertEquals(null, personNeeds.checkFoodAndWater(assets));
		assertEquals(900, assets.getQuantityFor(AssetType.FOOD));
		assertEquals(900, assets.getQuantityFor(AssetType.WATER));
	}
	
	@Test
	public void testHasEnoughFood() {
		PersonNeeds personNeeds = new PersonNeeds();
		
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.FOOD, 1000));
		assertEquals(true, personNeeds.hasEnoughFood(assets, new Assets[] { assets }));
		
		assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.FOOD, 100));
		assertEquals(true, personNeeds.hasEnoughFood(assets, new Assets[] { assets }));
		
		personNeeds.consumeFoodAndWater(new Assets[] { assets });
		personNeeds.checkFoodAndWater(assets);
		assertEquals(true, personNeeds.hasEnoughFood(assets, new Assets[] { assets }));
		assertEquals(100, assets.getQuantityFor(AssetType.FOOD));
		assertEquals("100 / 200", personNeeds.getFoodDescription());
		
		personNeeds.consumeFoodAndWater(new Assets[] { assets });
		personNeeds.checkFoodAndWater(assets);
		assertEquals(false, personNeeds.hasEnoughFood(assets, new Assets[] { assets }));
		assertEquals(0, assets.getQuantityFor(AssetType.FOOD));
		assertEquals("100 / 200", personNeeds.getFoodDescription());
	}
	
	@Test
	public void testHasEnoughWater() {
		PersonNeeds personNeeds = new PersonNeeds();
		
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.WATER, 1000));
		assertEquals(true, personNeeds.hasEnoughWater(assets, new Assets[] { assets }));
		
		assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.WATER, 100));
		assertEquals(true, personNeeds.hasEnoughWater(assets, new Assets[] { assets }));
		
		personNeeds.consumeFoodAndWater(new Assets[] { assets });
		personNeeds.checkFoodAndWater(assets);
		assertEquals(true, personNeeds.hasEnoughWater(assets, new Assets[] { assets }));
		assertEquals(100, assets.getQuantityFor(AssetType.WATER));
		assertEquals("100 / 200", personNeeds.getWaterDescription());
		
		personNeeds.consumeFoodAndWater(new Assets[] { assets });
		personNeeds.checkFoodAndWater(assets);
		assertEquals(false, personNeeds.hasEnoughWater(assets, new Assets[] { assets }));
		assertEquals(0, assets.getQuantityFor(AssetType.WATER));
		assertEquals("100 / 200", personNeeds.getWaterDescription());
	}
}
