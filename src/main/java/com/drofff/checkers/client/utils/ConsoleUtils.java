package com.drofff.checkers.client.utils;

import reactor.core.publisher.Mono;

import java.util.Scanner;

public class ConsoleUtils {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleUtils() {}

    public static String getUserInput(String request) {
        System.out.print(request);
        return SCANNER.nextLine();
    }

    public static Mono<String> printStrMono(Mono<String> strMono) {
        return strMono.doOnError(ConsoleUtils::handleException)
                .doOnNext(System.out::println);
    }

    private static void handleException(Throwable throwable) {
        System.out.println("Error: " + throwable.getMessage());
    }

}