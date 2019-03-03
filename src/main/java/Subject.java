import java.util.ArrayList;
import java.util.List;

public class Subject {

    private List<Observer> observers = new ArrayList<Observer>();

    String responseData;

    public void attach(Observer observer){
        observers.add(observer);
        observer.subject = this;
    }

    public void notifyAllObservers(String data){
        for (Observer observer : observers) {
            observer.update(data);
        }
    }

}
