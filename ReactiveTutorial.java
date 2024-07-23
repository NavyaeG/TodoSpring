package com.springReactive.ReactiveProgramming;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReactiveTutorial {
	
	//Java reactive has to two inbuilt publishers; Mono and flux
	
	private Mono<String> testMono(){
		//normally we return as return "String". In Reactive programming, we need to wrap it in Mono
		
//		What is a a wrapper class and why we need it?
		
//		A Wrapper class in Java is a class whose object wraps or contains primitive data types. 
		
//
//		They convert primitive data types into objects. Objects are needed if we wish to modify the arguments passed into a method (because primitive types are passed by value).
//		The classes in java.util package handles only objects and hence wrapper classes help in this case also.
//		Data structures in the Collection framework, such as ArrayList and Vector, store only objects (reference types) and not primitive types.
//		An object is needed to support synchronization in multithreading.

		//mono can't return a null value so we need to return a null value use Mono.empty or Mono.justorEmpty
		//Mono is only for one data
		return Mono.justOrEmpty("Javaaaa")
				.log();
	}
	
	private Flux<String> testFlux(ArrayList<String> names){
		//Flux is used for more than one data
		//We can create Flux from Arrays, iterable,streams etc
		
		return Flux.fromIterable(names);
//		return Flux.just("Java","Cpp","Rust")
//				.log();
	}
	
	private Flux<String> testMap(ArrayList<String> names){
		Flux<String> flux=Flux.fromIterable(names);
		//map used to return a value that is not a publisher
		return flux.map(s->s.toUpperCase());
	}
	
	private Flux<String> testFlatMap(ArrayList<String> names){
		//flatMap() used to return a publisher
		Flux<String> flux=Flux.fromIterable(names);
		//Mono.just() used here as it is a reactive publisher
		return flux.flatMap(s -> Mono.just(s.toUpperCase()));
	}
	
	private Flux<String> testBasicSkip(ArrayList<String> names){
		//Skip used to skip values
		//Here flux has a delay of 1 second
		Flux<String> flux=Flux.fromIterable(names).delayElements(Duration.ofSeconds(1));
		//skips data for a duration of first two seconds
		//return flux.skip(Duration.ofSeconds(2));
		return flux.skipLast(2);
	}
	
	private Flux<Integer> testComplexSkip(){
		Flux<Integer> flux=Flux.range(1,20);
		return flux.skipWhile(integer->integer<10);
	}
	
	private Flux<Integer> testConcat(){
		//displays the flux1 first and then flux2 //1,2,3 //101,102,103
		//Concatenation is achieved by sequentially subscribing to the first source then waiting for it to complete before subscribing to the next, and so on until the last source completes
		Flux<Integer> flux1=Flux.range(1,20)
				.delayElements(Duration.ofSeconds(1));
		Flux<Integer> flux2=Flux.range(101,20)
				.delayElements(Duration.ofSeconds(1));;
		//concats two fluxes, can take more than two arguments
		return Flux.concat(flux1,flux2);
	}
	
	private Flux<Integer> testMerge(){
		//displays flux1 and flux2 together //1,101 //2,102 //3,103 .... 
		//Merge both fluxes are subscribed to the same publisher at the same time
		Flux<Integer> flux1=Flux.range(1,20)
				.delayElements(Duration.ofSeconds(1));
		Flux<Integer> flux2=Flux.range(101,20)
				.delayElements(Duration.ofSeconds(1));;
		//concats two fluxes, can take more than two arguments
		return Flux.merge(flux1,flux2);
	}
	
	private Flux<Tuple2<Integer,Integer>> testZip(){
		//displays in the form [1,101] [2,102] ...
		Flux<Integer> flux1=Flux.range(1,20)
				.delayElements(Duration.ofSeconds(1));
		Flux<Integer> flux2=Flux.range(101,20)
				.delayElements(Duration.ofSeconds(1));;
		//can zip 3 fluxes using Tuple3 and so on upto 8
		//concats two fluxes, can take more than two arguments
		// you can zip mono and flux, but mono gets over after 1 item		
		
		return Flux.zip(flux1,flux2);
	}
	
	private Mono<List<Integer>> testCollect(){
		//return type is mono as it returns a list which is one object // [1,2,3..20]
		Flux<Integer> flux=Flux.range(1,20);
		return flux.collectList();
	}
	
	private Flux<List<Integer>> testBuffer(){
		//Collect all incoming values into a single List buffer that will be emittedby the returned Flux once this Flux completes. 
		Flux<Integer> flux=Flux.range(1,20);
		//waits until 3 items are recieved from publisher and then sending to the subscriber
		//we can also set buffer based on time like buffer(Duration.ofSeconds(1))
		return flux.buffer(3);
	}
	
	private Mono<Map<Integer,Integer>> testMapCollection(){ 
		Flux<Integer> flux=Flux.range(1,10);
		return flux.collectMap(integer->integer,integer->integer*integer);
	}
	
	private Flux<Integer> testDoFunctions(){
		Flux<Integer> flux=Flux.range(1,10);
		
		//Signal: 
		//A domain representation of a Reactive Stream signal.There are 4 distinct signals and their possible sequence 
		//is defined as such:onError | (onSubscribe onNext* (onError | onComplete)?)
		
		//Add behavior (side-effects) triggered when the Flux emits an item, fails with an error or completes successfully. 
		//All these events are represented as a Signal that is passed to the side-effect callback
		return flux.doOnEach(signal->{
			if (signal.getType()==SignalType.ON_COMPLETE) {
				System.out.println("I am done");
			}
			else {
				System.out.println(signal.get());//singal.get() gets item in the flux
			}
		});
	}
	
	private Flux<Integer> testDoFunctions2(){
		Flux<Integer> flux=Flux.range(1,10);
		
		//Add behavior (side-effect) triggered when the Flux completes successfully
		return flux.doOnComplete(()->System.out.println("I am done"));
	}
	
	private Flux<Integer> testDoFunctions3(){
		Flux<Integer> flux=Flux.range(1,10);
		
		//doOnNext gives each entry from the flux
		return flux.doOnNext(integer->System.out.println(integer));
	}
	
	private Flux<Integer> testDoFunctions4(){
		Flux<Integer> flux=Flux.range(1,10)
				.delayElements(Duration.ofSeconds(1));
		
		//Add behavior (side-effect) triggered when the Flux is cancelled
		return flux.doOnCancel(()->System.out.println("Cancelled"));
	}
	
	public static void main(String[] args) throws InterruptedException {
		ReactiveTutorial reactiveTutorial= new ReactiveTutorial();
		//reactiveTutorial.testMono(). this wont work as in reactive progrraming it is a publisher and subscriber
		//model. So we need to first subscribe to the Publisher
		reactiveTutorial.testMono().subscribe(data->System.out.println(data));
		ArrayList<String> names=new ArrayList<>();
		Collections.addAll(names,"java","cpp","rust");
		
//		reactiveTutorial.testFlux(names).subscribe(data->System.out.println(data));
//		reactiveTutorial.testMap(names).subscribe(data->System.out.println(data));
//		reactiveTutorial.testFlatMap(names).subscribe(data->System.out.println(data));
//		reactiveTutorial.testBasicSkip(names).subscribe(data->System.out.println(data));
//		reactiveTutorial.testComplexSkip().subscribe(data->System.out.println(data));
//		reactiveTutorial.testConcat().subscribe(data->System.out.println(data));
//		reactiveTutorial.testMerge().subscribe(data->System.out.println(data));
//		reactiveTutorial.testZip().subscribe(data->System.out.println(data));
		
//		reactiveTutorial.testCollect().subscribe(data->System.out.println(data));
//		Subscribe to this Mono and block indefinitely until a next signal is received.
//		List<Integer> output=reactiveTutorial.testCollect().block();//converting the non blocking object to blocking object// This is not recommended 
//		System.out.println(output);
		
//		reactiveTutorial.testBuffer().subscribe(data->System.out.println(data));
//		reactiveTutorial.testMapCollection().subscribe(data->System.out.println(data));
//		reactiveTutorial.testDoFunctions().subscribe();
//		reactiveTutorial.testDoFunctions2().subscribe(data->System.out.println(data));
//		reactiveTutorial.testDoFunctions3().subscribe();
		
		//Disposable indicates that a task or resource can be cancelled/disposed
		Disposable disposable=reactiveTutorial.testDoFunctions4().subscribe(data->System.out.println(data));
		Thread.sleep(3_500);
		disposable.dispose();//cancelling the subscription before it is finished
		
		
		//thread.sleep added here as return flux.delayElements is not a blocking operation so the program would just end
		Thread.sleep(10_000);
	}

}