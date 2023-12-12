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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import action.AttributableAsset;

abstract class AbstractAttributableAsset<T extends AssetAttribute> extends AbstractAsset implements AttributableAsset {
	private List<T> attributes;
	
	protected AbstractAttributableAsset(AssetType assetType, List<T> attributes, AssetProduce remainingProduce) {
		super(assetType, remainingProduce);
		this.attributes = attributes;
	}
	
	protected final List<T> getReadOnlyAttributes() {
		return Collections.unmodifiableList(attributes);
	}
	
	@Override
	public final int getQuantity() {
		return attributes.size();
	}
	
	public abstract AbstractAttributableAsset<T> newInstance(AssetType assetType, List<T> attributes, AssetProduce remainingProduce);

	@Override
	public final AbstractAttributableAsset<T> add(Asset asset) {
		AbstractAttributableAsset<T> other = (AbstractAttributableAsset<T>) asset;
		return newInstance(getAssetType(), addAttributes(attributes, other.attributes), remainingProduce.add(other.remainingProduce));
	}
	
	private List<T> addAttributes(List<T> list1, List<T> list2) {
		List<T> newAttributes = new ArrayList<>();
		newAttributes.addAll(list1);
		newAttributes.addAll(list2);
		
		return newAttributes;
	}
	
	protected final Iterator<T> iterator() {
		return attributes.iterator();
	}

	@Override
	public final void increaseQuantity(int quantityIncrease) {
		throw new IllegalStateException("increaseQuantity shouldn't be called for LocatableAsset " + this);
	}
	
	@Override
	public final void decreaseQuantity(int quantityDecrease) {
		if (quantityDecrease > attributes.size()) {
			throw new IllegalStateException("decreaseQuantity failed for class "+ getClass() + ", quantityDecrease " + quantityDecrease + ", and attributes.size " + attributes.size());
		}
		List<T> attributesToRemove = new ArrayList<T>(attributes.subList(attributes.size() - quantityDecrease, attributes.size()));
		attributes.removeAll(attributesToRemove);
	}

	@Override
	public final List<Asset> divide(int divisor) {
		List<Asset> assetList = new ArrayList<>();
		int dividedQuantity = attributes.size() / divisor;
		if (dividedQuantity == 0) { dividedQuantity = 1; }
		int currentAttributesIndex = 0;
		for(int i=0; i<divisor; i++) {
			final List<T> dividedAttributes;
			if (i == divisor - 1) {
				dividedAttributes = new ArrayList<>(attributes.subList(currentAttributesIndex, attributes.size()));
			} else if (currentAttributesIndex + dividedQuantity <= attributes.size()) {
				dividedAttributes = new ArrayList<>(attributes.subList(currentAttributesIndex, currentAttributesIndex + dividedQuantity));
				currentAttributesIndex += dividedQuantity;
			} else {
				dividedAttributes = new ArrayList<>();
			}
			assetList.add(newInstance(getAssetType(), dividedAttributes, getAssetType().getAssetProduceForQuantity(dividedAttributes.size())));
		}
		return assetList;
	}

	protected final T getAttribute(int index) {
		return attributes.get(index);
	}

	@Override
	public final void endOfTurn() {
		Iterator<T> iterator = attributes.iterator();
		while(iterator.hasNext()) {
			T attribute = iterator.next();
			boolean remove = attribute.endOfTurn();
			if (remove) {
				iterator.remove();
			}
		}
	}

	protected final T removeInternal(int index) {
		return attributes.remove(index);
	}
	
	@Override
	public final int getValidIndex(AssetEvaluator assetEvaluator) {
		for(int i=0; i<attributes.size(); i++) {
			AssetAttribute attribute = attributes.get(i);
			if (assetEvaluator.isValid(attribute)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public abstract List<Asset> divideUsingLandAsset(List<Asset> dividedLandAssets, int divisor);

	@Override
	public final Asset retrieve(int retrievedQuantity) {
		List<T> retrievedAttributes = new ArrayList<>(attributes.subList(0, retrievedQuantity));
		this.attributes = new ArrayList<>(attributes.subList(retrievedQuantity, attributes.size()));
		this.remainingProduce = getAssetType().getAssetProduceForQuantity(attributes.size());
		return newInstance(getAssetType(), retrievedAttributes, getAssetType().getAssetProduceForQuantity(retrievedAttributes.size()));
	}
	
	public final Asset retrieveIndex(int locationIndex) {
		List<T> retrievedAttributes = new ArrayList<>(attributes.subList(locationIndex, locationIndex + 1));
		this.attributes = new ArrayList<>(attributes);
		this.attributes.remove(locationIndex);
		this.remainingProduce = getAssetType().getAssetProduceForQuantity(attributes.size());
		return newInstance(getAssetType(), retrievedAttributes, getAssetType().getAssetProduceForQuantity(retrievedAttributes.size()));
	}
	
	@Override
	public final String toString() {
		return "assetType: " + getAssetType() + ", attributes: " + attributes;
	}
}
