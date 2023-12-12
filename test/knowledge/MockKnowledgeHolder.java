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
package knowledge;

public class MockKnowledgeHolder implements KnowledgeHolder {

	private final int percentage;
	private final boolean hasKnowledge;
	
	public MockKnowledgeHolder(int percentage, boolean hasKnowledge) {
		super();
		this.percentage = percentage;
		this.hasKnowledge = hasKnowledge;
	}

	@Override
	public int getKnowledgePercentageKnown(Knowledge knowledge) {
		return percentage;
	}

	@Override
	public boolean hasKnowledge(Knowledge knowledge) {
		return hasKnowledge;
	}

}
