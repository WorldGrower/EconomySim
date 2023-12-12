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

import java.util.List;

import org.junit.Test;

import environment.Location;

public class UTestLocatableAsset {

	@Test
	public void testAdd() {
		Asset asset1 = new LocatableAsset(AssetType.LAND, new Location(0));
		Asset asset2 = new LocatableAsset(AssetType.LAND, new Location(1), new Location(2));
		assertEquals(1, asset1.getQuantity());
		assertEquals(2, asset2.getQuantity());
		
		LocatableAsset asset = (LocatableAsset) asset1.add(asset2);
		assertEquals(3, asset.getQuantity());
	}
	
	@Test
	public void testDecreaseQuantity() {
		Asset asset = new LocatableAsset(AssetType.LAND, new Location(0), new Location(1), new Location(2));
		asset.decreaseQuantity(2);
		
		assertEquals(1, asset.getQuantity());
	}
	
	@Test
	public void testDivide() {
		LocatableAsset asset = new LocatableAsset(AssetType.LAND, new Location(0), new Location(1), new Location(2), new Location(3));
		List<Asset> dividedAssets = asset.divide(2);
		
		assertEquals(4, asset.getQuantity());
		assertEquals(2, dividedAssets.size());
		assertEquals(2, dividedAssets.get(0).getQuantity());
		assertEquals(2, dividedAssets.get(1).getQuantity());
	}
	
	@Test
	public void testDivideOddQuantity() {
		LocatableAsset asset = new LocatableAsset(AssetType.LAND, new Location(0), new Location(1), new Location(2), new Location(3), new Location(4));
		List<Asset> dividedAssets = asset.divide(2);
		
		assertEquals(5, asset.getQuantity());
		assertEquals(2, dividedAssets.size());
		assertEquals(2, dividedAssets.get(0).getQuantity());
		assertEquals(3, dividedAssets.get(1).getQuantity());
	}
	
	@Test
	public void testIndexOfLocation() {
		LocatableAsset asset = new LocatableAsset(AssetType.LAND, new Location(0));
		assertEquals(0, asset.indexOfLocation(new Location(0)));
		assertEquals(-1, asset.indexOfLocation(new Location(1)));
	}
	
	@Test
	public void testEndOfTurn() {
		LocatableAsset asset = new LocatableAsset(AssetType.LAND, new Location(0), new Location(1));

		assertEquals(0, asset.getAge(0));
		assertEquals(0, asset.getAge(1));
		
		asset.endOfTurn();
		assertEquals(1, asset.getAge(0));
		assertEquals(1, asset.getAge(1));
	}
	
	@Test
	public void testRemove() {
		LocatableAsset asset = new LocatableAsset(AssetType.LAND, new Location(7), new Location(8));
		
		Location removedLovation = asset.remove(0);
		assertEquals(7, removedLovation.getId());
		
		assertEquals(1, asset.getQuantity());
	}
	
	@Test
	public void testGetValidIndex() {
		Location location1 = new Location(7);
		Location location2 = new Location(8);
		LocatableAsset asset = new LocatableAsset(AssetType.WHEAT_FIELD, location1, location2);
		
		assertEquals(0, asset.getValidIndex(attribute -> ((LocatableAssetAttribute) attribute).getLocation().getId() == 7));
		assertEquals(1, asset.getValidIndex(attribute -> ((LocatableAssetAttribute) attribute).getLocation().getId() == 8));
		assertEquals(-1, asset.getValidIndex(attribute -> ((LocatableAssetAttribute) attribute).getLocation().getId() == 9));
	}
	
	@Test
	public void testDivideUsingLandAsset() {
		Location location1 = new Location(7);
		Location location2 = new Location(8);
		Location location3 = new Location(9);
		LocatableAsset landAsset = new LocatableAsset(AssetType.LAND, location1, location2, location3);
		LocatableAsset wheatFieldAsset = new LocatableAsset(AssetType.WHEAT_FIELD, location3);
		
		List<Asset> dividedLandAssets = landAsset.divide(2);
		
		List<Asset> dividedAssets = wheatFieldAsset.divideUsingLandAsset(dividedLandAssets, 2);
		assertEquals(2, dividedAssets.size());
		assertEquals(0, dividedAssets.get(0).getQuantity());
		assertEquals(1, dividedAssets.get(1).getQuantity());	
	}
	
	@Test
	public void testRetrieve() {
		Location location1 = new Location(7);
		Location location2 = new Location(8);
		Location location3 = new Location(9);
		LocatableAsset landAsset = new LocatableAsset(AssetType.LAND, location1, location2, location3);
		LocatableAsset retrievedAsset = (LocatableAsset) landAsset.retrieve(1);
		
		assertEquals(1, retrievedAsset.getQuantity());
		assertEquals(0, retrievedAsset.indexOfLocation(location1));
		
		assertEquals(2, landAsset.getQuantity());
		assertEquals(-1, landAsset.indexOfLocation(location1));
		assertEquals(0, landAsset.indexOfLocation(location2));
		assertEquals(1, landAsset.indexOfLocation(location3));
	}
	
	@Test
	public void testRetrieveIndex() {
		Location location1 = new Location(7);
		Location location2 = new Location(8);
		Location location3 = new Location(9);
		LocatableAsset landAsset = new LocatableAsset(AssetType.LAND, location1, location2, location3);
		LocatableAsset retrievedAsset = (LocatableAsset) landAsset.retrieveIndex(1);
		
		assertEquals(1, retrievedAsset.getQuantity());
		assertEquals(0, retrievedAsset.indexOfLocation(location2));
		
		assertEquals(2, landAsset.getQuantity());
		assertEquals(0, landAsset.indexOfLocation(location1));
		assertEquals(-1, landAsset.indexOfLocation(location2));
		assertEquals(1, landAsset.indexOfLocation(location3));
	}
}
