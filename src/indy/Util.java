package indy;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Util {

	private final static int LOOPS = 4096 * 4096;
	
	public static int veryExpensiveComputation1() {
		return fib(30);
	}

	private static int fib(int n) {
        if (n <= 1) return n;
        else return fib(n-1) + fib(n-2);
	}

	public static String veryExpensiveComputation2() {
		return String.valueOf(veryExpensiveComputation1());
	}

	public static void main(String[] args) {
//		final Lazy lazy = new IndyLazy();
		final Lazy lazy = new LazyImpl();
		
        List<Callable<Long>> tasks = new ArrayList<Callable<Long>>(10);
        // make a runnable that walks the whole jdk model
        Callable<Long> runnable = new Callable<Long>(){
            @Override
            public Long call() throws Exception {
                System.out.println("Starting work from thread " + Thread.currentThread().getName());
                Long l = doWork(lazy);
                System.out.println("Done work from thread " + Thread.currentThread().getName());
                return l;
            }
        };
        // make ten tasks with the same runnable
        for(int i=0;i<10;i++){
            tasks.add(runnable);
        }
        // create an executor
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(10));
        try {
            System.out.println("Launching all threads");
            // run them all
            List<Future<Long>> futures = executor.invokeAll(tasks);
            // and wait for them all to be done
            long total = 0;
            for(Future<Long> f : futures){
                total += f.get();
            }
            executor.shutdown();
            System.out.println("Done: "+lazy.getCount()+", "+lazy.getResult()+" in "+total/1000000+"ms");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
	}

	protected static long doWork(Lazy lazy) {
		int res = 1;
		long start = System.nanoTime();
		for(int i=0;i<LOOPS;i++){
			String result = lazy.getResult();
			res += fib(5) * i * lazy.getCount() + result.codePointCount(0, result.length());
		}
		System.out.println("Result: "+res + " LOOPS: "+LOOPS);
		return System.nanoTime() - start;
	}
	
	public static void callAndInvalidate(MethodHandle call, SwitchPoint sp, Lazy target) throws Throwable{
		// we bind the checkInit to the current Lazy target and invoke it
		call.bindTo(target).invokeExact();
		// now we invalidate the switch point, which means this should never be called again
		SwitchPoint.invalidateAll(new SwitchPoint[]{sp});
	}
	
	public static CallSite bootstrap(Lookup lookup, String name, MethodType type) throws Exception {
		// checkInit is the method we want to call once
		MethodHandle checkInit = lookup.findVirtual(IndyLazy.class, "checkInit", MethodType.methodType(void.class));
		
		// this is a noop method of type void(Lazy) that will replace the checkInit call after we call it once
		MethodHandle noop = MethodHandles.constant(Object.class, null).asType(MethodType.methodType(void.class));
		MethodHandle noopCall = MethodHandles.dropArguments(noop, 0, Lazy.class);

		// make a switch point
		SwitchPoint switchPoint = new SwitchPoint();
		
		// the first call will call callAndInvalidate(checkInit, switchPoint, lazyTarget)
		MethodHandle callAndInvalidate = lookup.findStatic(Util.class, "callAndInvalidate", MethodType.methodType(void.class, MethodHandle.class, SwitchPoint.class, Lazy.class));
		// provide the checkInit and switchPoint arguments ourselves
		MethodHandle callAndInvalidateBound = MethodHandles.insertArguments(callAndInvalidate, 0, checkInit, switchPoint);
		
		// setup the SwitchPoint
		MethodHandle test = switchPoint.guardWithTest(callAndInvalidateBound, noopCall);
		
		// constant call site
		return new ConstantCallSite(test);
	}
}
