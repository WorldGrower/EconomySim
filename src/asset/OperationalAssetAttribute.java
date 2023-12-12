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

import java.io.Serializable;

public class OperationalAssetAttribute implements AssetAttribute, Serializable {
	
	private static final int MAX_OPERATIONAL = 1000;
	private static final int OPERATIONAL_THRESHOLD = 950;
	
	private int operational = 0;
	private boolean finished = false;

	public void increaseOperational() {
		operational++;
		if (isOperational()) {
			finished = true;
		}
		if (operational > MAX_OPERATIONAL) {
			operational = MAX_OPERATIONAL;
		}
	}
	
	public void decreaseOperational() {
		operational -= 10;
		if (operational < 0) {
			operational = 0;
		}
	}
	
	public boolean isOperational() {
		return operational >= OPERATIONAL_THRESHOLD;
	}

	@Override
	public String toString() {
		return operational + " / " + MAX_OPERATIONAL;
	}

	@Override
	public int getAge() {
		return 0;
	}

	@Override
	public boolean endOfTurn() {
		if (isFinished()) {
			decreaseOperational();
			return operational == 0;
		} else {
			return false;
		}
	}

	public boolean isFinished() {
		return finished;
	}
}
