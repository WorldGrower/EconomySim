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
package view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import asset.Asset;
import asset.AssetType;

abstract class AbstractAssetsTableModel extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = { "Name", "Quantity", "RemainingProduce" };
	
	public AbstractAssetsTableModel() {
		super();
	}
	
	public abstract List<AssetType> getAssetTypes();
	public abstract Asset getAsset(AssetType assetType);
	
	@Override
	public final int getRowCount() {
		return getAssetTypes().size();
	}

	@Override
	public final int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public final String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public final Object getValueAt(int rowIndex, int columnIndex) {
		AssetType assetType = getAssetTypes().get(rowIndex);
		Asset asset = getAsset(assetType);
		switch(columnIndex) {
		case 0:
			return asset.getAssetType().toString();
		case 1:
			return asset.getQuantity();
		case 2:
			return asset.getRemainingProduceDescription();
		}
		return null;
	}

}
