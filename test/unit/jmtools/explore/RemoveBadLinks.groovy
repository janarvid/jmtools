package jmtools.explore

import groovy.transform.CompileStatic;

@CompileStatic
class RemoveBadLinks 
{
	static main(args) {
		new File('m:\\Music\\remove_these.txt').eachLine { String fname ->
			if (fname.startsWith('find:')) return
			final file = new File('m:\\Music\\'+fname.substring(2))
			println file
			assert file.delete()
		}
	}

}
