log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    appenders {
        console name: 'stdout', layout: pattern(conversionPattern: '%d [%t] %-5p %c - %m%n')
    }

    error  'org.codehaus.griffon'
	
	warn   'org.jaudiotagger'

    info   'griffon.util',
           'griffon.core',
           'griffon.swing',
           'org.springframework'
		   
	debug  'griffon.app',
		   'org.veggeberg.jmtools'
}

griffon.config.locations = [
	"classpath:jmtools-config.groovy",
	//"file:${userHome}/.griffon/${appName}-config.groovy"
	]

// Needed for junit tests
griffon.mvcid.collision = 'warning' // accepted values are 'warning', 'exception' (default)