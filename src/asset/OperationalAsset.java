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

import java.util.ArrayList;
import java.util.List;

public class OperationalAsset extends AbstractAttributableAsset<OperationalAssetAttribute> {
	
	public OperationalAsset(AssetType assetType) {
		this(assetType, createAttributes(1), assetType.getAssetProduceForQuantity(1));
	}
	
	private OperationalAsset(AssetType assetType, List<OperationalAssetAttribute> attributes, AssetProduce remainingProduce) {
		super(assetType, attributes, remainingProduce);
	}
	
	private static List<OperationalAssetAttribute> createAttributes(int quantity) {
		List<OperationalAssetAttribute> attributes = new ArrayList<>();
		for(int i=0; i<quantity; i++) { attributes.add(new OperationalAssetAttribute()); }
		return attributes;	
	}

	public int size() {
		return getQuantity();
	}
	
	public OperationalAssetAttribute getOperationalAssetAttribute(int index) {
		return getAttribute(index);
	}

	@Override
	public AbstractAttributableAsset<OperationalAssetAttribute> newInstance(AssetType assetType, List<OperationalAssetAttribute> attributes, AssetProduce remainingProduce) {
		return new OperationalAsset(assetType, attributes, remainingProduce);
	}

	@Override
	public List<Asset> divideUsingLandAsset(List<Asset> dividedLandAssets, int divisor) {
		return divide(divisor);
	}
}
