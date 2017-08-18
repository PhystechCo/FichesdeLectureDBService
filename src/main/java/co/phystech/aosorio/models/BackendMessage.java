/**
 * 
 */
package co.phystech.aosorio.models;

/**
 * @author AOSORIO
 *
 */
public class BackendMessage {

	public class Message {
		
		private boolean errorInd = false;
		private String value;
		
		/**
		 * @return the errorInd
		 */
		public boolean isErrorInd() {
			return errorInd;
		}
		/**
		 * @param errorInd the errorInd to set
		 */
		public void setErrorInd(boolean errorInd) {
			this.errorInd = errorInd;
		}
		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
		
	}
	
	public Object getOkMessage(String message) {
		Message response = new Message();
		response.setErrorInd(false);
		response.setValue(message);
		return response;
	}


	public Object getNotOkMessage(String message) {
		Message response = new Message();
		response.setErrorInd(true);
		response.setValue(message);
		return response;
	}
	
	
}
