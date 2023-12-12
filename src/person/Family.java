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
package person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import asset.Asset;
import asset.AssetContainer;
import asset.AssetTrait;
import asset.AssetType;
import asset.Assets;
import organization.Organization;

public class Family implements Serializable {
	private Person partner;
	private final List<Person> children = new ArrayList<>();
	private final List<Person> parents = new ArrayList<>();
	
	public void addChild(Person parent, Person child) {
		children.add(child);
		child.getFamily().parents.add(parent);
		if (partner != null) {
			partner.getFamily().children.add(child);
			child.getFamily().parents.add(partner);
		}
	}
	
	public void onDeath(Person dyingPerson) {
		if (partner != null) {
			partner.getFamily().partner = null;
		}
		if (children.size() > 0) {
			for(Person child : children) {
				child.getFamily().parents.remove(dyingPerson);
			}
		}
		if (parents.size() > 0) {
			for(Person parent : parents) {
				parent.getFamily().children.remove(dyingPerson);
			}
		}
	}
	
	public void divideAssets(Assets assets, AssetContainer assetContainer) {
		int divisor = (partner != null ? 1 : 0) + children.size();

		if (divisor > 0) {
			List<Assets> dividedAssets = assets.divide(divisor);
			
			int currentIndex = 0;
			if (partner != null) {
				partner.getAssets().addAssets(dividedAssets.get(currentIndex));
				currentIndex++;
			}
			for(Person child : children) {
				child.getAssets().addAssets(dividedAssets.get(currentIndex));
				currentIndex++;
			}
		} else {
			assetContainer.addAssets(assets);
		}
	}
	
	public Asset findAssetToWorkOn(Assets assets, AssetType neededAssetType) {
		Assets[] familyAssetsList = getFamilyAssets(assets);
		for(Assets familyAssets : familyAssetsList) {
			Asset producingAsset = familyAssets.getProducingAsset(neededAssetType);
			if (producingAsset != null) {
				return producingAsset;
			}
		}
		return null;
	}
	
	//method returns array for performance reasons, as this method is called frequently
	public Assets[] getFamilyAssets(Assets assets) {
		int size = 1 + (partner != null ? 1 : 0) + parents.size();
		Assets[] familyAssets = new Assets[size];
		int index = 0;
		familyAssets[index++] = assets;
		if (partner != null) {
			familyAssets[index++] = partner.getAssets();
		}
		for(Person parent : parents) {
			familyAssets[index++] = parent.getAssets();
		}
		return familyAssets;
	}
	
	public Asset getAsset(Assets assets, AssetType assetType) {
		Assets[] familyAssetsList = getFamilyAssets(assets);
		for(Assets familyAssets : familyAssetsList) {
			Asset asset = familyAssets.get(assetType);
			if (asset != null) {
				return asset;
			}
		}
		return null;
	}
	
	public int getQuantityFor(Assets assets, AssetType assetType) {
		int quantity = 0;
		Assets[] familyAssetsList = getFamilyAssets(assets);
		for(Assets familyAssets : familyAssetsList) {
			quantity += familyAssets.getQuantityFor(assetType);
		}
		return quantity;
	}
	
	public boolean findAssetWithTrait(Assets assets, AssetTrait assetTrait) {
		Assets[] familyAssetsList = getFamilyAssets(assets);
		for(Assets familyAssets : familyAssetsList) {
			boolean hasTrait = familyAssets.hasTrait(assetTrait);
			if (hasTrait) {
				return hasTrait;
			}
		}
		return false;
	}

	public Person getPartner() {
		return partner;
	}

	public void setPartner(Person person, Person newPartner) {
		partner = newPartner;
		newPartner.getFamily().partner = person;
	}

	public List<Person> getParents() {
		return Collections.unmodifiableList(parents);
	}

	public List<Person> getChildren() {
		return Collections.unmodifiableList(children);
	}
	
	public boolean parentIsMemberOf(Person mother, Organization organization) {
		if (organization.contains(mother)) {
			return true;
		}
		if (partner != null && organization.contains(partner)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "partner: " + partner + ", children: " + children + ", parents: " + parents;
	}
}
