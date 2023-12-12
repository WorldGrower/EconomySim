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

import person.Person;
import person.Sex;

public class UTestSimpleAsset {

	@Test
	public void testWork() {
		Asset asset = new SimpleAsset(AssetType.LAND, 2);
		assertEquals(true, asset.work(AssetType.FOOD));
		assertEquals(true, asset.work(AssetType.FOOD));
		assertEquals(false, asset.work(AssetType.FOOD));
		
		asset.regenerate();
		
		assertEquals(true, asset.work(AssetType.FOOD));
		assertEquals(true, asset.work(AssetType.FOOD));
		assertEquals(false, asset.work(AssetType.FOOD));
	}
	
	@Test
	public void testWorkAfterAddAsset() {
		Asset asset = new SimpleAsset(AssetType.LAND, 2);
		assertEquals(true, asset.work(AssetType.FOOD));
		assertEquals(true, asset.work(AssetType.FOOD));
		assertEquals(false, asset.work(AssetType.FOOD));
		
		asset = asset.add(new SimpleAsset(AssetType.LAND, 1));
		assertEquals(true, asset.work(AssetType.FOOD));
		assertEquals(false, asset.work(AssetType.FOOD));
	}
	
	@Test
	public void testGetQuantityFor() {
		List<Asset> assets = Arrays.asList(new SimpleAsset(AssetType.FOOD, 5), new SimpleAsset(AssetType.BREAD, 6));
		assertEquals(11, Asset.getQuantityFor(assets));
	}
	
	@Test
	public void testDivide() {
		SimpleAsset asset = new SimpleAsset(AssetType.LAND, 4);
		List<Asset> dividedAssets = asset.divide(2);
		
		assertEquals(4, asset.getQuantity());
		assertEquals(2, dividedAssets.size());
		assertEquals(2, dividedAssets.get(0).getQuantity());
		assertEquals(2, dividedAssets.get(1).getQuantity());
	}
	
	@Test
	public void testDivideOddQuantity() {
		SimpleAsset asset = new SimpleAsset(AssetType.LAND, 5);
		List<Asset> dividedAssets = asset.divide(2);
		
		assertEquals(5, asset.getQuantity());
		assertEquals(2, dividedAssets.size());
		assertEquals(2, dividedAssets.get(0).getQuantity());
		assertEquals(3, dividedAssets.get(1).getQuantity());
	}
	
	@Test
	public void testDivideAssetsLimitedLand() {
		Person person = new Person(18, Sex.MALE);
		Person child1 = new Person(18, Sex.FEMALE);
		Person child2 = new Person(18, Sex.FEMALE);
		Person child3 = new Person(18, Sex.FEMALE);
		MockAssetContainer assetContainer = new MockAssetContainer();
		
		
		person.getAssets().addAsset(new SimpleAsset(AssetType.LAND, 1));
		person.getFamily().addChild(person, child1);
		person.getFamily().addChild(person, child2);
		person.getFamily().addChild(person, child3);
		
		person.getFamily().divideAssets(person.getAssets(), assetContainer);
		
		assertEquals(1, child1.getAssets().get(AssetType.LAND).getQuantity());
		assertEquals(null, child2.getAssets().get(AssetType.LAND));
		assertEquals(null, child3.getAssets().get(AssetType.LAND));
		assertEquals(0, assetContainer.getLand());
	}
	
	@Test
	public void testRetrieve() {
		Asset asset = new SimpleAsset(AssetType.FOOD, 100);
		Asset retrievedAsset = asset.retrieve(30);
		
		assertEquals(70, asset.getQuantity());
		assertEquals(30, retrievedAsset.getQuantity());
	}
}
