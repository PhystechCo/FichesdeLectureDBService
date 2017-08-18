/**
 * 
 */
package co.phystech.aosorio.services;

import java.util.UUID;

/**
 * @author AOSORIO
 *
 */
public class RandomUuidGenerator implements IUuidGenerator {

	/* (non-Javadoc)
	 * @see co.phystech.services.IUuidGenerator#generate()
	 */
	@Override
	public UUID generate() {
		
		return UUID.randomUUID();
	}

}
