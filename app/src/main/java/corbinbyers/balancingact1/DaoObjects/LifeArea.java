package corbinbyers.balancingact1.DaoObjects;

/**
 * Created by Corbin Byers on 3/6/2017.
 */

public class LifeArea {
    public Integer id;
    public String title;
    public Integer selected;

    public LifeArea(){}

    public LifeArea(Integer i, String t, Integer s){
        id = i;
        title = t;
        selected = s;
    }
}
