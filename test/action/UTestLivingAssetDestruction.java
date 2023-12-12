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
import asset.LivingAsset;
import asset.Tools;
import environment.PublicLocationsImpl;
import society.MockPublicAssets;

public class UTestLivingAssetDestruction {

	private LivingAssetDestruction assetDestruction = new LivingAssetDestruction(AssetType.CATTLE, AssetType.FOOD, 400); 
	
	@Test
	public void testPerform() {
		Assets assets = new Assets();
		assets.addAsset(new LivingAsset(AssetType.CATTLE));
		assetDestruction.perform(assets, Tools.NONE, new MockPublicAssets(), new PublicLocationsImpl());
		
		assertEquals(0, assets.get(AssetType.CATTLE).getQuantity());
		assertEquals(400, assets.get(AssetType.FOOD).getQuantity());
	}
}
