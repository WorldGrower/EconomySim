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
import asset.SimpleAsset;
import asset.Tools;
import environment.PublicLocationsImpl;
import society.MockPublicAssets;

public class UTestAssetTransformation {

	private AssetTransformation assetTransformation = new AssetTransformation(AssetType.WHEAT, AssetType.FLOUR, 100); 
	
	@Test
	public void testPerformLessThan100() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.WHEAT, 80));
		assetTransformation.perform(assets, Tools.NONE, new MockPublicAssets(), new PublicLocationsImpl());
		
		assertEquals(0, assets.getQuantityFor(AssetType.WHEAT));
		assertEquals(80, assets.getQuantityFor(AssetType.FLOUR));
	}
	
	@Test
	public void testPerformMoreThan100() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.WHEAT, 120));
		assetTransformation.perform(assets, Tools.NONE, new MockPublicAssets(), new PublicLocationsImpl());
		
		assertEquals(20, assets.getQuantityFor(AssetType.WHEAT));
		assertEquals(100, assets.getQuantityFor(AssetType.FLOUR));
	}
	
	@Test
	public void testPerformTooling() {
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.WHEAT, 80));
		SimpleAsset tool = new SimpleAsset(AssetType.STONE_SICKLE, 1);
		Tools tools = new Tools();
		tools.add(tool);
		assetTransformation.perform(assets, tools, new MockPublicAssets(), new PublicLocationsImpl());
		
		assertEquals(0, assets.getQuantityFor(AssetType.WHEAT));
		assertEquals(90, assets.getQuantityFor(AssetType.FLOUR));
	}
}
