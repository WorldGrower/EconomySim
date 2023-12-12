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

public class LivingAsset extends AbstractAttributableAsset<LivingAssetAttribute> {
	
	public LivingAsset(AssetType assetType) {
		this(assetType, createAttributes(1), assetType.getAssetProduceForQuantity(1));
	}
	
	private LivingAsset(AssetType assetType, List<LivingAssetAttribute> attributes, AssetProduce remainingProduce) {
		super(assetType, attributes, remainingProduce);
	}
	
	private static List<LivingAssetAttribute> createAttributes(int quantity) {
		List<LivingAssetAttribute> attributes = new ArrayList<>();
		for(int i=0; i<quantity; i++) { attributes.add(new LivingAssetAttribute()); }
		return attributes;	
	}

	public int size() {
		return getQuantity();
	}

	public int getAge(int index) {
		return getAttribute(index).getAge();
	}
	
	public void remove(int index) {
		removeInternal(index);
	}
	
	@Override
	public AbstractAttributableAsset<LivingAssetAttribute> newInstance(AssetType assetType, List<LivingAssetAttribute> attributes, AssetProduce remainingProduce) {
		return new LivingAsset(assetType, attributes, remainingProduce);
	}

	@Override
	public List<Asset> divideUsingLandAsset(List<Asset> dividedLandAssets, int divisor) {
		return divide(divisor);
	}
}
