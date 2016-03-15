package py.pol.una.ii.pw.util;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class ServiceException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException(Exception cause) {   
        super(cause);
    }
}