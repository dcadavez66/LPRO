
abstract class Conv implements Runnable{

	Thread thread;
	
	protected String receiver;
	protected Conversation conv;
	
	public Conv(String receiver, Conversation c) {
		this.receiver = receiver;
		this.conv = c;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void startrunning() {
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void killthread() {
		thread.interrupt();	
	}
	
	
}
