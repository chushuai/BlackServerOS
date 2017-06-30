/*
 * Sleuth Kit Data Model
 *
 * Copyright 2013 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.datamodel;

import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Performs a "Bottom Up" test by verifying that calls to getParent() are the
 * same as calls to getChildren() from the root (i.e. the TopDown test). Does
 * not compare against a gold standard. Basic concept is that TopDown test makes
 * a "leaf file" with a line for each file that contains the list of parent IDs
 * for the file. The code in this test starts at each leaf file, calls
 * getParent() recursively, and verifies that the same IDs are reached.
 */
@RunWith(Parameterized.class)
public class BottomUpTest {

	private List<String> imagePaths;

	public BottomUpTest(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}

	/**
	 * Get the sets of filenames for each test image, they should be located in
	 * the folder specified by the build.xml
	 *
	 * @return A Collection of one-element Object arrays, where that one element
	 * is a List<String> containing the image file paths (the type is weird
	 * because that's what JUnit wants for parameters).
	 */
	@Parameterized.Parameters
	public static Collection<Object[]> testImageData() {
		Collection<Object[]> data = new ArrayList<Object[]>();

		for (Object imagePaths : DataModelTestSuite.getImagePaths()) {
			data.add(new Object[]{imagePaths});
		}
		return data;
	}

	/**
	 * Runs a bottom up traversal of the image, starting at each leaf and going
	 * until it reaches the top
	 */
	@Test
	public void testBottomUpDiff() {
		String title = DataModelTestSuite.getImgName(imagePaths.get(0));
		String exFile = DataModelTestSuite.buildPath(DataModelTestSuite.getRsltDirPath(), title, DataModelTestSuite.BTTMUP, ".txt");
		try {
			java.io.File dbFile = new java.io.File(DataModelTestSuite.getRsltDirPath());
			String tempDirPath = dbFile.getAbsolutePath();
			String dbPath = DataModelTestSuite.buildPath(tempDirPath, title, DataModelTestSuite.BTTMUP, ".db");
			dbFile.delete();
			SleuthkitCase sk = SleuthkitCase.newCase(dbPath);
			String timezone = "";
			title = title + DataModelTestSuite.BTTMUP + ".txt";
			SleuthkitJNI.CaseDbHandle.AddImageProcess process = sk.makeAddImageProcess(timezone, true, false, "");
			try {
				process.run(imagePaths.toArray(new String[imagePaths.size()]));
			} catch (TskDataException ex) {
				List<Exception> inp = new ArrayList<Exception>();
				inp.add(ex);
				DataModelTestSuite.writeExceptions(exFile, inp);
			}
			process.commit();

			// open the "leaf file" generated by the TopDown test and verify that getParent() follows the same sequence
			java.io.File lvs = new java.io.File(dbFile.getAbsolutePath() + java.io.File.separator + title);
			Scanner climber;
			climber = new Scanner(lvs);
			while (climber.hasNextLine()) {
				String cliNL = climber.nextLine();
				cliNL = cliNL.substring(1);
				String[] ids = cliNL.split("[\\],]\\s?+");
				Content c = sk.getContentById(Integer.parseInt(ids[0]));
				for (int x = 0; x < ids.length; x++) {
					assertEquals("Got ID " + c.getId() + " should have gotten ID " + ids[x], ids[x].equals(((Long) c.getId()).toString()), true);
					c = c.getParent();
				}
			}
		} catch (NumberFormatException ex) {
			System.out.println(ex.toString());
			fail("Failed to run BottomUp test: " + ex.getMessage());
		} catch (TskCoreException ex) {
			List<Exception> inp = new ArrayList<Exception>();
			inp.add(ex);
			DataModelTestSuite.writeExceptions(exFile, inp);
		} catch (FileNotFoundException ex) {
			List<Exception> inp = new ArrayList<Exception>();
			inp.add(ex);
			DataModelTestSuite.writeExceptions(exFile, inp);
		}
	}
}
