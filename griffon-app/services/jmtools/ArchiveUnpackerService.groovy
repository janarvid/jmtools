package jmtools

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

class ArchiveUnpackerService {
	static final UNPACK_CMD_BY_FILETYPE = ['7z': ['7z', 'x'], rar: ['unrar', 'x'], zip: 'unzip', ace: 'unace x']
	ExternalProcessService externalProcessService 
    // void serviceInit() {
    //    // this method is called after the model is instantiated
    // }

    // void serviceDestroy() {
    //    // this method is called when the service is destroyed
    // }
	
	// TODO Should use ExternalProcessService
	/*
	void runCommand(File dir, cmd) {
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
	
	*/
	
	private static void flattenOne(File dir) {
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
/*	
	void unpackDir(File dir) {
		dir.eachFile { file ->
			def i = file.toString().toLowerCase().lastIndexOf('.')
			if (i > 0) {
				def upDir = new File(file.toString().substring(0, i))
				if ( ! upDir.mkdir() ) {
					throw new IOException("Unable to create directory '$upDir'")
				}
				unpackOne(upDir, file.name)
				flattenOne(upDir)
			}
		}
	}
*/
    def unpackArchive(File arkFile, logger) {
		def dir = arkFile.parent
		def fileName = arkFile.name
		
		// Set the command for unpacking
		def suffix = FilenameUtils.getExtension(fileName)
		def cmd = (suffix) ? UNPACK_CMD_BY_FILETYPE[suffix] : null
		if ( ! cmd ) {
			throw new IOException("Don't know how to unpak a file with suffix '$suffix' for file '$arkFile'")
		}
		cmd << "../$fileName".toString()
		
		// Create an empty dir with the same name as the archive and so unpacking can be done there
		File arkUnpackDir = new File(FilenameUtils.removeExtension(arkFile.toString()))
		if (! arkUnpackDir.exists()) {
			if (! arkUnpackDir.mkdir()) {
				throw new IOException("Unable to create directory '$arkUnpackDir'")
			}
		}
		log.debug "arkDir=$arkUnpackDir, cmd=$cmd"
		
		// Run the command to unpack the archive
		externalProcessService.executeCommand(arkUnpackDir, cmd, logger)
		flattenOne(arkUnpackDir)
    }
}
