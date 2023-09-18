import java.util.Random;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
public class Main extends Canvas implements Runnable{
  public static final int WIDTH=800, HEIGHT=WIDTH/12 *9;
  private Thread thread;
  private boolean running =false;
  private Handler handler;
  private Menu menu;
  public int seed=1334559;
  public int playerX=WIDTH/2;
  public int playerY=HEIGHT/2;
  public int startX=playerX-WIDTH/2;
  public int startY=playerY-HEIGHT/2;
  Double zoomFactor=1.0;
  public StarSystem[] systemList=new StarSystem[20];
  public enum STATE{
    Menu,
    Generate,
    Map,
    Game
  }
  //for changing the view when you click on a system
  public void resetCoords(double xChange,double yChange){
    for(int i = 0;i<systemList.length;i++){

      systemList[i].rX+=xChange;
      systemList[i].rY+=yChange;
    }
  }

  public STATE gameState=Main.STATE.Menu;
  public Main(){
    handler= new Handler();
    menu=new Menu(this, handler);
    this.addKeyListener(new KeyInput(handler));
    this.addMouseListener(menu);
    new Window(WIDTH, HEIGHT,"Space game",this);

    /*if(gameState==STATE.Game){
      Scanner sc = new Scanner(System.in);
      String input;
      Random random = new Random();
      System.out.println("What seed are you using?");
      random.setSeed(sc.nextLong());
      StarSystem s = new StarSystem(WIDTH/2,HEIGHT/2,ID.StarSystem,random.nextInt(1000000), random.nextInt(100), random.nextDouble(), random);

      handler.addObject(s);
      for(int i=0;i<s.planetList.size();i++){
        Planet tempPlanet=s.planetList.get(i);
        handler.addObject(tempPlanet);
      }

    }*/
  }
  public synchronized void start(){
    thread= new Thread(this);
    thread.start();
    running = true;
  }
  public synchronized void stop(){
    try{
      thread.join();
      running = false;
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  public void run(){
    long lastTime=System.nanoTime();
    double amountOfTicks=60.0;
    double ns=1000000000/amountOfTicks;
    double delta=0;
    long timer=System.currentTimeMillis();
    int frames=0;
    while(running){
      long now=System.nanoTime();
      delta+=(now-lastTime)/ns;
      lastTime=now;
      while(delta>=1){
        tick();
        delta--;
      }
      if(running){
        render();
      }
      frames++;
      if(System.currentTimeMillis()-timer>1000){
        timer+=1000;
        //System.out.println("FPS: "+frames);
        //System.out.println("The handler contains "+handler.getSize()+" objects");
        frames=0;
      }
    }
    stop();
  }
  private void tick(){
    handler.tick();
    if(gameState==STATE.Game){
      //tick for games
    }else if(gameState==STATE.Menu){
      menu.tick();
    }
  }
  private void render(){

    BufferStrategy bs=this.getBufferStrategy();
    if(bs==null){
      this.createBufferStrategy(3);
      return;
    }

    Graphics g = bs.getDrawGraphics();
    g.setColor(Color.black);
    g.fillRect(0,0,WIDTH,HEIGHT);
    handler.render(g);
    menu.render(g);
    g.dispose();
    bs.show();
  }
  public static void main(String[] args){
    new Main();
  }
}
