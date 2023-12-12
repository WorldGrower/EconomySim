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

import environment.Location;

public class LocatableAssetAttribute implements AssetAttribute, Serializable {
	private final int id;
	private int capacity = 100;
	private int age = 0;
	private boolean canSetCapacity = true;
	
	public LocatableAssetAttribute(int id) {
		this.id = id;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		if (canSetCapacity) {
			this.capacity = capacity;
			this.canSetCapacity = false;
		} else {
			throw new IllegalStateException("capacity cannot be set");
		}
	}
	
	public boolean canSetCapacity() {
		return canSetCapacity;
	}

	@Override
	public int getAge() {
		return age;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		LocatableAssetAttribute other = (LocatableAssetAttribute) obj;
		return id == other.id;
	}

	@Override
	public boolean endOfTurn() {
		age++;
		canSetCapacity = true;
		return false;
	}

	public Location getLocation() {
		return new Location(id);
	}
	
	@Override
	public String toString() {
		return "capacity: " + capacity + ", age: " + age;
	}
}
