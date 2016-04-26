package spopo.sonas.menu;

/**
 * Created by Luca on 4/23/2016.
 */
public interface MenuItem {
    public boolean onSelected(Menu callingItem);
    //public int getTitleSound(); //TODO: dynamic menu sound generation by concating the MenuItems
}
