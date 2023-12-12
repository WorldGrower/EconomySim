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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import action.Activity;
import environment.Location;
import society.PublicAssets;

public class Assets implements AssetContainer, Serializable {
	private Map<AssetType, Asset> assets = new EnumMap<>(AssetType.class);
	private transient Collection<Asset> values = assets.values();
	
	public void addAsset(Asset asset) {
		if (asset.getQuantity() > 0) {
			AssetType assetType = asset.getAssetType();
			if (this.assets.containsKey(assetType)) {
				this.assets.put(assetType, asset.add(this.assets.get(assetType)));
			} else {
				this.assets.put(assetType, asset);
			}
		}
	}
	
	@Override
	public void addAssets(Assets assetsToBeAdded) {
		for(Asset asset : assetsToBeAdded.values) {
			addAsset(asset);
		}
	}

	public Asset get(AssetType assetType) {
		return assets.get(assetType);
	}

	public Collection<Asset> values() {
		return values;
	}
	
	public Collection<AssetType> keys() {
		return assets.keySet();
	}
	
	public void regenerate() {
		for(Asset asset : values) {
			asset.regenerate();
			if (asset.getQuantity() == 0) {
				assets.remove(asset.getAssetType());
			}
		}
	}

	public List<Assets> divide(int divisor) {
		List<Assets> dividedAssetsList = new ArrayList<>();
		for(int i=0; i<divisor; i++) {
			dividedAssetsList.add(new Assets()); 
		}
		AssetDivision assetDivision = AssetDivisionFactory.createAssetDivision(this, divisor);
		for(Asset asset : values) {
			List<Asset> dividedAssets = assetDivision.getDividedAssets(asset, divisor);
			for(int i=0; i<dividedAssets.size(); i++) {
				dividedAssetsList.get(i).addAsset(dividedAssets.get(i));
			}
		}
		
		return dividedAssetsList;
	}

	public int getQuantityFor(AssetType assetType) {
		Asset asset = assets.get(assetType);
		if (asset != null) {
			return asset.getQuantity();
		} else {
			return 0;
		}
	}
	
	public void increaseQuantity(AssetType assetType, int quantityIncrease) {
		if (assets.containsKey(assetType)) {
			assets.get(assetType).increaseQuantity(quantityIncrease);
		} else {
			assets.put(assetType, new SimpleAsset(assetType, quantityIncrease));
		}
	}

	public Asset getProducingAsset(AssetType neededAssetType) {
		AssetType[] producingAssetTypes = NeededAssetTypeCache.getProducingAssets(neededAssetType);
		for(AssetType producingAssetType : producingAssetTypes) {
			Asset asset = assets.get(producingAssetType);
			if (asset != null && asset.hasRemainingProduce(neededAssetType)) {
				return asset;
			}
		}
		return null;
	}

	public Tools findToolsForAsset(AssetType assetType, AssetType neededAssetType) {
		Activity activity = ActivityCache.findActivity(assetType, neededAssetType);
		if (activity != null) {
			Tools tools = new Tools();
			for(AssetType toolAssetType : activity.getTools()) {
				Asset tool = assets.get(toolAssetType);
				if (tool != null) {
					tools.add(tool);
				}
			}
			return tools;
		}
		
		return Tools.NONE;
	}

	public void removeAsset(AssetType assetType, int quantity) {
		Asset asset = assets.get(assetType);
		asset.decreaseQuantity(quantity);
		if (asset.getQuantity() == 0) {
			assets.remove(assetType);
		}
	}

