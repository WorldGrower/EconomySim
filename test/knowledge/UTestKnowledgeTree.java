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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestKnowledgeTree {

	@Test
	public void testCreation() {
		Knowledge[] knowledgeArray = { Knowledge.STONE_TOOLING, Knowledge.CLOTHING, Knowledge.WEAVING };
		KnowledgeTree knowledgeTree = new KnowledgeTree(knowledgeArray);
		
		assertEquals(2, knowledgeTree.size());
		assertArrayEquals(new Knowledge[]{ Knowledge.STONE_TOOLING, Knowledge.CLOTHING }, knowledgeTree.getKnowledge(0));
		assertArrayEquals(new Knowledge[]{ Knowledge.WEAVING }, knowledgeTree.getKnowledge(1));
	}
}
