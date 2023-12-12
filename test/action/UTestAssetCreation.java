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
package action;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import asset.AssetType;
import asset.Assets;
import asset.LocatableAsset;
import asset.SimpleAsset;
import environment.Location;
import environment.PublicLocations;
import environment.PublicLocationsImpl;
import society.MockPublicAssets;

public class UTestAssetCreation {

	@Test
	public void testCanPerform() {
		AssetCreation assetCreation = new AssetCreation(AssetType.WHEAT_FIELD, AssetType.LAND, AssetType.WHEAT, 100);
		Assets assets = new Assets();
		assertEquals(false, assetCreation.canPerform(assets, new PublicLocationsImpl()));
		
		assets.addAsset(new SimpleAsset(AssetType.WHEAT, 200));
		assertEquals(false, assetCreation.canPerform(assets, new PublicLocationsImpl()));
		
		assets.addAsset(new LocatableAsset(AssetType.LAND, new Location(0)));
		assertEquals(true, assetCreation.canPerform(assets, new PublicLocationsImpl()));
	}
	
	@Test
	public void testCanPerformInsufficientQuality() {
		AssetCreation assetCreation = new AssetCreation(AssetType.WHEAT_FIELD, AssetType.LAND, AssetType.WHEAT, 100);
		Assets assets = new Assets();
		assets.addAsset(new LocatableAsset(AssetType.LAND, new Location(0)));
		assets.addAsset(new SimpleAsset(AssetType.WHEAT, 5));
		assertEquals(false, assetCreation.canPerform(assets, new PublicLocationsImpl()));
	}
	
	@Test
	public void testCanPerformJustEnoughQuality() {
		AssetCreation assetCreation = new AssetCreation(AssetType.WHEAT_FIELD, AssetType.LAND, AssetType.WHEAT, 100);
		Assets assets = new Assets();
		assets.addAsset(new LocatableAsset(AssetType.LAND, new Location(0)));
		assets.addAsset(new SimpleAsset(AssetType.WHEAT, 100));
		assertEquals(true, assetCreation.canPerform(assets, new PublicLocationsImpl()));
	}
	
	@Test
	public void testCanPerformWithoutConsumable() {
		PublicLocations publicLocations = new PublicLocationsImpl();
		AssetCreation assetCreation = new AssetCreation(AssetType.IRRIGATION_CANAL, AssetType.LAND);
		Assets assets = new Assets();
		assertEquals(false, assetCreation.canPerform(assets, publicLocations));
		
		assets.addAsset(new LocatableAsset(AssetType.LAND, new Location(0)));		
		assertEquals(true, assetCreation.canPerform(assets, publicLocations));
		
		assetCreation.perform(assets, null, new MockPublicAssets(), publicLocations);
		assertEquals(false, assetCreation.canPerform(assets, publicLocations));
	}
}
