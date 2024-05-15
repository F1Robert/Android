package com.shsany.riskelectronicfence.observa;

import java.util.ArrayList;
import java.util.List;

public class UWBConnectObservable {
    public static boolean isUWBConnect = true;
    public static boolean isAIConnect = true;
    private static List<UWBConnectObserver> observers = new ArrayList<>();

    public static boolean isUWBConnect() {
        return isUWBConnect;
    }

    public static void setUWBConnect(boolean uwbConnect) {
        isUWBConnect = uwbConnect;
        notifyObservers();
    }

    public static void addObserver(UWBConnectObserver observer) {
        observers.add(observer);
    }

    public static void removeObserver(UWBConnectObserver observer) {
        observers.remove(observer);
    }

    private static void notifyObservers() {
        for (UWBConnectObserver observer : observers) {
            observer.onUWBConnectChanged(isUWBConnect);
        }
    }
}
