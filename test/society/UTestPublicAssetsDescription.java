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
package society;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import asset.AssetType;
import asset.OperationalAsset;
import asset.SimpleAsset;

public class UTestPublicAssetsDescription {

	@Test
	public void testGetRiverRemainingProduceDescription() {
		PublicAssets publicAssets = new MockPublicAssets();
		PublicAssetsDescription publicAssetsDescription = new PublicAssetsDescription(publicAssets);
		assertEquals("", publicAssetsDescription.getRiverRemainingProduceDescription());
		
		publicAssets.addAsset(new SimpleAsset(AssetType.RIVER, 10));
		assertEquals("10 / 10", publicAssetsDescription.getRiverRemainingProduceDescription());
	}
	
	@Test
	public void testGetIrrigationCanalOperational() {
		PublicAssets publicAssets = new MockPublicAssets();
		PublicAssetsDescription publicAssetsDescription = new PublicAssetsDescription(publicAssets);
		assertEquals("", publicAssetsDescription.getIrrigationCanalOperational());
		
		publicAssets.addAsset(new OperationalAsset(AssetType.IRRIGATION_CANAL));
		assertEquals("0 / 1000 ( operational: false )", publicAssetsDescription.getIrrigationCanalOperational());
	}
}
