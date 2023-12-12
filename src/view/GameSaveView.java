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

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import society.Society;
import util.GameSave;

public class GameSaveView {

	private final JFileChooser fileChooser;
	
	public GameSaveView() {
		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Economy Save files", "sav");
		fileChooser.setFileFilter(filter);
	}
	
	public Society loadGame(Component parent) {
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return GameSave.load(selectedFile.getAbsolutePath());
        } else {
        	return null;
        }
	}
	
	public void saveGame(Component parent, Society society) {
		String defaultFilename = "economysim-" + System.currentTimeMillis() + ".sav";
		fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), defaultFilename));
		int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            GameSave.save(selectedFile.getAbsolutePath(), society);
        }
	}
}
