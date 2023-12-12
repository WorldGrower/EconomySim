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

import environment.Location;

public class LocatableAsset extends AbstractAttributableAsset<LocatableAssetAttribute> {
	
	public LocatableAsset(AssetType assetType, Location... locations) {
		this(assetType, locations, assetType.getAssetProduceForQuantity(locations.length));
	}
	
	private LocatableAsset(AssetType assetType, Location[] locations, AssetProduce remainingProduce) {
		super(assetType, createAttributes(locations), remainingProduce);
	}
	
	private LocatableAsset(AssetType assetType, List<LocatableAssetAttribute> attributes, AssetProduce remainingProduce) {
		super(assetType, attributes, remainingProduce);
	}
	
	private static List<LocatableAssetAttribute> createAttributes(Location[] locations) {
		List<LocatableAssetAttribute> attributes = new ArrayList<>();
		for(int i=0; i<locations.length; i++) { attributes.add(new LocatableAssetAttribute(locations[i].getId())); }
		return attributes;	
	}

	public Location getLocation(int index) {
		return getAttribute(index).getLocation();
	}
	
	public Location remove(int index) {
		return removeInternal(index).getLocation();
	}

	public int size() {
		return getQuantity();
	}

	public int getCapacity(int index) {
		return getAttribute(index).getCapacity();
	}
	
	public int indexOfLocation(Location location) {
		List<LocatableAssetAttribute> attributes = getReadOnlyAttributes();
		for(int i=0; i<attributes.size(); i++) {
			LocatableAssetAttribute attribute = attributes.get(i);
			if (attribute.getLocation().getId() == location.getId()) {
				return i;
			}
		}
		return -1;
	}

	public boolean canSetCapacity(int index) {
		return getAttribute(index).canSetCapacity();
	}

	public boolean canSetCapacity() {
		List<LocatableAssetAttribute> attributes = getReadOnlyAttributes();
		for(int i=0; i<attributes.size(); i++) {
			LocatableAssetAttribute attribute = attributes.get(i);
			if (!attribute.canSetCapacity()) {
				return false;
			}
		}
		return true;
	}
	
	public void setCapacity(int index, int value) {
		getAttribute(index).setCapacity(value);
	}

	public int getAge(int index) {
		return getAttribute(index).getAge();
	}
	
	@Override
	public List<Asset> divideUsingLandAsset(List<Asset> dividedLandAssets, int divisor) {
		List<Asset> assetList = new ArrayList<>();
		List<LocatableAssetAttribute> attributes = getReadOnlyAttributes();
		
		for(int i=0; i<divisor; i++) {
			List<LocatableAssetAttribute> dividedAttributes = new ArrayList<>();
			LocatableAsset landAsset = (LocatableAsset) dividedLandAssets.get(i);
			for(LocatableAssetAttribute attribute : attributes) {
				if (landAsset.indexOfLocation(attribute.getLocation()) != -1) {
					dividedAttributes.add(attribute);
				}
			}
			
			assetList.add(new LocatableAsset(getAssetType(), dividedAttributes, getAssetType().getAssetProduceForQuantity(dividedAttributes.size())));
		}
		return assetList;
	}

	@Override
	public AbstractAttributableAsset<LocatableAssetAttribute> newInstance(AssetType assetType, List<LocatableAssetAttribute> attributes, AssetProduce remainingProduce) {
		return new LocatableAsset(assetType, attributes, remainingProduce);
	}
}
