/**
 * 
 */
package test;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dconyers
 *
 */
public class MainTest {

	
	private static Logger logger = LoggerFactory.getLogger(MainTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		logger.trace("top of MainTest::main()");
	}

}
