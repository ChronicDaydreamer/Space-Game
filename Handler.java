import java.awt.Graphics;
import java.util.LinkedList;

public class Handler{
  public LinkedList<GameObject> object = new LinkedList<GameObject>();
  public void tick(){
    for(int i =0;i<object.size();i++){
      GameObject tempObject=object.get(i);
      tempObject.tick();
    }
  }
  public void render(Graphics g){
    for(int i=0;i<object.size();i++){
      GameObject tempObject = object.get(i);
      tempObject.render(g);
    }
  }
  //add object to handler
  public void addObject(GameObject object){
    this.object.add(object);
  }
  //etc
  public void removeObject(GameObject object){
    this.object.remove(object);
  }
  //get the number of objects in the handler
  public int getSize(){
    return this.object.size();
  }
  //remove all objects from the handler
  public void empty(){
    this.object.clear();
  }
}
