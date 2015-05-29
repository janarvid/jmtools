package jmtools

import org.veggeberg.jmtools.ArchiveUnpacker;

class PrepareFilesService {
    // void serviceInit() {
    //    // this method is called after the model is instantiated
    // }

    // void serviceDestroy() {
    //    // this method is called when the service is destroyed
    // }

    def unpackArchive(File srcArk, File destDir) {
		final unpacker = new ArchiveUnpacker()
		if ( ! destDir.exists() ) {
			assert destDir.mkdir()
		}
		unpacker.unpackOne(destDir, srcArk)
		unpacker.flattenOne(destDir)
    }
}
