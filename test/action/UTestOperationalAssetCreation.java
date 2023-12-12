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
import asset.Tools;
import environment.MockPublicLocations;
import society.MockPublicAssets;
import society.PublicAssets;

public class UTestOperationalAssetCreation {

	@Test
	public void testPerformPublic() {
		OperationalAssetCreation operationalAssetCreation = new OperationalAssetCreation(AssetType.RIVER, AssetType.IRRIGATION_CANAL, AssetOwner.PUBLIC);
		Assets personAssets = new Assets();
		PublicAssets publicAssets = new MockPublicAssets();
		operationalAssetCreation.perform(personAssets, Tools.NONE, publicAssets, new MockPublicLocations());
		
		assertEquals(0, personAssets.getQuantityFor(AssetType.IRRIGATION_CANAL));
	}
}
