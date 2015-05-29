package org.veggeberg.jmtools

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

class ArchiveUnpackerTest 
{
	ArchiveUnpacker cut
	private static FS = File.separator
	static File arkResDir = new File("test/resources/archives")
	static File dir 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dir = File.createTempDir()
		assert dir.exists() || dir.isDirectory()
		for (i in 1..3) {
			final file = new File("$dir/file${i}.txt")
			file.text = "file${i} content"
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//new AntBuilder().delete(dir: lettersTopDir.toString())
	}

	@Before
	public void setUp() throws Exception {
		cut = new ArchiveUnpacker()
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testTar() {
		def dir = "${arkResDir}/tar"
		//ArchiveUnpacker.main(dir) // TODO
	}

	@Test
	public void testZip() {
		AntBuilder ant = new AntBuilder()
		File zipFile = new File("${dir}.zip")
//		ant.echo("Heia deranne")
		ant.zip(destFile: zipFile, basedir: dir)
		File unzipDestDir = File.createTempDir()
		cut.unpackOne(unzipDestDir, zipFile)
		dir.eachFile { File expFile ->
			File actFile = new File("${unzipDestDir}/${expFile.name}")
			assertEquals(expFile.text, actFile.text)
		}
	}
}
