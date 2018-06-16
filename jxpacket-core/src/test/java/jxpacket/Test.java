package jxpacket;

import jxpacket.ethernet.Ethernet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;

public class Test {

	static class MyPublisher implements Flow.Publisher<String> {

		@Override
		public void subscribe(Flow.Subscriber<? super String> subscriber) {

		}

	}

	static class MySubcriber implements Flow.Subscriber<Integer> {

		@Override
		public void onSubscribe(Flow.Subscription subscription) {

		}

		@Override
		public void onNext(Integer item) {

		}

		@Override
		public void onError(Throwable throwable) {

		}

		@Override
		public void onComplete() {

		}

	}

	public static void main(String[] args) throws IOException {
		List<Integer> integers = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99);
		System.out.println(
			integers.parallelStream()
				.filter(i -> i % 2 == 0)
				.findFirst().get()
		);

	}

}
