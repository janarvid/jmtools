package org.veggeberg.jmtools.progarchives

import groovy.transform.CompileStatic;

@CompileStatic
class BaseMatcher
{	
	static Map<String,String> replaceMap = [
		/\(original version\)/: '',
		/\(live\)/: '',
		' & ': ' and ',
		/\(2 cd\)/: '',
		
		// a
		'á': 'a',
		'à': 'a',
		'ã': 'a',
		'ä': 'a',
		'å': 'a',
		'æ': 'ae',
		'�' : 'a',
		'�' : 'a',
		
		// c
		'ç': 'c',
		
		// e
		'é': 'e',
		'ę': 'e',
		'è': 'e',
		'�': 'e',
		'�' : 'e',
		
		// i
		'ï': 'i',
		'í': 'i',
		
		// l
		'ł': 'l',
		
		// n
		'ń': 'n',
		
		// o
		'ó': 'o',
		'ö': 'o',
		'ô': 'o',
		'ø': 'o',
		'�' : 'o',
		'�' : 'o',
		
		// s
		'ś': 's',
		
		// u
		'ù': 'u',
		'ü': 'u',
		'�': 'u',
		'�' : 'u',
		
		// y
		'ý': 'y',
		'�': 'y',
	    'ÿ': 'y',
		'�'	: 'y',
		'�' : 'y',
		];
}
