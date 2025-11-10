package com.motorbike.main.shared;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    protected List<Subscriber> subscribers = new ArrayList<>();

    public void addSubscriber(Subscriber s) {
        subscribers.add(s);
    }

    public void notifyAllSubscribers() {
        for (Subscriber s : subscribers) {
            s.update();
        }
    }
}
