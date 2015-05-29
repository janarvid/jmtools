package jmtools.controllers;

import static org.junit.Assert.*;
import groovy.lang.Closure;
import groovy.transform.CompileStatic;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@CompileStatic
class AbstractJMToolsControllerTests {
	AbstractJMToolsController cut
	int[] selectedRows
	JTable table

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	static class MockJMToolsController extends AbstractJMToolsController {
		void edt(Closure closure) {
//			println "closure=$closure"
			closure.call()
		}
	}

	@Before
	public void setUp() throws Exception {
		selectedRows = new int[1]
		selectedRows[0] = 7
		table = [getSelectedRows: {selectedRows}] as JTable
		cut = new MockJMToolsController()
	}

	@Test
	public void testSelectedRows() {
		List<Integer> selRows = cut.getSelectedRows(table)
		assertNotNull(selRows)
		//println "selRows=$selRows"
		assertEquals(selectedRows.toList(), selRows)
	}

	@Test
	public void testGetMinOneSelectedRowsJTable() {
//		fail("Not yet implemented");
	}

	@Test
	public void testDoRefreshDir() {
//		fail("Not yet implemented");
	}

}
