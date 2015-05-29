package org.veggeberg.jmtools

import groovy.transform.CompileStatic;
import groovy.transform.TypeCheckingMode;

import org.apache.commons.io.FileUtils;

@CompileStatic
class ArchiveUnpacker 
{
	static final Map<String, List<String>> UNPACK_CMD_BY_FILETYPE = [:]
	
	static {
		UNPACK_CMD_BY_FILETYPE.putAll([
			'7z': ['7z','x'],
			ace: ['unace', 'x'],
			rar: ['unrar','x'],
			tar: ['tar','xf'],
			zip: ['unzip'],
		])
	}
	
	void runCommand(File dir, List<String> cmd) {
		println "runCommand($dir, $cmd)"
		ProcessBuilder pb = new ProcessBuilder(cmd);
		pb.directory(dir)
		pb.redirectErrorStream(true); // merge stdout, stderr of process
	
		Process p = pb.start();
		InputStreamReader isr = new  InputStreamReader(p.getInputStream());
		BufferedReader br = new BufferedReader(isr);
	
		String lineRead;
		while ((lineRead = br.readLine()) != null) {
			println lineRead
		}
	
		int rc = p.waitFor();
	}
	
	@CompileStatic(TypeCheckingMode.SKIP)
	void unzip(File zipFile, File dir) {
		AntBuilder ant = new AntBuilder()
		ant.unzip(src: zipFile, dest: dir)
	}
	
	void unpackOne(File dir, File arkFile) {
		final i = arkFile.toString().toLowerCase().lastIndexOf('.')
		final suffix = (i > 0) ? arkFile.toString().substring(i+1) : null
		List<String> cmd
		if (suffix) {
			cmd = (List)UNPACK_CMD_BY_FILETYPE[suffix]
			if (cmd) {
				cmd = new ArrayList(cmd)
				if ( ! cmd ) {
					println "Unknown suffix '$suffix' for file '$arkFile'"
					return
				}
			}
			else {
				println "File '$arkFile' does not have suffix"
				return
			}
		}
//		println "cmd=$cmd"
		def arkFilePath = arkFile.isAbsolute() ? arkFile : new File("../$arkFile")
		cmd << arkFilePath.toString()
//		println "dir=$dir, cmd=$cmd"
		if (suffix == 'zip') {
			unzip(arkFilePath, dir)
		}
		else {
			runCommand(dir, cmd)
		} 
	}
	
	void flattenOne(File dir) {
		def dirs = dir.listFiles()
		if (dirs.size() == 1 && dirs[0].isDirectory()) {
			for (file in dirs[0].listFiles()) {
				if (file.isDirectory()) {
					FileUtils.moveDirectoryToDirectory(file, dir, false)
				}
				else {
					FileUtils.moveFileToDirectory(file, dir, false)
				}
			} 
			if ( ! dirs[0].delete()) {
				throw new IOException("Unable to delete directory '${dirs[0]}")
			}
		}
	}
	
	void unpackDir(File dir) {
		dir.eachFile { File arkFile ->
			final i = arkFile.toString().toLowerCase().lastIndexOf('.')
			if (i > 0) {
				final upDir = new File(arkFile.toString().substring(0, i))
				if ( ! upDir.mkdir() ) {
					throw new IOException("Unable to create directory '$upDir'")
				}
//				println "Before unpackOne($upDir, $arkFile)"
				unpackOne(upDir, arkFile)
				flattenOne(upDir)
			}
		}
	}

	static void main(String[] args) {
		if (args.size() != 1) {
			System.err.println "Usage: ArchiveUnpacker dir_with_archive_files"
			System.exit 45
		}
		def au = new ArchiveUnpacker()
		au.unpackDir(new File(args[0]))
	}
}
