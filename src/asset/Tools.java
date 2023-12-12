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
import java.util.List;

public class Tools {
	public static final Tools NONE = new Tools(Collections.emptyList());
	
	private final List<Asset> tools;

	public Tools() {
		super();
		this.tools = new ArrayList<>();
	}
	
	private Tools(List<Asset> tools) {
		super();
		this.tools = tools;
	}

	public void add(Asset tool) {
		this.tools.add(tool);
	}

	public void checkRemainingProduce(Assets personAssets) {
		for(Asset tool : tools) {
			if (tool != null && !tool.hasRemainingProduce(AssetType.TOOL)) {
				personAssets.removeAsset(tool.getAssetType(), 1);
			}
		}
	}
	
	public int calculateProduction(int quantityProvided) {
		int production = quantityProvided;
		
		for(Asset tool : tools) {
			boolean toolUsed = useTool(tool);
			if (toolUsed) {
				production += tool.getAssetType().getProductionBonus();
			}
		}
		return production;
	}
	
	private static boolean useTool(Asset toolAsset) {
		AbstractAsset tool = (AbstractAsset) toolAsset;
		if (tool.remainingProduce.getProduce(AssetType.TOOL) > 0) {
			tool.remainingProduce.use(AssetType.TOOL);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tools == null) ? 0 : tools.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tools other = (Tools) obj;
		if (tools == null) {
			if (other.tools != null)
				return false;
		} else if (!tools.equals(other.tools))
			return false;
		return true;
	}
}
