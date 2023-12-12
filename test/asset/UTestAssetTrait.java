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

import society.MockPublicAssets;

public class UTestAssetTrait {

	@Test
	public void testPerishable() {
		Assets assets = new Assets();
		Asset asset = new SimpleAsset(AssetType.FOOD, 200);
		assets.addAsset(asset);
		AssetTrait.PERISHABLE.endOfTurn(assets, asset, new MockPublicAssets(), new AssetStorage());
		
		assertEquals(0, assets.getQuantityFor(AssetType.FOOD));
	}
	
	@Test
	public void testPerishablePartial() {
		Assets assets = new Assets();
		Asset asset = new SimpleAsset(AssetType.FOOD, 200);
		assets.addAsset(asset);
		assets.addAsset(new SimpleAsset(AssetType.CLAY_POT, 1));
		AssetTrait.PERISHABLE.endOfTurn(assets, asset, new MockPublicAssets(), new AssetStorage());
		
		assertEquals(100, assets.getQuantityFor(AssetType.FOOD));
	}
	
	@Test
	public void testPerishableMultiple() {
		AssetStorage assetStorage = new AssetStorage();
		Assets assets = new Assets();
		Asset asset = new SimpleAsset(AssetType.WHEAT, 200);
		Asset asset2 = new SimpleAsset(AssetType.FLOUR, 200);
		Asset asset3 = new SimpleAsset(AssetType.BREAD, 200);
		assets.addAsset(asset);
		assets.addAsset(asset2);
		assets.addAsset(asset3);
		assets.addAsset(new SimpleAsset(AssetType.CLAY_POT, 3));
		AssetTrait.PERISHABLE.endOfTurn(assets, asset, new MockPublicAssets(), assetStorage);
		AssetTrait.PERISHABLE.endOfTurn(assets, asset2, new MockPublicAssets(), assetStorage);
		AssetTrait.PERISHABLE.endOfTurn(assets, asset3, new MockPublicAssets(), assetStorage);
		
		assertEquals(200, assets.getQuantityFor(AssetType.WHEAT));
		assertEquals(100, assets.getQuantityFor(AssetType.FLOUR));
		assertEquals(0, assets.getQuantityFor(AssetType.BREAD));
	}
	
	@Test
	public void testLiquid() {
		Assets assets = new Assets();
		Asset asset = new SimpleAsset(AssetType.WATER, 200);
		assets.addAsset(asset);
		AssetTrait.LIQUID.endOfTurn(assets, asset, new MockPublicAssets(), new AssetStorage());
		
		assertEquals(0, assets.getQuantityFor(AssetType.WATER));
	}
	
	@Test
	public void testLiquidPartial() {
		Assets assets = new Assets();
		Asset asset = new SimpleAsset(AssetType.WATER, 200);
		assets.addAsset(asset);
		assets.addAsset(new SimpleAsset(AssetType.GLAZED_POT, 1));
		AssetTrait.LIQUID.endOfTurn(assets, asset, new MockPublicAssets(), new AssetStorage());
		
		assertEquals(100, assets.getQuantityFor(AssetType.WATER));
	}
}
