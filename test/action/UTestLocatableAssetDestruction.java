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
import asset.Tools;
import environment.Location;
import environment.PublicLocationsImpl;
import society.MockPublicAssets;

public class UTestLocatableAssetDestruction {

	private LocatableAssetDestruction assetDestruction = new LocatableAssetDestruction(AssetType.WHEAT_FIELD, AssetType.WHEAT, 100); 
	
	@Test
	public void testPerform() {
		Assets assets = new Assets();
		assets.addAsset(new LocatableAsset(AssetType.WHEAT_FIELD, new Location(0)));
		assetDestruction.perform(assets, Tools.NONE, new MockPublicAssets(), new PublicLocationsImpl());
		
		assertEquals(0, assets.get(AssetType.WHEAT_FIELD).getQuantity());
		assertEquals(200, assets.get(AssetType.WHEAT).getQuantity());
	}
}
