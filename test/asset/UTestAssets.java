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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import environment.Location;
import society.MockPublicAssets;

public class UTestAssets {

	@Test
	public void testAddAsset() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.LAND, 5));
		
		Asset actualAsset = assets.get(AssetType.LAND);
		assertEquals(AssetType.LAND, actualAsset.getAssetType());
		assertEquals(5, actualAsset.getQuantity());
		
		assets.addAsset(new SimpleAsset(AssetType.LAND, 5));
		
		actualAsset = assets.get(AssetType.LAND);
		assertEquals(AssetType.LAND, actualAsset.getAssetType());
		assertEquals(10, actualAsset.getQuantity());
	}
	
	@Test
	public void testAddAssetZeroQuantity() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.LAND, 0));
		
		Asset actualAsset = assets.get(AssetType.LAND);
		assertEquals(null, actualAsset);
	}
	
	@Test
	public void testDivide() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.LAND, 5));
		
		List<Assets> dividedAssets = assets.divide(2);
		assertEquals(2, dividedAssets.size());
		assertEquals(2, dividedAssets.get(0).get(AssetType.LAND).getQuantity());
		assertEquals(3, dividedAssets.get(1).get(AssetType.LAND).getQuantity());
	}
	
	@Test
	public void testRegenerate() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.LAND, 1));
		assets.addAsset(new SimpleAsset(AssetType.RIVER, 1));
		
		assertEquals(true, assets.get(AssetType.LAND).work(AssetType.FOOD));
		assertEquals(false, assets.get(AssetType.LAND).work(AssetType.FOOD));
		assertEquals(true, assets.get(AssetType.RIVER).work(AssetType.WATER));
		assertEquals(false, assets.get(AssetType.RIVER).work(AssetType.WATER));
		
		assets.regenerate();
		assertEquals(true, assets.get(AssetType.LAND).work(AssetType.FOOD));
		assertEquals(false, assets.get(AssetType.LAND).work(AssetType.FOOD));
		assertEquals(true, assets.get(AssetType.RIVER).work(AssetType.WATER));
		assertEquals(false, assets.get(AssetType.RIVER).work(AssetType.WATER));
	}
	
	@Test
	public void testIncreaseQuantity() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.LAND, 5));
		
		assets.increaseQuantity(AssetType.LAND, 1);
		assertEquals(6, assets.getQuantityFor(AssetType.LAND));
	}
	
	@Test
	public void testEndOfTurn() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.LAND, 5));
		assets.addAsset(new SimpleAsset(AssetType.CAMP_FIRE, 1));
		assets.addAsset(new SimpleAsset(AssetType.FOOD, 100));
		assets.addAsset(new SimpleAsset(AssetType.WATER, 100));
		assets.addAsset(new SimpleAsset(AssetType.CLAY_POT, 10));
		assets.addAsset(new SimpleAsset(AssetType.GLAZED_POT, 10));
		
		MockPublicAssets publicAssets = new MockPublicAssets();
		assets.endOfTurn(300, publicAssets);
		assertEquals(5, assets.getQuantityFor(AssetType.LAND));
		assertEquals(0, assets.getQuantityFor(AssetType.CAMP_FIRE));
		assertEquals(100, assets.getQuantityFor(AssetType.FOOD));
		assertEquals(100, assets.getQuantityFor(AssetType.WATER));
		assertEquals(0, publicAssets.size());
	}
	
	@Test
	public void testEndOfTurnMaxQuantityExceeded() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.LAND, 65));
		assets.addAsset(new SimpleAsset(AssetType.FOOD, 100));
		assets.addAsset(new SimpleAsset(AssetType.WATER, 100));
		assets.addAsset(new SimpleAsset(AssetType.CLAY_POT, 55));
		
		MockPublicAssets publicAssets = new MockPublicAssets();
		assets.endOfTurn(50, publicAssets);
		assertEquals(65, assets.getQuantityFor(AssetType.LAND));
		assertEquals(50, assets.getQuantityFor(AssetType.FOOD));
		assertEquals(0, assets.getQuantityFor(AssetType.WATER));
		assertEquals(50, assets.getQuantityFor(AssetType.CLAY_POT));
		assertEquals(2, publicAssets.size());
		assertEquals(50, publicAssets.getQuantityFor(AssetType.FOOD));
		assertEquals(5, publicAssets.getQuantityFor(AssetType.CLAY_POT));
	}
	
	@Test
	public void testEndOfTurnWaterConsumption() {
		Assets assets = new Assets();
		Asset riverAsset = new SimpleAsset(AssetType.RIVER, 1000);
		assets.addAsset(new LivingAsset(AssetType.DOG));
		
		MockPublicAssets publicAssets = new MockPublicAssets(riverAsset);
		assets.endOfTurn(300, publicAssets);
		assertEquals("water: 950 / 1000, irrigation canal: 1000 / 1000", riverAsset.getRemainingProduceDescription());
		assertEquals(1, assets.getQuantityFor(AssetType.DOG));
	}
	
	@Test
	public void testEndOfTurnWaterConsumptionMultiple() {
		Assets assets = new Assets();
		Asset riverAsset = new SimpleAsset(AssetType.RIVER, 1000);
		assets.addAsset(new LivingAsset(AssetType.DOG));
		assets.addAsset(new LivingAsset(AssetType.DOG));
		
		MockPublicAssets publicAssets = new MockPublicAssets(riverAsset);
		assets.endOfTurn(300, publicAssets);
		assertEquals("water: 900 / 1000, irrigation canal: 1000 / 1000", riverAsset.getRemainingProduceDescription());
		assertEquals(2, assets.getQuantityFor(AssetType.DOG));
	}
	
	@Test
	public void testEndOfTurnWaterConsumptionWaterShortage() {
		Assets assets = new Assets();
		Asset riverAsset = new SimpleAsset(AssetType.RIVER, 10);
		assets.addAsset(new LivingAsset(AssetType.DOG));
		
		MockPublicAssets publicAssets = new MockPublicAssets(riverAsset);
		assets.endOfTurn(300, publicAssets);
		assertEquals("water: 0 / 10, irrigation canal: 10 / 10", riverAsset.getRemainingProduceDescription());
		assertEquals(0, assets.getQuantityFor(AssetType.DOG));
	}
	
	@Test
	public void testEndOfTurnWaterConsumptionWaterShortageMultiple() {
		Assets assets = new Assets();
		Asset riverAsset = new SimpleAsset(AssetType.RIVER, 70);
		assets.addAsset(new LivingAsset(AssetType.DOG));
		assets.addAsset(new LivingAsset(AssetType.DOG));
		assets.addAsset(new LivingAsset(AssetType.DOG));
		
		MockPublicAssets publicAssets = new MockPublicAssets(riverAsset);
		assets.endOfTurn(300, publicAssets);
		assertEquals("water: 0 / 70, irrigation canal: 70 / 70", riverAsset.getRemainingProduceDescription());
		assertEquals(1, assets.getQuantityFor(AssetType.DOG));
	}
	
	@Test
	public void testEndOfTurnWaterConsumptionWaterShortageNoRemainingProduce() {
		Assets assets = new Assets();
		Asset riverAsset = new SimpleAsset(AssetType.RIVER, 50);
		assets.addAsset(new LivingAsset(AssetType.DOG));
		
		MockPublicAssets publicAssets = new MockPublicAssets(riverAsset);
		assets.endOfTurn(300, publicAssets);
		assertEquals("water: 0 / 50, irrigation canal: 50 / 50", riverAsset.getRemainingProduceDescription());
		assertEquals(1, assets.getQuantityFor(AssetType.DOG));
		
		assets.endOfTurn(300, publicAssets);
		assertEquals("water: 0 / 50, irrigation canal: 50 / 50", riverAsset.getRemainingProduceDescription());
		assertEquals(0, assets.getQuantityFor(AssetType.DOG));
	}
	
	@Test
	public void testIncreaseAge() {
		Assets assets = new Assets();
		assets.addAsset(new LocatableAsset(AssetType.WHEAT_FIELD, new Location(7)));
		
		LocatableAsset asset = (LocatableAsset) assets.get(AssetType.WHEAT_FIELD);
		assertEquals(0, asset.getAge(0));
		
		assets.regenerate();
		assertEquals(1, asset.getAge(0));
	}
	
	@Test
	public void testHasTrait() {
		Assets assets = new Assets();
		assertEquals(false, assets.hasTrait(AssetTrait.LIGHT_SOURCE));
		
		assets.addAsset(new SimpleAsset(AssetType.LAND, 5));
		assets.addAsset(new SimpleAsset(AssetType.CAMP_FIRE, 1));
		assertEquals(true, assets.hasTrait(AssetTrait.LIGHT_SOURCE));
		
		assets.removeAsset(AssetType.CAMP_FIRE, 1);
		assertEquals(false, assets.hasTrait(AssetTrait.LIGHT_SOURCE));
	}
	
	@Test
	public void testGetAssetsWithTrait() {
		Assets assets = new Assets();
		List<Asset> assetsWithTrait = assets.getAssetsWithTrait(AssetTrait.FOOD_SOURCE);
		assertEquals(0, assetsWithTrait.size());
		
		assets.addAsset(new SimpleAsset(AssetType.FOOD, 5));
		assets.addAsset(new SimpleAsset(AssetType.BREAD, 6));
		assets.addAsset(new SimpleAsset(AssetType.CLAY_POT, 3));
		
		assetsWithTrait = assets.getAssetsWithTrait(AssetTrait.FOOD_SOURCE);
		assertEquals(2, assetsWithTrait.size());
	}
	
	@Test
	public void testRemoveAssetsWithTrait() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.FOOD, 5));
		assets.addAsset(new SimpleAsset(AssetType.BREAD, 6));
		assets.addAsset(new SimpleAsset(AssetType.CLAY_POT, 3));
		
		assets.removeAssetsWithTrait(AssetTrait.FOOD_SOURCE, 2);
		assertEquals(3, assets.getQuantityFor(AssetType.FOOD));
		assertEquals(6, assets.getQuantityFor(AssetType.BREAD));
		
		assets.removeAssetsWithTrait(AssetTrait.FOOD_SOURCE, 5);
		assertEquals(0, assets.getQuantityFor(AssetType.FOOD));
		assertEquals(4, assets.getQuantityFor(AssetType.BREAD));
	}
	
	@Test
	public void testFindToolsForAsset() {
		Assets assets = new Assets();
		SimpleAsset stoneSickle = new SimpleAsset(AssetType.STONE_SICKLE, 1);
		assets.addAsset(stoneSickle);
		
		Tools tools = assets.findToolsForAsset(AssetType.LAND, AssetType.FOOD);
		Tools expectedTools = new Tools();
		expectedTools.add(stoneSickle);
		
		assertEquals(expectedTools, tools);
	}
	
	@Test
	public void testFindToolForAssetMultiple() {
		Assets assets = new Assets();
		SimpleAsset stoneSickle = new SimpleAsset(AssetType.STONE_SICKLE, 1);
		assets.addAsset(stoneSickle);
		LivingAsset dog = new LivingAsset(AssetType.DOG);
		assets.addAsset(dog);
		
		Tools tools = assets.findToolsForAsset(AssetType.LAND, AssetType.FOOD);
		Tools expectedTools = new Tools();
		expectedTools.add(stoneSickle);
		expectedTools.add(dog);
		
		assertEquals(expectedTools, tools);
	}
	
	@Test
	public void testRemoveAsset() {
		Assets assets = new Assets();
		SimpleAsset stoneSickle = new SimpleAsset(AssetType.STONE_SICKLE, 1);
		assets.addAsset(stoneSickle);
		
		assertEquals(1, assets.getQuantityFor(AssetType.STONE_SICKLE));
		
		assets.removeAsset(AssetType.STONE_SICKLE, 1);
		assertEquals(0, assets.getQuantityFor(AssetType.STONE_SICKLE));
	}
	
	@Test
	public void testgetSortedAssetKeys() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.STONE_SICKLE, 1));
		assets.addAsset(new SimpleAsset(AssetType.BREAD, 1));
		assets.addAsset(new SimpleAsset(AssetType.LAND, 1));
		
		assertEquals(Arrays.asList(AssetType.BREAD, AssetType.LAND, AssetType.STONE_SICKLE), assets.getSortedAssetKeys());
	}
	
	@Test
	public void testRetrieveLocationAssetsForOneLocation() {
		Assets assets = new Assets();
		assets.addAsset(new LocatableAsset(AssetType.LAND, new Location(7), new Location(8)));
		assets.addAsset(new LocatableAsset(AssetType.WHEAT_FIELD, new Location(7)));
		assets.addAsset(new LocatableAsset(AssetType.IRRIGATION_CANAL, new Location(8)));
		
		Assets retrievedAssets = assets.retrieveLocationAssetsForOneLocation();
		assertEquals(1, retrievedAssets.getQuantityFor(AssetType.LAND));
		assertEquals(1, retrievedAssets.getQuantityFor(AssetType.WHEAT_FIELD));
		assertEquals(0, retrievedAssets.getQuantityFor(AssetType.IRRIGATION_CANAL));
		
		assertEquals(1, assets.getQuantityFor(AssetType.LAND));
		assertEquals(0, assets.getQuantityFor(AssetType.WHEAT_FIELD));
		assertEquals(1, assets.getQuantityFor(AssetType.IRRIGATION_CANAL));
	}
	
	@Test
	public void testRemoveNonPublicAssets() {
		Assets assets = new Assets();
		assets.addAsset(new LocatableAsset(AssetType.LAND, new Location(7)));
		assets.addAsset(new SimpleAsset(AssetType.STONE_SICKLE, 1));
		assets.addAsset(new SimpleAsset(AssetType.ACCOUNTING_SYSTEM, 1));
		
		assets.removeNonPublicAssets();
		
		assertEquals(1, assets.getQuantityFor(AssetType.LAND));
		assertEquals(1, assets.getQuantityFor(AssetType.STONE_SICKLE));
		assertEquals(0, assets.getQuantityFor(AssetType.ACCOUNTING_SYSTEM));
	}
}
