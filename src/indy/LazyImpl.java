package indy;

public class LazyImpl implements Lazy {
	
	private int count;
	private String result;
	private volatile boolean initialised = false;
	
	private void checkInit(){
		if(!initialised){
			synchronized(this){
				if(!initialised){
					init();
					initialised = true;
				}				
			}
		}
	}

	private void init() {
		System.out.println("Initialisation");
		count = Util.veryExpensiveComputation1();
		result = Util.veryExpensiveComputation2();
	}

	@Override
	public int getCount() {
		checkInit();
		return count;
	}

	@Override
	public String getResult() {
		checkInit();
		return result;
	}
}