	public void endOfTurn(int maxQuantity, PublicAssets publicAssets) {
		//TODO: 1 clay_pot doesn't cover all perishables
		//int perishableStorageCount = getAssetCount(AssetTrait.PERISHABLE_STORAGE);
		AssetStorage assetStorage = new AssetStorage();
		for(Asset asset : values) {
			AssetType assetType = asset.getAssetType();
			AssetTrait[] assetTraits = assetType.getAssetTraits();
			for(AssetTrait assetTrait : assetTraits) {
				assetTrait.endOfTurn(this, asset, publicAssets, assetStorage);
			}
			//TODO: this should also run for public assets, except code below
			
			if (assetType.isTradeable()) {
				int quantity = getQuantityFor(assetType);
				if (quantity > maxQuantity) {
					Asset retrievedAssets = retrieve(assetType, quantity - maxQuantity);
					publicAssets.addAsset(retrievedAssets);
				}
			}
		}
	}

	public boolean hasTrait(AssetTrait assetTrait) {
		for(Asset asset : values) {
			AssetType assetType = asset.getAssetType();
			if (assetType.hasTrait(assetTrait) && getQuantityFor(assetType) > 0) {
				return true;
			}
		}
		return false;
	}

	public List<Asset> getAssetsWithTrait(AssetTrait assetTrait) {
		List<Asset> assetsList = new ArrayList<>();
		for(Asset asset : values) {
			AssetType assetType = asset.getAssetType();
			if (assetType.hasTrait(assetTrait)) {
				assetsList.add(asset);
			}
		}
		return assetsList;
	}

	public void removeAssetsWithTrait(AssetTrait assetTrait, int quantity) {
		int totalQuantityToBeRemoved = quantity;
		for(Asset asset : values) {
			AssetType assetType = asset.getAssetType();
			if (assetType.hasTrait(assetTrait)) {
				int quantityToBeRemoved = Math.min(asset.getQuantity(), totalQuantityToBeRemoved);
				asset.decreaseQuantity(quantityToBeRemoved);
				totalQuantityToBeRemoved -= quantityToBeRemoved;
				if (totalQuantityToBeRemoved == 0) {
					break;
				}
			}
		}
	}

	public Asset retrieve(AssetType assetType, int quantity) {
		Asset asset = assets.get(assetType);
		return asset.retrieve(quantity);
	}
	
	@Override
	public String toString() {
		return "assets: " + assets;
	}

	public List<AssetType> getSortedAssetKeys() {
		List<AssetType> sortedAssetKeys = new ArrayList<>(keys());
		Collections.sort(sortedAssetKeys, new AssetTypeComparator());
		return sortedAssetKeys;
	}
	
	private static class AssetTypeComparator implements Comparator<AssetType> {

		@Override
		public int compare(AssetType o1, AssetType o2) {
			return o1.name().compareTo(o2.name());
		}
	}
	
	@Serial
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	    ois.defaultReadObject();
	    this.values = assets.values();
	}
	
	@Serial
	private void writeObject(ObjectOutputStream oos) throws IOException {
	    oos.defaultWriteObject();
	}

	public Assets retrieveLocationAssetsForOneLocation() {
		Assets returnedAssets = new Assets();
		Asset landAsset = get(AssetType.LAND);
		if (landAsset != null && landAsset.getQuantity() > 0) {
			Asset retrievedLand = landAsset.retrieve(1);
			returnedAssets.addAsset(retrievedLand);
			Location location = ((LocatableAsset) retrievedLand).getLocation(0);
			
			for(Asset asset : values) {
				AssetType assetType = asset.getAssetType();
				if (assetType != AssetType.LAND && assetType.hasLocation()) {
					LocatableAsset locatableAsset = (LocatableAsset) get(assetType);
					int indexOfLocation = locatableAsset.indexOfLocation(location);
					if (indexOfLocation != -1) {
						returnedAssets.addAsset(locatableAsset.retrieveIndex(indexOfLocation));
					}
				}
			}
		}
		return returnedAssets;
	}

	public void removeNonPublicAssets() {
		Iterator<AssetType> assetIterator = assets.keySet().iterator();
		while(assetIterator.hasNext()) {
			AssetType assetType = assetIterator.next();
			if (!assetType.canBePublic()) {
				assetIterator.remove();
			}
		}
	}

	public void remove(AssetType assetType) {
		assets.remove(assetType);
	}
}
