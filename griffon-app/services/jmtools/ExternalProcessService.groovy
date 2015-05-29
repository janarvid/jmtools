package jmtools

import groovy.transform.CompileStatic;

import org.codehaus.griffon.runtime.core.AbstractGriffonService;

@CompileStatic
class ExternalProcessService extends AbstractGriffonService {
    // void serviceInit() {
    //    // this method is called after the model is instantiated
    // }

    // void serviceDestroy() {
    //    // this method is called when the service is destroyed
    // }

    def executeCommand(File currentDir, List<String> cmd, Appendable logger) {
		println "${getClass().name}.executeCommand($currentDir, $cmd, logger)"
		ProcessBuilder pb = new ProcessBuilder(cmd)
		pb.directory(currentDir)
		pb.redirectErrorStream(true) // merge stdout, stderr of process
	
		Process p = pb.start()
		InputStreamReader isr = new  InputStreamReader(p.inputStream)
		BufferedReader br = new BufferedReader(isr)
	
		String lineRead
		while ((lineRead = br.readLine()) != null) {
			logger.append(lineRead)
			logger.append('\n')
		}
	
		p.waitFor()
    }
}
